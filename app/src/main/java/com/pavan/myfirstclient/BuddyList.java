package com.pavan.myfirstclient;

import android.content.Context;
import android.content.Intent;
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

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.widget.AdapterView.*;

public class BuddyList extends AppCompatActivity {
    String currentUser = "";
    ListView contact_list;
    String path;
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
        contact_list = (ListView)findViewById(R.id.contact_list);
        contact_list.setAdapter(new MyArrayAdapter(this,android.R.layout.simple_list_item_1, buddies));

        Intent out = getIntent();
        buddies.add(out.getExtras().get("buddy").toString());

        contact_list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentUser = (String)adapterView.getItemAtPosition(i);

                /*Toast.makeText(view.getContext(), "Hello " +buddy+ " !", Toast.LENGTH_SHORT).show();
                if(i > 0){*/
                Intent s = new Intent(view.getContext(), Client.class);
                s.putExtra("username", currentUser);
                startActivityForResult(s, 101);
//                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
