package com.nibm.insuranceapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class UserAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        getSupportActionBar().hide();
    }
}