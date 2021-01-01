package by.bsuir.wildboom.lab3;

import android.content.Intent;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;

import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    LoginViewModel enteredDataVM;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.emailEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        enteredDataVM = new ViewModelProvider(this).get(LoginViewModel.class);
        db = FirebaseFirestore.getInstance();
    }

    public void loginClick(View view) {
        enteredDataVM.setEmail(email.getText().toString());
        enteredDataVM.setPassword(password.getText().toString());

        validate();
    }

    private void validate() {
        if (enteredDataVM.getEmail().equals("") || enteredDataVM.getPassword().equals("")) {
            Toast.makeText(this, "Please, fill in all fields", Toast.LENGTH_SHORT).show();
        } else if (enteredDataVM.getPassword().length() <= 7 || !enteredDataVM.getEmail().contains("@")) {
                Toast.makeText(this,
                    "Password must contain at least 8 characters or wrong email format",
                        Toast.LENGTH_SHORT).show();
        } else {
            final User user = new User(email.getText().toString(), password.getText().toString());
            final Context context = this;

            db.collection("users")
                .whereEqualTo("email", user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean isCorrectPassword = true;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : Objects.requireNonNull(task.getResult())) {
                                Map<String, Object> result = doc.getData();

                                if (result.get("nickname") != null) {
                                    user.setNickname(Objects.requireNonNull(result.get("nickname")).toString());
                                }

                                if (result.get("imageUrl") != null) {
                                    user.setImageUrl(Objects.requireNonNull(result.get("imageUrl")).toString());
                                }

                                if (result.containsValue(user.getEmail()) &&
                                        !Objects.equals(result.get("password"), user.getPassword())) {
                                    isCorrectPassword = false;
                                }
                            }

                            if (task.getResult().isEmpty()) {
                                db.collection("users").add(user);
                            }

                            if (isCorrectPassword) {
                                Intent intent = new Intent(context, ConnectActivity.class);
                                intent.putExtra("User", user);
                                context.startActivity(intent);
                            } else {
                                Toast.makeText(context, "Wrong password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        }
    }

    @Override
    protected void onDestroy() {
        enteredDataVM.setEmail(email.getText().toString());
        enteredDataVM.setPassword(password.getText().toString());

        super.onDestroy();
    }
}