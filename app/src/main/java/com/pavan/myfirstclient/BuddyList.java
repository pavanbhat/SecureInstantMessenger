package com.pavan.myfirstclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.widget.AdapterView.*;

public class BuddyList extends AppCompatActivity {
    String currentUser = "";
    ListView contact_list;
    String buddyAdded, regName;
    FloatingActionButton floatingActionButton;
    private ArrayList<String> buddies = new ArrayList<>();
    public class  MyArrayAdapter <T> extends ArrayAdapter<String> {
        Context c;
        ArrayList<String> buddies;
        int rid;

        MyArrayAdapter(Context c, int rid, ArrayList<String> buddies){
            super(c,rid, buddies);
            this.c = c;
            this.rid = rid;
            this.buddies = buddies;
        }
        public View getView(int pos, View v, ViewGroup grp){
            LayoutInflater lf = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout l = (LinearLayout)lf.inflate(R.layout.buddy,null);
            TextView t = (TextView)l.findViewById(R.id.textView);
            t.setText(buddies.get(pos));
            ImageView im = (ImageView)l.findViewById(R.id.imageView);
            im.setImageResource(R.mipmap.ic_launcher);
            return l;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddy_list);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);
        contact_list = (ListView)findViewById(R.id.contact_list);
        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString("friends", null);
        Type t = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> friendsList = gson.fromJson(json, t);
        for (String friend : friendsList){
            buddies.add(friend);
        }*/

        Intent out = getIntent();
        regName = out.getExtras().get("regName").toString();
        buddyAdded = out.getExtras().get("buddy").toString();
        buddies.add(buddyAdded);

        contact_list.setAdapter(new MyArrayAdapter(this,android.R.layout.simple_list_item_1, buddies));



        contact_list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentUser = (String)adapterView.getItemAtPosition(i);

                Intent s = new Intent(view.getContext(), Client.class);
                s.putExtra("regName", regName);
                s.putExtra("username", currentUser);
                startActivityForResult(s, 102);
//                }
            }
        });

        floatingActionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BuddyList.this, AddUser.class));
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
