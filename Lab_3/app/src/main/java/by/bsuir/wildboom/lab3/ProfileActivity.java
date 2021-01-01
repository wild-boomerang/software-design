package by.bsuir.wildboom.lab3;

import android.app.ProgressDialog;
import android.view.View;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.timgroup.jgravatar.Gravatar;
import com.timgroup.jgravatar.GravatarRating;
import com.timgroup.jgravatar.GravatarDefaultImage;

import java.util.Objects;
import java.util.ArrayList;
import java.io.IOException;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private UserConnect userConnect;
    private Uri filePath;
    private ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private StorageReference storageReference;

    private EditText nickname;
    private ListView scores;
    private ImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userConnect = (UserConnect)getIntent().getSerializableExtra("UserConnect");

        nickname = (EditText) findViewById(R.id.NicknameEditText);
        scores = (ListView) findViewById(R.id.ResultsListView);
        userImage = (ImageView) findViewById(R.id.UserImageView);

        if (userConnect.getUser().getNickname() != null) {
            nickname.setText(userConnect.getUser().getNickname());
        }

        final Context context = this;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();
        db.collection("scores")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        Map<String, Object> result;
                        HashMap<String, String> map;
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            result = document.getData();
                            map = new HashMap<>();
                            if (Objects.requireNonNull(result.get("winner")).toString()
                                    .equals(userConnect.getUser().getEmail())) {
                                map.put("email", "Enemy: " + Objects.requireNonNull(
                                        result.get("looser")).toString());
                                map.put("result", "Result: Won");
                            } else if (Objects.requireNonNull(result.get("looser")).toString()
                                    .equals(userConnect.getUser().getEmail())) {
                                map.put("email", "Enemy: " + Objects.requireNonNull(
                                        result.get("winner")).toString());
                                map.put("result", "Result: Lost");
                            }
                            arrayList.add(map);
                        }
                    }

                    SimpleAdapter simpleAdapter = new SimpleAdapter(
                            context,
                            arrayList,
                            android.R.layout.simple_list_item_2,
                            new String[] { "email", "result" },
                            new int[] { android.R.id.text1, android.R.id.text2 });
                    scores.setAdapter(simpleAdapter);
                }
            });

        if (userConnect.getUser().getImageUrl() != null) {
            StorageReference photoReference = storageReference.child(userConnect.getUser().getImageUrl());

            photoReference.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    userImage.setImageBitmap(bmp);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplicationContext(), "There is no such file or path found!", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Gravatar gravatar = new Gravatar();
            gravatar = gravatar.setDefaultImage(GravatarDefaultImage.IDENTICON);
            gravatar = gravatar.setRating(GravatarRating.GENERAL_AUDIENCES);
            gravatar = gravatar.setSize(100);
            String url = gravatar.getUrl(userConnect.getUser().getEmail());
            try {
                Picasso.with(context).load(url).into(userImage);
            } catch (Exception e) {
                Toast.makeText(
                        getApplicationContext(),
                        "There is no such gravatar or image doesn't exist!",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void saveNickname(View view) {
        userConnect.getUser().setNickname(nickname.getText().toString());
        uploadImage();

        db.collection("users")
            .whereEqualTo("email", userConnect.getUser().getEmail())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
               @Override
               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                   if (!task.isSuccessful()) {
                       return;
                   }

                   String reference = "";
                   for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                       reference = document.getId();
                   }

                   Map<String, Object> nickname = new HashMap<>();
                   nickname.put("nickname", userConnect.getUser().getNickname());
                   if (userConnect.getUser().getImageUrl() != null) {
                       nickname.put("imageUrl", userConnect.getUser().getImageUrl());
                   }
                   db.collection("users").document(reference).update(nickname);
               }
           });
    }

    public void chooseImage(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Please, select image from here"), 1);
    }

    private void uploadImage() {
        if (filePath == null) {
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        userConnect.getUser().setImageUrl("images/" + UUID.randomUUID().toString());
        StorageReference storageReference = this.storageReference.child(userConnect.getUser().getImageUrl());
        storageReference.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "Image successfully uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot)
                    {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this,"Failed! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                userImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ConnectActivity.class);
        intent.putExtra("UserConnect", userConnect);
        setResult(RESULT_OK, intent);

        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("key", nickname.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        nickname.setText(savedInstanceState.getString("key"));
    }
}
