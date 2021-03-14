package com.nibm.insuranceapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    TextView btnNew, btnApply;
    EditText txtUsername, txtPassword;
    String username, password, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        // Transparent Navigation Bar
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        txtUsername = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);
        txtUsername.requestFocus();

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    loginHandle(v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnNew = findViewById(R.id.btnNew);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        btnApply = findViewById(R.id.btnApply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ApplyActivity.class);
                startActivity(intent);
            }
        });
    }

    public void loginHandle(View view) throws Exception {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            if(txtUsername.getText().toString().equals("") || txtPassword.getText().toString().equals("")){
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setPositiveButton("Ok", null);
                builder.setTitle("Invalid Credentials");
                builder.setMessage("You have to input both username and password!");
                builder.show();
            } else {
                AESCrypt aesCrypt = new AESCrypt();
                //password = aesCrypt.encrypt(txtPassword.getText().toString());
                password = txtPassword.getText().toString();
                username = txtUsername.getText().toString();
                type = "login";

                // Send username and password to the background activity
                BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                backgroundWorker.execute(type, username, password);
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setPositiveButton("Ok", null);
            builder.setTitle("Connection Status");
            builder.setMessage("Connection is not available! Connect your device to an internet connection!");
            builder.show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    void exitByBackKey(){
        androidx.appcompat.app.AlertDialog.Builder altdial = new androidx.appcompat.app.AlertDialog.Builder(LoginActivity.this);
        altdial.setMessage("Do you want to exit?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        androidx.appcompat.app.AlertDialog alertDialog = altdial.create();
        alertDialog.show();
    }
}