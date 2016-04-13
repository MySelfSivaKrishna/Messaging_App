package com.parse.starter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class AlbumDetails extends AppCompatActivity {
TextView name,privacy;
    ListView users;

    ArrayList<String> usersnames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.albumdetails);
        name= (TextView) findViewById(R.id.nameInDetails);
        privacy= (TextView) findViewById(R.id.privacyInDetails);
        users= (ListView) findViewById(R.id.listViewInDetails);

        String sname=getIntent().getExtras().getString("name");
        boolean sprivacy=getIntent().getExtras().getBoolean("privacy");
        usersnames=getIntent().getExtras().getStringArrayList("usersnames");
        name.setText(sname);
        if (sprivacy){
            privacy.setText("Public");
        }
        else {
            privacy.setText("Private");
        }


Log.d("demo", "usersnames=" + usersnames.size());
        ArrayAdapter<String> adapterForUserList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usersnames);
        users.setAdapter(adapterForUserList);

    }

}
