package com.parse.starter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.security.acl.Acl;
import java.util.ArrayList;
import java.util.List;


public class inviteusers extends AppCompatActivity {
TextView info;
    Button invite,done;
    ArrayList<ParseUser> all=new ArrayList<>();
    ArrayList<ParseUser> selected=new ArrayList<>();
  String allNames[];
    boolean checked[];
    CharSequence temp[]=new CharSequence[5];
    AlertDialog.Builder alt;
    ListView sharedUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inviteusers);
        info= (TextView) findViewById(R.id.textToClearInInviteUsers);
        invite= (Button) findViewById(R.id.inviteinInviteUsers);
        done= (Button) findViewById(R.id.doneInInviteusers);
        sharedUsers= (ListView) findViewById(R.id.listViewInInviteUsers);
  /*      if (getIntent().getExtras().getBoolean("firsttime")){
            info.setText(R.string.privacysuggestion);
        }
        else {
            info.setText("");
        } */

        ParseQuery<ParseUser> users=ParseQuery.getQuery("_User");
        users.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                allNames = new String[objects.size()];
                for (int i = 0; i < objects.size(); i++) {
                    all.add(objects.get(i));
                    allNames[i] = all.get(i).getString("fname") + " " + all.get(i).getString("lname");
                    Log.d("demo", i + "," + allNames[i]);

                }

                checked = new boolean[all.size()];
                for (int i = 0; i < all.size(); i++) {
                    checked[i] = false;
                }

                alt = new AlertDialog.Builder(inviteusers.this);
                alt.setCancelable(true);
                alt.setTitle("User List");
                alt.setMultiChoiceItems(allNames,
                        checked,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton,
                                                boolean isChecked) {
                                if (isChecked) {
                                    selected.add(all.get(whichButton));
                                } else {
                                    for (int i = 0; i < selected.size(); i++) {
                                        if (selected.get(i).getEmail().equals(all.get(whichButton).getEmail())) {
                                            selected.remove(i);
                                        }
                                    }
                                }
                                Log.d("demo", selected.toString());
                            }
                        });
                alt.setPositiveButton("Select", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CustomAdapterForUserList adapter = new CustomAdapterForUserList(inviteusers.this, R.layout.userlist, selected);
                        sharedUsers.setAdapter(adapter);
                    }
                });
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                 ParseQuery<ParseObject> album=ParseQuery.getQuery("albums");
                        album.whereEqualTo("owner",ParseUser.getCurrentUser());
                        album.whereEqualTo("name",getIntent().getExtras().getString("name"));
                        album.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                if (e!=null){
                                    Log.d("demo","error in gettng the album: "+e.toString());
                                }
                                if (e==null) {
                                    ParseACL privateUsers = new ParseACL();
                                    //ParseRelation<ParseUser> relation = object.getRelation("usersAccessing");
                                    for (int i = 0; i < selected.size(); i++) {
                                        //relation.add(selected.get(i));
                                        // object.setACL(new ParseACL(selected.get(i)));
                                        //  object.saveInBackground();
                                        privateUsers.setWriteAccess(selected.get(i), true);
                                        privateUsers.setReadAccess(selected.get(i),true);
                                    }
                                    privateUsers.setWriteAccess(ParseUser.getCurrentUser(),true);
                                    privateUsers.setReadAccess(ParseUser.getCurrentUser(),true);
                                    object.setACL(privateUsers);
                                    object.saveInBackground();
                                }
                            }
                        });
                        finish();
                    }
                });
            }
        });



        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog alert11 = alt.create();
                alert11.show();

            }
        });



    }

}

