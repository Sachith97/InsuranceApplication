package com.nibm.insuranceapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class SignInActivity extends AppCompatActivity {

    EditText txtId, txtFirstName, txtSurname, txtEmail, txtUserName, txtPassword, txtRePassword;
    Button btnCreate;
    String type, id, name, email, username, password, imageData;
    ImageView imgCameraView;
    ImageButton btnBrowse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportActionBar().hide();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(new String[] {
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

        txtId = findViewById(R.id.txtId);
        txtFirstName = findViewById(R.id.txtFirstName);
        txtSurname = findViewById(R.id.txtSurname);
        txtEmail = findViewById(R.id.txtEmailReg);
        txtUserName = findViewById(R.id.txtCreateUserName);
        txtPassword = findViewById(R.id.txtNewPassword);
        txtRePassword = findViewById(R.id.txtRePassword);

        btnCreate = findViewById(R.id.btnCreate);
        btnBrowse = findViewById(R.id.btnBrowse);

        imgCameraView = findViewById(R.id.imgScreen);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtPassword.getText().toString().equals(txtRePassword.getText().toString()) ) {

                    if(txtId.getText().toString().equals("") || txtFirstName.getText().toString().equals("") ||
                            txtSurname.getText().toString().equals("") || txtEmail.getText().toString().equals("") ||
                            txtUserName.getText().toString().equals("") || txtPassword.getText().toString().equals("") ||
                            txtRePassword.getText().toString().equals("")) {

                        Toast.makeText(SignInActivity.this, "You have to fill all the details!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        type = "insert";
                        id = txtId.getText().toString();
                        name = txtFirstName.getText().toString() + " " + txtSurname.getText().toString();
                        email = txtEmail.getText().toString();
                        username = txtUserName.getText().toString();
                        password = txtPassword.getText().toString();

                        BitmapDrawable drawable = (BitmapDrawable) imgCameraView.getDrawable();
                        Bitmap userImage = drawable.getBitmap();
                        Bitmap compressedUserImage = Bitmap.createScaledBitmap(userImage, 200, 200, true);
                        imageData = toBase64(compressedUserImage);

                        // Password Encryption
                        /*AESCrypt aesCrypt = new AESCrypt();
                        try {
                            password = aesCrypt.encrypt(txtPassword.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/

                        // Send strings to database to the background activity
                        BackgroundWorker backgroundWorker = new BackgroundWorker(SignInActivity.this);
                        backgroundWorker.execute(type, id, name, email, username, password, imageData);
                    }

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                    builder.setPositiveButton("Ok", null);
                    builder.setTitle("Password Mismatch");
                    builder.setMessage("Your password and re-entered password does not match!");
                    builder.show();
                }
            }
        });

        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(SignInActivity.this);
            }
        });
    }

    public void handleRegister() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            startCrop(imageUri);
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK) {
                imgCameraView.setImageURI(result.getUri());
            }
        }
    }

    private void startCrop(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setAspectRatio(1,1)
                .start(this);
    }

    public String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}