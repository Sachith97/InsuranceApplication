package com.nibm.insuranceapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {

    ImageView imgUserIcon;
    String userId, userMobile;
    AlertDialog.Builder builder;
    TextView btnInquiry, btnSignOut, btnDashboard, btnAccDetails;
    TextView txtUserName, txtUserEmail;
    Timer timer;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();
        // Transparent Navigation Bar
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        imgUserIcon = findViewById(R.id.imgUserIcon);
        txtUserName = findViewById(R.id.txtUserName);
        txtUserEmail = findViewById(R.id.txtUserEmail);

        btnInquiry = findViewById(R.id.btnInquiry);
        btnAccDetails = findViewById(R.id.btnAccDetails);
        btnDashboard = findViewById(R.id.btnDashboard);
        btnSignOut = findViewById(R.id.btnSignOut);

        Bundle extras = getIntent().getExtras();
        userId = extras.getString("userId");
        getUserData(userId);

        btnInquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) imgUserIcon.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Intent intent = new Intent(HomeActivity.this, InquiryActivity.class);
                intent.putExtra("userName", txtUserName.getText().toString());
                intent.putExtra("userEmail", txtUserEmail.getText().toString());
                intent.putExtra("userMobile", userMobile);
                intent.putExtra("userIcon", byteArray);
                startActivity(intent);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder altdial = new androidx.appcompat.app.AlertDialog.Builder(HomeActivity.this);
                altdial.setMessage("Do you want to sign out?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog = new ProgressDialog(HomeActivity.this);
                                progressDialog.show();
                                progressDialog.setContentView(R.layout.logout_dialog);
                                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                                timer = new Timer();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        finish();
                                        System.exit(0);
                                    }
                                }, 3000);
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
        });

        btnAccDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, UserAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    void getUserData(String id){
        RequestQueue queue = Volley.newRequestQueue(this);

        String dataURL = "https://metlinsuranceapp.000webhostapp.com/DataRetrieve.php?id=" + id;

        StringRequest userDataRequest = new StringRequest(Request.Method.GET, dataURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject userDataRoot = new JSONObject(response);
                    JSONArray dataList = userDataRoot.getJSONArray("userdata");

                    txtUserName.setText(dataList.getJSONObject(0).getString("name"));
                    txtUserEmail.setText(dataList.getJSONObject(0).getString("email"));
                    userMobile = dataList.getJSONObject(0).getString("mobile");

                    String url = dataList.getJSONObject(0).getString("imageurl");
                    imgUserIcon.setTag(url);
                    new DownLoadImageTask(imgUserIcon).execute(url);

                }catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(userDataRequest);
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
        androidx.appcompat.app.AlertDialog.Builder altdial = new androidx.appcompat.app.AlertDialog.Builder(HomeActivity.this);
        altdial.setMessage("Do you want to sign out?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog = new ProgressDialog(HomeActivity.this);
                        progressDialog.show();
                        progressDialog.setContentView(R.layout.logout_dialog);
                        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 3000);
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