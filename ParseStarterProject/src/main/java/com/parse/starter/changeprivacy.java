package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class changeprivacy extends AppCompatActivity {
String name;
    boolean oldprivacy,newprivacy;
    Button save,cancel;
    RadioGroup rg;
    RadioButton pu,pr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeprivacy);
        save= (Button) findViewById(R.id.SaveInChangePrivacy);
        cancel= (Button) findViewById(R.id.cancelInChangePrivacy);
        rg=(RadioGroup)findViewById(R.id.radiogroupInChangePrivacy);
        pu=(RadioButton)findViewById(R.id.radioButtonpublicInChangePrivacy);
        pr= (RadioButton) findViewById(R.id.radioButtonprivateInChangePrivacy);

        name=getIntent().getExtras().getString("name");
        oldprivacy=getIntent().getExtras().getBoolean("privacy");
        if (oldprivacy){
            rg.check(R.id.radioButtonpublicInChangePrivacy);
        }
        else {
            rg.check(R.id.radioButtonprivateInChangePrivacy);
        }


cancel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        finish();
    }
});

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rg.getCheckedRadioButtonId()==R.id.radioButtonpublicInChangePrivacy){
                    newprivacy=true;
                }
                else {
                    newprivacy=false;
                }

                if (oldprivacy!=newprivacy){
                    if (newprivacy){
                        ParseQuery<ParseObject> album=ParseQuery.getQuery("albums");
                        album.whereEqualTo("name",name);
                        album.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                if (e==null){
                                    object.put("privacy", newprivacy);
                                    ParseACL acl=new ParseACL();
                                    acl.setPublicWriteAccess(true);
                                    acl.setPublicReadAccess(true);
                                    object.setACL(acl);
object.saveInBackground(new SaveCallback() {
    @Override
    public void done(ParseException e) {
        if (e==null){
            Toast.makeText(changeprivacy.this, "Changes successful!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
});
                                }
                            }
                        });
                    }
                    else {
                        ParseQuery<ParseObject> album=ParseQuery.getQuery("albums");
                        album.whereEqualTo("name",name);
                        album.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                if (e==null){
                                    object.put("privacy", newprivacy);
                                    ParseACL acl=new ParseACL();
                                    acl.setPublicWriteAccess(false);
                                    acl.setPublicReadAccess(false);
                                    acl.setReadAccess(ParseUser.getCurrentUser(),true);
                                    acl.setWriteAccess(ParseUser.getCurrentUser(),true);
                                    object.setACL(acl);
                                    object.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e==null){
                                                Toast.makeText(changeprivacy.this, "Changes successful!", Toast.LENGTH_SHORT).show();
                                                Intent invite=new Intent(changeprivacy.this,inviteusers.class);
                                                invite.putExtra("firsttime",true);
                                                invite.putExtra("name",name);
                                                startActivity(invite);
                                                finish();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }


            }
        });
    }

}
/*ParseQuery<ParseObject> album=ParseQuery.getQuery("albums");
album.whereEqualTo("name",name);
        album.getFirstInBackground(new GetCallback<ParseObject>() {
@Override
public void done(ParseObject object, ParseException e) {
        if (e==null){
        object.put("privacy",privacy);

        }
        }
        });
        */