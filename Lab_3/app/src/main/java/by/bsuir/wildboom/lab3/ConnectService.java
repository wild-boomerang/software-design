package by.bsuir.wildboom.lab3;

import android.app.Service;
import android.os.IBinder;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;
import java.util.HashMap;
import java.util.Map;

public class ConnectService extends Service {
    private String ref;
    private boolean isListening = true;
    private boolean noBlock = false;

    public ConnectService() {

    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        HashMap<String, Object> connection = (HashMap<String, Object>)intent.getSerializableExtra("connection");
        String email = intent.getStringExtra("email");

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("connections").add(Objects.requireNonNull(connection));
        db.collection("connections")
            .whereEqualTo("sender", email)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (!task.isSuccessful()) {
                        return;
                    }

                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        ConnectActivity.setConnectionStringText(document.getId());
                        ref = document.getId();
                    }

                    final DocumentReference docRef = db.collection("connections").document(ref);
                    docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                            @Nullable FirebaseFirestoreException e) {
                            if (isListening) {
                                if (noBlock) {
                                    if (documentSnapshot != null && documentSnapshot.exists()) {
                                        Map<String, Object> result = documentSnapshot.getData();
                                        if (!Objects.requireNonNull(result).isEmpty()
                                                && !Objects.equals(result.get("recipient"), "")) {

                                            Intent intent = new Intent("ServiceFinish");
                                            intent.putExtra("reference", ref);
                                            LocalBroadcastManager.getInstance(ConnectService.this).sendBroadcast(intent);

                                            onDestroy();
                                            isListening = false;
                                        }
                                    }
                                }
                                noBlock = true;
                            }
                        }
                    });
                }
            });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
