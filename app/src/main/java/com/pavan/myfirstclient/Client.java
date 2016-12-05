package com.pavan.myfirstclient;

/*
 * This is a simple Android mobile client
 * This application read any string messege typed on the text field and
 * send it to the server when the Send button is pressed

 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Client extends AppCompatActivity implements Serializable{

    private static final long serialVersionId = 1L;

    private Socket client;
    private PrintWriter printwriter;
    private EditText textField;
    private Button button;
    public TextView t;
    private String message;
    public HashMap<String, String> chatData = new HashMap<>();
    public String myUsername = "Sid: ";
    public String name;
    StringBuilder chat;
//    public File chat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textField = (EditText) findViewById(R.id.chat); //reference to the text field
        button = (Button) findViewById(R.id.send);   //reference to the send button
        t = (TextView)findViewById(R.id.chat_window);

        chat = new StringBuilder();


        Intent out = getIntent();
        name = out.getExtras().getString("username");
        //Appending the chat
        if(!PreferenceManager.getDefaultSharedPreferences(this).getString( name, "").equalsIgnoreCase("")){
            chat.append(PreferenceManager.getDefaultSharedPreferences(this).getString( name, ""));

        }
//        Toast.makeText(this, PreferenceManager.getDefaultSharedPreferences(this).getString( name, ""), Toast.LENGTH_LONG).show();

        t.setText(PreferenceManager.getDefaultSharedPreferences(this).getString( name, ""));


        //Button press event listener
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                myUsername = "\n Sid:";
                message = textField.getText().toString(); //get the text message on the text field
                textField.setText("");      //Reset the text field to blank
                t.setText(t.getText()+ " \n " + myUsername + message);
                chat.append(textField.getText()+ " \n " + myUsername + message);
                /*OutputStreamWriter outputStreamWriter = null;
                try {
                    outputStreamWriter = new OutputStreamWriter(openFileOutput(name + ".txt", Context.MODE_PRIVATE));
                    outputStreamWriter.write("\n" + t.getText().toString());
                    outputStreamWriter.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }*/


                chatData.put(name, t.getText().toString());
                t.setText(chatData.get(name));


                try {
                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                    if (SDK_INT > 8)
                    {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        //your codes here
                        client = new Socket("129.21.126.227", 10210);  //connect to server

                        printwriter = new PrintWriter(client.getOutputStream(),true);
                        message = createJSONMessage(name, "Sid" , message);
                        printwriter.write(message);  //write the message to output stream

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

        new MyTask().execute();

            }

    public void onBackPressed(){
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

    class MyTask extends AsyncTask<String, Void, String>{
        Handler handler = new Handler();
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
                            handler.post(new updateThr(result));
                        }
                        br.close();
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            return null;
        }

    }

    class updateThr implements Runnable {
        private String message;

        public updateThr(String message) {
            this.message = message;
        }

        @Override
        public void run() {

            chat.append("\n" + name + " Says: "+ this.message);

            t.setText(chat);

            /*OutputStreamWriter outputStreamWriter = null;
            try {
                t.setText(t.getText().toString()+ "\n" + name + " Says: "+ this.message);
                FileOutputStream fos = new FileOutputStream(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOCUMENTS)+"/"+name);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(chatData);
                *//*outputStreamWriter = new OutputStreamWriter(openFileOutput(name + ".txt", Context.MODE_PRIVATE));
                outputStreamWriter.write( + "\n" + name + " Says: "+ message);*//*
//                outputStreamWriter.close();
                oos.close();
                fos.close();
                Toast.makeText(Client.this, "Message received", Toast.LENGTH_LONG).show();
            } catch (Exception e) {

                e.printStackTrace();
            }*/

        }
    }


    }


