package com.parse.starter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class editing extends AppCompatActivity {
EditText email,fname,lname;
    ImageView dp;
    Switch gender;
    Button save,cancel;
    String options[]={"Set Picture","Remove"};
    int fill_image=1;
    int set=0;
    Uri selectedImage;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK&&requestCode==fill_image){
            selectedImage = data.getData();
            dp.setImageURI(selectedImage);
            set=1;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editing);
            fname= (EditText) findViewById(R.id.fnameinEditing);
            lname= (EditText) findViewById(R.id.lnameinEditing);
            email= (EditText) findViewById(R.id.emailinEditing);
            save= (Button) findViewById(R.id.saveInEditing);
            cancel= (Button) findViewById(R.id.cancelnEditing);
            gender= (Switch) findViewById(R.id.genderinEditing);
            dp= (ImageView) findViewById(R.id.photoInEditing);

        fname.setText(ParseUser.getCurrentUser().getString("fname"));
        lname.setText(ParseUser.getCurrentUser().getString("lname"));
        email.setText(ParseUser.getCurrentUser().getEmail());
            if(ParseUser.getCurrentUser().getString("gender").equals("male")){
                gender.setChecked(false);
            }
            else {
                gender.setChecked(true);
            }
        if (ParseUser.getCurrentUser().getString("photo").equals("nil")){
            dp.setImageResource(R.mipmap.default_avatar);
        }
        else {
            dp.setImageURI(Uri.parse(ParseUser.getCurrentUser().getString("photo")));
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fname.getText().toString().equals("") && !lname.getText().toString().equals("") && !email.getText().toString().equals("")) {
                    String sgender;
                    if (gender.isChecked()) {
                        sgender = "female";
                    } else {
                        sgender = "male";
                    }
                    ParseUser.getCurrentUser().setEmail(email.getText().toString());
                    ParseUser.getCurrentUser().put("fname", fname.getText().toString());
                    ParseUser.getCurrentUser().put("lname", lname.getText().toString());
                    ParseUser.getCurrentUser().put("gender", sgender);
                    if (set!=0){
                        if (set==1){
                            ParseUser.getCurrentUser().put("photo",selectedImage.toString());
                        }
                        else {
                            ParseUser.getCurrentUser().put("photo","nil");
                        }
                    }
                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e==null){
                                Toast.makeText(editing.this, "Changes saved!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final AlertDialog.Builder alt=new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("Pick/Remove Photo")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0){

                            Intent fill=new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
                            startActivityForResult(fill, fill_image);
                        }
                        if(i==1){
                            dp.setImageResource(R.mipmap.default_avatar);
                            set=-1;
                            //there is more.. edit it parse!
                        }
                    }
                });

        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alt.show();
            }
        });

    }
}
