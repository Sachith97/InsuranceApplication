package com.nibm.insuranceapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InquiryActivity extends AppCompatActivity {

    TextView txtUserName, txtUserEmail;
    TextView btnCheck;
    EditText txtLocation, txtSituation, txtContactNumber;
    ImageView imgUserIcon;
    String userName, userEmail, userMobile, selectedPlan;
    byte[] byteArray;
    Spinner spnPlan;
    Button btnSubmit;

    String[] insurancePlans = {"Motor Insurance"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        getSupportActionBar().hide();

        txtUserName = findViewById(R.id.txtIUserName);
        txtUserEmail = findViewById(R.id.txtIUserEmail);
        imgUserIcon = findViewById(R.id.imgIUserIcon);

        Bundle extras = getIntent().getExtras();
        userName = extras.getString("userName");
        userEmail = extras.getString("userEmail");
        userMobile = extras.getString("userMobile");
        byteArray = extras.getByteArray("userIcon");

        txtUserName.setText(userName);
        txtUserEmail.setText(userEmail);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        imgUserIcon.setImageBitmap(bitmap);

        txtLocation = findViewById(R.id.txtLocation);
        txtSituation = findViewById(R.id.txtDes);
        txtContactNumber = findViewById(R.id.txtEmMobile);
        btnCheck = findViewById(R.id.btnCheck);
        btnSubmit = findViewById(R.id.btnSubmit);
        spnPlan = findViewById(R.id.spnType);
        ArrayAdapter<String> adapter =new ArrayAdapter<String> (this, android.R.layout.simple_spinner_dropdown_item, insurancePlans);
        spnPlan.setAdapter(adapter);

        selectedPlan = spnPlan.getSelectedItem().toString();
        txtContactNumber.setText(userMobile);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtLocation.getText().toString().equals("") || txtSituation.getText().toString().equals("") || txtContactNumber.getText().toString().equals("")) {
                    Toast.makeText(InquiryActivity.this, "You have to fill all the details!", Toast.LENGTH_SHORT).show();
                } else {
                    sendMail(selectedPlan, txtLocation.getText().toString(), txtSituation.getText().toString(), txtContactNumber.getText().toString());
                }
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InquiryActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    void sendMail(String plan, String location, String situation, String contactNumber) {
        String to = "cohdse201f-020@student.nibm.lk";
        String subject = "New Inquiry";
        String message = "Insurance Plan : " + plan + "\n" + "Location : " + location + "\n" + "Situation : " + situation + "\n" + "Contact Number : " + contactNumber;

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[] {to});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, message);
        email.setType("message/rfc822");

        try {
            startActivity(Intent.createChooser(email, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(InquiryActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

}