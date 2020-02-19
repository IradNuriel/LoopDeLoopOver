package com.example.irad9731.loopdeloopover;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {


    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    public ProgressBar mProgress;
    private FirebaseAuth mAuth;
    private static final String TAG="LoopDeLoopOver";
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mNameField;
    private ImageView imgPhoto;
    private static final int PICK_IMAGE_REQUEST = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mEmailField = findViewById(R.id.field_email);
        mPasswordField = findViewById(R.id.field_password);
        mProgress = findViewById(R.id.progressBar);
        mNameField = findViewById(R.id.playerName);
        imgPhoto = findViewById(R.id.playerImg);
    }


    private void signIn(String email, String password) {

        showProgress();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            //moveToBoard(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        hideProgress();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }


    public void pick() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    private void uploadImageIntoFB(final FirebaseUser user){

       /* imgPhoto.setDrawingCacheEnabled(true);
        imgPhoto.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(imgPhoto.getDrawingCache());*/

        Bitmap bitmap = (Bitmap) imgPhoto.getTag();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        FirebaseStorage storage = FirebaseStorage.getInstance();

        final StorageReference storageRef = storage.getReference().child("photos").child(user.getUid()+".jpeg");


        UploadTask uploadTask = storageRef.putBytes(data);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {  if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    String name = user.getEmail().substring(0,user.getEmail().indexOf('@'));
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .setPhotoUri(downloadUri)
                            .build();
                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            addNewPlayer(user);
                            hideProgress();
                        }
                    });
                } else {
                    // Handle failures
                    // ...
                    String s="";
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode){
            case PICK_IMAGE_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        imgPhoto.setImageBitmap(bitmap);
                        imgPhoto.setTag(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }


    private void addNewPlayer(final FirebaseUser user){
        if(mNameField.getText().equals("")){

        }
        Player p = new Player(mNameField.getText().toString(),user.getUid(),Long.MAX_VALUE,Long.MAX_VALUE,Long.MAX_VALUE,user.getPhotoUrl().toString());
        database.getReference().child("players").child(user.getUid()).setValue(p).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("nit",e.getMessage());
            }
        });


    }


    public void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mProgress.setVisibility(View.INVISIBLE);

    }
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void createAccount(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            uploadImageIntoFB(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    private void moveToMenu(final FirebaseUser currentUser){
        database.getReference().child("players").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                Map m = (Map)dataSnapshot.getValue();
                String name = (String) m.get("name");
                i.putExtra("name", name);
                startActivity(i);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void signOut() {
        mAuth.signOut();
        updateUI(mAuth.getCurrentUser());
    }
    private void updateUI(FirebaseUser currentUser){
        if (currentUser!= null){

            mEmailField.setText(currentUser.getEmail());
            mPasswordField.setText("********");
            //getImageFromFB(currentUser);
            findViewById(R.id.btnSigninAccount).setEnabled(false);
            findViewById(R.id.btnCreateAccount).setEnabled(false);
            findViewById(R.id.btnGo).setEnabled(true);
            findViewById(R.id.btnSignOut).setEnabled(true);
        }
        else{
            findViewById(R.id.btnSigninAccount).setEnabled(true);
            findViewById(R.id.btnCreateAccount).setEnabled(true);
            findViewById(R.id.btnSignOut).setEnabled(false);
            findViewById(R.id.btnGo).setEnabled(false);
        }
    }


    public void onClick(View v){
        int i = v.getId();
        switch(i){
            case R.id.btnCreateAccount:
                if(mNameField.getText().toString().equals("")||mEmailField.getText().toString().equals("")||mPasswordField.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "please enter all attributes before signing up",
                            Toast.LENGTH_SHORT).show();

                }else {
                    createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
                }
                break;
            case R.id.btnSigninAccount:
                if(mPasswordField.getText().toString().equals("")||mEmailField.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "please enter all attributes before signing in",
                            Toast.LENGTH_SHORT).show();
                }else {
                    signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
                }
                break;
            case R.id.btnSignOut:
                signOut();
                break;
            case R.id.gallery:
                pick();
                break;
            case R.id.btnGo:
                if(mAuth.getCurrentUser()==null){
                    Toast.makeText(LoginActivity.this, "please sign in/sign up before you continue",
                            Toast.LENGTH_SHORT).show();

                }else {
                    moveToMenu(mAuth.getCurrentUser());
                }
                break;
        }
    }

}
