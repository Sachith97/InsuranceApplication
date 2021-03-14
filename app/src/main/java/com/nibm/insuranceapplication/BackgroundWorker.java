package com.nibm.insuranceapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundWorker extends AsyncTask<String,Void,String> {

    Context context;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    Timer timer;
    String type;

    BackgroundWorker(Context ctx){
        context = ctx;
    }

    @Override
    protected String doInBackground(String... strings) {
        type = strings[0];
        String login_url = "https://metlinsuranceapp.000webhostapp.com/LoginAndroid.php";
        if(type.equals("login")){
            try {
                String username = strings[1];
                String password = strings[2];

                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("type","UTF-8")+"="+URLEncoder.encode(type,"UTF-8")+"&"
                        +URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String result="";
                String line="";

                while((line = bufferedReader.readLine())!=null){
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("insert")){
            try {
                String id = strings[1];
                String name = strings[2];
                String email = strings[3];
                String username = strings[4];
                String password = strings[5];
                String imageData = strings[6];

                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("type","UTF-8")+"="+URLEncoder.encode(type,"UTF-8")+"&"
                        +URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8")+"&"
                        +URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"
                        +URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"
                        +URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"
                        +URLEncoder.encode("imageData","UTF-8")+"="+URLEncoder.encode(imageData,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String result="";
                String line="";

                while((line = bufferedReader.readLine())!=null){
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals("retrieve")) {

        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        builder = new AlertDialog.Builder(context);

        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.logging_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    protected void onPostExecute(String result) {
        progressDialog.dismiss();
        if (result.equals("login unsuccess")) {
            builder.setPositiveButton("Ok",null);
            builder.setTitle("Login Status");
            builder.setMessage("Invalid Username or Password!");
            builder.show();
        }
        else if (result.equals("insert success")) {
            builder.setTitle("Insert Status");
            builder.setMessage("Account Creation Successful!!");
            builder.show();

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
            }, 3000);
        }
        else if (result.equals("insert unsuccess")) {
            builder.setTitle("Insert Status");
            builder.setMessage("Account Creation Unsuccessful!!");
            builder.show();
        }
        else if (result.equals("invalid")) {
            builder.setPositiveButton("Ok",null);
            builder.setTitle("Account Details");
            builder.setMessage("You have not registered with entered email and Insurance ID. Contact us!");
            builder.show();
        }
        else if (result.equals("user available")) {
            builder.setPositiveButton("Ok",null);
            builder.setTitle("User Status");
            builder.setMessage("You have already created an account. Go to login!");
            builder.show();
        }
        else {
            Intent intent = new Intent(context, HomeActivity.class);
            intent.putExtra("userId", result);
            context.startActivity(intent);
        }
    }
}
