package com.nibm.insuranceapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ApplyActivity extends AppCompatActivity {

    Spinner spnPlan;
    EditText txtName, txtEmail, txtMobile;
    Button btnApply;

    String[] insurancePlans = {"-select-", "Travel Insurance", "Health Insurance", "Motor Insurance"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        getSupportActionBar().hide();

        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtMobile = findViewById(R.id.txtContactNo);
        btnApply = findViewById(R.id.btnApply);
        spnPlan = findViewById(R.id.spnPlan);
        ArrayAdapter<String> adapter =new ArrayAdapter<String> (this, android.R.layout.simple_spinner_dropdown_item, insurancePlans);
        spnPlan.setAdapter(adapter);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spnPlan.getSelectedItemPosition() == 0 || txtName.getText().toString().equals("") ||
                        txtEmail.getText().toString().equals("") || txtMobile.getText().toString().equals("")) {
                    Toast.makeText(ApplyActivity.this, "You have to fill all the details!", Toast.LENGTH_SHORT).show();
                } else {
                    sendMail(spnPlan.getSelectedItem().toString(), txtName.getText().toString(), txtEmail.getText().toString(), txtMobile.getText().toString());
                }
            }
        });
    }

    void sendMail(String plan, String name, String email, String contactNumber) {
        String to = "cohdse201f-054@student.nibm.lk";
        String subject = "New Application";
        String message = "Insurance Plan : " + plan + "\n" + "Name : " + name + "\n" + "Email : " + email + "\n" + "Contact Number : " + contactNumber;

        Intent emailData = new Intent(Intent.ACTION_SEND);
        emailData.putExtra(Intent.EXTRA_EMAIL, new String[] {to});
        emailData.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailData.putExtra(Intent.EXTRA_TEXT, message);
        emailData.setType("message/rfc822");

        try {
            startActivity(Intent.createChooser(emailData, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ApplyActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}