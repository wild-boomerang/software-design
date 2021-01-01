package by.bsuir.wildboom.lab3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ConnectActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    private static EditText key;
    private UserConnect userConnect;
    private FirebaseFirestore db;
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String ref = intent.getStringExtra("reference");

            Intent newIntent = new Intent(context, MainActivity.class);
            userConnect.setConnectionId(ref);
            userConnect.getUser().setStatus("Creator");
            newIntent.putExtra("UserConnect", userConnect);
            context.startActivity(newIntent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        key = (EditText) findViewById(R.id.keyEditText);
        userConnect = new UserConnect((User)getIntent().getSerializableExtra("User"), "");
        db = FirebaseFirestore.getInstance();
    }

    public void createConnect(View view) {
        HashMap<String, Object> connection = new HashMap<>();
        connection.put("recipient", "");
        connection.put("sender", userConnect.getUser().getEmail());

        Intent intent = new Intent(this, ConnectService.class);
        intent.putExtra("connection", connection);
        intent.putExtra("email", userConnect.getUser().getEmail());
        this.startService(intent);

        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver,
                new IntentFilter("ServiceFinish"));
    }

    public void Connect(View view) {
        final Context context = this;
        final String connectionString = key.getText().toString();
        final Map<String, Object> recipient = new HashMap<>();
        recipient.put("recipient", userConnect.getUser().getEmail());

        if (connectionString.equals("")) {
            Toast.makeText(this, "Please, enter a connection string", Toast.LENGTH_SHORT).show();
        } else {
            db.collection("connections")
                .document(connectionString)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        DocumentSnapshot document = task.getResult();
                        if (Objects.requireNonNull(document).exists()) {
                            db.collection("connections")
                                    .document(connectionString)
                                    .update(recipient);

                            Intent intent = new Intent(context, MainActivity.class);
                            userConnect.getUser().setStatus("Connected");
                            userConnect.setConnectionId(connectionString);
                            intent.putExtra("ConnectionString", document.getId());
                            intent.putExtra("UserConnect", userConnect);
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "Invalid connection string!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        }
    }

    public static void setConnectionStringText(String text) {
        key.setText(text);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("UserConnect", userConnect);

        Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();

        startActivityForResult(intent, 1);

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("key", key.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        key.setText(savedInstanceState.getString("key"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            userConnect = (UserConnect) data.getSerializableExtra("UserConnect");
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        menu.add("Profile");

        return super.onCreateOptionsMenu(menu);
    }
}
