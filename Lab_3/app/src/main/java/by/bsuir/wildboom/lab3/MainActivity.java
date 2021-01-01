package by.bsuir.wildboom.lab3;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.EventListener;

import java.util.Objects;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView pageTitle;
    Button rotateShip;
    Spinner shipTypes;

    private FirebaseFirestore db;
    GameplayViewModel gameplayVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameplayVM = new ViewModelProvider(this).get(GameplayViewModel.class);

        if (gameplayVM.getOurField() == null) {
            gameplayVM.setOurField(new BattleFieldLogic());
            gameplayVM.setEnemyField(new BattleFieldLogic());
            gameplayVM.setConnectionId(getIntent().getStringExtra("ConnectionString"));
            gameplayVM.setUserConnect((UserConnect) getIntent().getSerializableExtra("UserConnect"));

            Map<String, Object> data = gameplayVM.getOurField().getFieldAsMap();
            data.put("user", gameplayVM.getUserConnect().getUser().getEmail());
            data.put("GameStatus", "Processing");

            db = FirebaseFirestore.getInstance();
            db.collection("collections")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        gameplayVM.setDocumentId(documentReference.getId());
                    }
                });
        }

        initButtons();
    }

    private void initButtons() {
        shipTypes = (Spinner) findViewById(R.id.ShipTypesSpinner);
        pageTitle = (TextView) findViewById(R.id.PageTitleTextView);
        rotateShip = (Button) findViewById(R.id.RotateShipButton);
        initSpinner();

        TableLayout table = (TableLayout) findViewById(R.id.table);
        TableRow row;
        int rowNum = table.getChildCount(), columnNum;
        Button button;
        for (int i = 1; i < rowNum - 1; i++) {
            row = (TableRow) table.getChildAt(i);
            columnNum = row.getChildCount();
            for (int j = 0; j < columnNum; j++) {
                button = (Button) row.getChildAt(j);
                setBtnListener(button, i - 1, j);
            }
        }
    }

    private void initSpinner() {
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, gameplayVM.getOurField().getShipTypes());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shipTypes.setAdapter(adapter);
    }

    private void setBtnListener(Button button, final int i, final int j) {
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if (gameplayVM.getGameStatus() == GameStatus.create) {
                    gameplayVM.setSelectedShipType(initShipType(shipTypes.getSelectedItem().toString()));

                    if (gameplayVM.getOurField().setShip(i, j, gameplayVM.getSelectedShipType(),
                            gameplayVM.isHorizontalOrientation())) {
                        updateField(gameplayVM.getOurField(), true);

                        if (gameplayVM.getOurField().isReady()) {
                            pageTitle.setText(R.string.waiting_enemy);
                            gameplayVM.setGameStatus(GameStatus.ready);

                            Map<String, Object> data = gameplayVM.getOurField().getFieldAsMap();
                            data.put("GameStatus", "Ready");
                            db.collection("collections").document(
                                    gameplayVM.getDocumentId()).update(data);

                            Map<String, Object> recipient = new HashMap<>();
                            recipient.put(gameplayVM.getUserConnect().getUser().getStatus(), "Ready");
                            db.collection("connections").document(
                                    gameplayVM.getUserConnect().getConnectionId()).update(recipient);

                            final DocumentReference docRef = db.collection("connections")
                                    .document(gameplayVM.getUserConnect().getConnectionId());

                            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                    @Nullable FirebaseFirestoreException e) {
                                    if (snapshot == null || !snapshot.exists()) {
                                        return;
                                    }

                                    Map<String, Object> result = snapshot.getData();
                                    if (!Objects.requireNonNull(result).isEmpty()
                                            && !Objects.equals(result.get("recipient"), "")
                                            && result.size() == 4) {
                                        if (gameplayVM.getUserConnect().getUser().getStatus().equals("Creator")) {
                                            loadEnemyField(Objects.requireNonNull(result.get("recipient")).toString());
                                        } else {
                                            loadEnemyField(Objects.requireNonNull(result.get("sender")).toString());
                                        }
                                    }
                                }
                            });
                            changeActivityState();
                        }
                    }
                } else if (gameplayVM.getGameStatus() == GameStatus.ready) {
                    int attackResult = gameplayVM.getEnemyField().EnemyAttack(i, j);
                    if (attackResult == 1) {
                        view.setBackground(getResources().getDrawable(R.drawable.ic_baseline_close));
                    } else if (attackResult == 0) {
                        pageTitle.setText(R.string.enemy_turn);
                        view.setBackground(getResources().getDrawable(R.drawable.ic_baseline_adjust));

                        db.collection("collections")
                                .document(gameplayVM.getEnemy().getId())
                                .update(gameplayVM.getEnemyField().getFieldAsMap());
                        updateField(gameplayVM.getOurField(), true);
                        gameplayVM.setGameStatus(GameStatus.wait);
                    }

                    if (gameplayVM.getEnemyField().isAllShipsKilled()) {
                        gameplayVM.setGameStatus(GameStatus.close);

                        stopGame("You won!");
                        saveScore(gameplayVM.getUserConnect().getUser().getEmail(),
                                gameplayVM.getEnemy().getEmail());

                        Map<String, Object> recipient = new HashMap<>();
                        recipient.put("GameStatus", "Close");
                        db.collection("collections")
                                .document(gameplayVM.getEnemy().getId())
                                .update(recipient);
                    }
                }
            }
        });
    }

    private ShipType initShipType(String spinnerItem) {
        switch (spinnerItem) {
            case "Single-deck":
                return ShipType.singleDecker;
            case "Double-deck":
                return ShipType.doubleDecker;
            case "Three-decker":
                return ShipType.threeDecker;
            case "Four-decker":
                return ShipType.fourDecker;
        }

        return ShipType.singleDecker;
    }

    private void stopGame(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        pageTitle.setText(message);
        updateField(gameplayVM.getEnemyField(), true);
    }

    private void saveScore(String winner, String looser) {
        Map<String, Object> score = new HashMap<>();
        score.put("winner", winner);
        score.put("looser", looser);
        db.collection("scores").add(score);
    }

    private void loadEnemyField(final String enemy) {
        gameplayVM.getEnemy().setEmail(enemy);
        db.collection("collections")
            .whereEqualTo("user", enemy)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (!task.isSuccessful()) {
                        return;
                    }

                    String reference;
                    Map<String, Object> result;
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        reference = document.getId();
                        gameplayVM.getEnemy().setId(reference);

                        result = document.getData();
                        gameplayVM.getEnemyField().setFieldFromMap(result);
                    }

                    if (gameplayVM.getUserConnect().getUser().getStatus().equals("Creator")) {
                        pageTitle.setText(R.string.our_turn);
                        updateField(gameplayVM.getEnemyField(), false);
                        gameplayVM.setGameStatus(GameStatus.ready);
                    } else {
                        pageTitle.setText(R.string.enemy_turn);
                        gameplayVM.setGameStatus(GameStatus.wait);
                    }

                    db.collection("collections")
                        .whereEqualTo("user", gameplayVM.getUserConnect().getUser().getEmail())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (!task.isSuccessful()) {
                                    return;
                                }

                                String reference2 = "";
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                    reference2 = document.getId();
                                }

                                final DocumentReference docRef = db.collection("collections")
                                        .document(reference2);
                                final String reference3 = reference2;
                                docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                        @Nullable FirebaseFirestoreException e) {
                                        if (snapshot == null || !snapshot.exists()) {
                                            return;
                                        }

                                        Map<String, Object> result = snapshot.getData();
                                        if (!Objects.equals(Objects.requireNonNull(result).get("GameStatus"), "Close")) {
                                            if (gameplayVM.isGameStart()) {
                                                if (pageTitle.getText().toString().equals("Your turn")) {
                                                    pageTitle.setText(R.string.enemy_turn);
                                                    gameplayVM.setGameStatus(GameStatus.wait);
                                                } else {
                                                    pageTitle.setText(R.string.our_turn);
                                                    updateField(gameplayVM.getEnemyField(), false);
                                                    gameplayVM.setGameStatus(GameStatus.ready);
                                                }
                                            }
                                            updateOurField(reference3);
                                        } else {
                                            gameplayVM.setGameStatus(GameStatus.close);
                                            stopGame("You lost");
                                        }
                                        gameplayVM.setGameStart(true);
                                    }
                                });
                            }
                        });
                }
            });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void updateField(BattleFieldLogic field, boolean mode) {
        TableLayout table = (TableLayout) findViewById(R.id.table);
        TableRow row;
        Button button;
        int rowNum = table.getChildCount(), columnNum, primaryResult;

        for (int i = 1; i < rowNum - 1; i++) {
            row = (TableRow) table.getChildAt(i);
            columnNum = row.getChildCount();

            for (int j = 0; j < columnNum; j++) {
                button = (Button) row.getChildAt(j);
                primaryResult = field.isAttacked(i - 1, j);
                if (mode) {
                    if (primaryResult == 3) {
                        button.setBackgroundColor(Color.RED);
                    } else if (primaryResult == 2) {
                        button.setBackground(getResources().getDrawable(R.drawable.ic_baseline_adjust));
                    } else if (primaryResult == 1) {
                        button.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    } else {
                        button.setBackgroundColor(Color.TRANSPARENT);
                    }
                } else {
                    if (primaryResult == 3) {
                        button.setBackground(getResources().getDrawable(R.drawable.ic_baseline_close));
                    } else if (primaryResult == 2) {
                        button.setBackground(getResources().getDrawable(R.drawable.ic_baseline_adjust));
                    } else {
                        button.setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        }
    }

    private void updateOurField(String reference) {
        db.collection("collections")
                .document(reference)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (Objects.requireNonNull(document).exists()) {
                                Map<String, Object> map = document.getData();
                                gameplayVM.getOurField().setFieldFromMap(map);
                            }
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ConnectActivity.class);
        intent.putExtra("UserConnect", gameplayVM.getUserConnect());
        setResult(RESULT_OK, intent);

        super.onBackPressed();
    }

    public void rotateClick(View view) {
        gameplayVM.setHorizontalOrientation(!gameplayVM.isHorizontalOrientation());

        if (gameplayVM.isHorizontalOrientation()) {
            rotateShip.setText(getResources().getString(R.string.horizontal));
        } else {
            rotateShip.setText(getResources().getString(R.string.vertical));
        }
    }

    public void changeActivityState() {
        pageTitle.setText(R.string.ready_state);
        rotateShip.setVisibility(View.INVISIBLE);
        shipTypes.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        if (!gameplayVM.getDocumentId().equals("")) {
            db.collection("collections")
                    .document(gameplayVM.getDocumentId())
                    .delete();
            db.collection("connections")
                    .document(gameplayVM.getConnectionId())
                    .delete();
        }

        super.onDestroy();
    }
}