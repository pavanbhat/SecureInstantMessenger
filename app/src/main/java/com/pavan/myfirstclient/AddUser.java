package com.pavan.myfirstclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.R.attr.data;

public class AddUser extends AppCompatActivity {

    private String regName;
    private String ipAdd;
    private EditText buddyName;
    private Button addBuddy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        buddyName = (EditText)findViewById(R.id.edit_buddy_name);
        addBuddy = (Button)findViewById(R.id.add);

        Intent out = getIntent();
        ipAdd = out.getExtras().get("ip").toString();
        regName = out.getExtras().get("registeredName").toString();

        addBuddy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(view.getContext(), BuddyList.class);
                in.putExtra("buddy", buddyName.getText());
                startActivityForResult(in, 102);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
