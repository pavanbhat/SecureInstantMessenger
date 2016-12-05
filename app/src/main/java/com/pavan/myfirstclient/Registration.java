package com.pavan.myfirstclient;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registration extends AppCompatActivity {

    // Button to register a user
    private Button register;
    // Placeholder for a registered username
    private EditText registeredName;

    private EditText registeredPass;

    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        register = (Button)findViewById(R.id.register);
        registeredName = (EditText)findViewById(R.id.registeredName);
        login = (Button)findViewById(R.id.login);

        // Registration of a new user
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address;
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
                    address = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
                    Toast.makeText(Registration.this, address, Toast.LENGTH_LONG).show();


                    Intent in = new Intent(Registration.this, AddUser.class);
                    in.putExtra("ip", address);
                    in.putExtra("registeredName", registeredName.getText());
                    startActivity(in);
                }
            }
        });

        // Login of an old user
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
