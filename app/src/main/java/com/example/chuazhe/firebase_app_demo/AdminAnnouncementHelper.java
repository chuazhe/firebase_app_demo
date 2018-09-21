package com.example.chuazhe.firebase_app_demo;


import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chuazhe.firebase_app_demo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class AdminAnnouncementHelper extends AppCompatActivity {

    Button announceBtn;
    Button cancelBtn;
    EditText commentBox;

    //Replace with your authentication key and token
    public final static String AUTH_KEY_FCM = "";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    String deviceToken = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_push_notification);

        announceBtn = (Button) findViewById(R.id.announceBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        commentBox = (EditText) findViewById(R.id.commentBox);

        StrictMode.enableDefaults();

        announceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendPushNotification(commentBox.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public String sendPushNotification(String message)
            throws IOException, JSONException {
        String result = "";
        URL url = new URL(API_URL_FCM);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
        conn.setRequestProperty("Content-Type", "application/json");

        JSONObject json = new JSONObject();

        json.put("to", deviceToken.trim());
        JSONObject info = new JSONObject();
        info.put("body", message); // Notification
        // body
        json.put("data", info);
        try {
            OutputStreamWriter wr = new OutputStreamWriter(
                    conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            System.out.println("Success");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("f");
        }
        System.out.println("GCM Notification is sent successfully");

        return result;
    }


}