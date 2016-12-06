package com.pavan.myfirstclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class Client extends AppCompatActivity implements Serializable {

    private static final long serialVersionId = 1L;

    private Socket client;
    private PrintWriter printwriter;
    private EditText textField;
    private Button button;
    public static TextView t;
    private String msg;
    public static String myUsername;
    public static String name;
    static StringBuilder chat;
//    public File chat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textField = (EditText) findViewById(R.id.chat); //reference to the text field
        button = (Button) findViewById(R.id.send);   //reference to the send button
        t = (TextView) findViewById(R.id.chat_window);
        t.setMovementMethod(new ScrollingMovementMethod());
        chat = new StringBuilder();


        Intent out = getIntent();
        myUsername = out.getExtras().getString("regName");
        name = out.getExtras().getString("username");

        //Appending the chat
        chat.append(PreferenceManager.getDefaultSharedPreferences(this).getString(name, ""));

        t.setText(chat);


        //Button press event listener
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                msg = textField.getText().toString(); //get the text message on the text field
                textField.setText("");      //Reset the text field to blank
                chat.append(" \n " + myUsername + ": " + msg);
                Client.t.setText("");
                Client.t.setText(chat.toString());

                try {
                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                    if (SDK_INT > 8) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        //your codes here
                        client = new Socket("129.21.139.214", 10210);  //connect to server

                        printwriter = new PrintWriter(client.getOutputStream(), true);
                        msg = createJSONMessage(name, myUsername, msg);
                        printwriter.write(msg);  //write the message to output stream

                        printwriter.flush();
                        printwriter.close();
                        client.close();   //closing the connection
                    }

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        new MyTask().execute(chat.toString());

    }

    public void onBackPressed() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(name, chat.toString()).commit();
        super.onBackPressed();
    }


    public static String createJSONMessage(String to, String from, String body) {
        String message = "";
        JSONObject root = new JSONObject();
        JSONObject msg = new JSONObject();

        try {
            msg.put("to", to);
            msg.put("from", from);
            msg.put("body", body);
            root.put("message", msg);
            root.put("type", "message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        message = root.toString();

        return message;
    }

    class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            ServerSocket sock;

            try {

                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    sock = new ServerSocket(10210);
                    while (true) {
                        Socket incoming = sock.accept();
                        BufferedReader br = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
                        String result;
                        while ((result = br.readLine()) != null) {
                            String[] temp = new String[2];
                            temp[0] = strings[0];
                            temp[1] = "\n" + name + " Says: " + result;
                            publishProgress(temp);
                        }
                        br.close();
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            chat.append(values[1]);
            Client.t.setText(chat.toString());
            super.onProgressUpdate(values);
        }
    }


}


