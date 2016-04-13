package com.parse.starter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class createalbum extends AppCompatActivity {
    EditText albumname;
    RadioGroup privacy;
    RadioButton pu,pr;
    ImageView addpics,addmorepics;
    TextView addmorepicstext;
    Button create,cancel;
    LinearLayout removechild,gridviewholder;
    GridView gridView;
    int upload_images=0,add_more_pics=1;
    ArrayList<String> selectedimages=new ArrayList<>();
    CustomAdapter adapter;
    String salbumname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createalbum);

        albumname= (EditText) findViewById(R.id.albumnameInCreateAlbum);
        privacy= (RadioGroup) findViewById(R.id.privacyRadioGroupInCreateAlbum);
        pu= (RadioButton) findViewById(R.id.pu);
        pr= (RadioButton) findViewById(R.id.pr);
        addpics= (ImageView) findViewById(R.id.addpicsInCreateAlbum);
        create= (Button) findViewById(R.id.createInCreateAblum);
        cancel= (Button) findViewById(R.id.cancelInCreateAlbum);
        removechild= (LinearLayout) findViewById(R.id.removechildfromthiscontainer);
        gridviewholder= (LinearLayout) findViewById(R.id.gridviewHolder);
        addmorepicstext= (TextView) findViewById(R.id.addmorepicstextInCreateAlbum);
        addmorepics= (ImageView) findViewById(R.id.addmorepicsInCreateAlbum);
        gridView= (GridView) findViewById(R.id.gridViewInCreateAlbum);
        privacy.check(R.id.pu);



        addpics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent fill = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(fill, upload_images);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                 salbumname = albumname.getText().toString();
                final boolean bprivacy;
                if (privacy.getCheckedRadioButtonId() == R.id.pu) {
                    bprivacy = true;
                } else {
                    bprivacy = false;
                }
                ParseObject myAlbum = new ParseObject("albums");
                myAlbum.put("name", salbumname);
                myAlbum.put("privacy",bprivacy);
                myAlbum.put("owner", ParseUser.getCurrentUser());
                myAlbum.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("albums");
                        query.whereEqualTo("name", salbumname);
                        query.whereEqualTo("owner", ParseUser.getCurrentUser());
                 query.findInBackground(new FindCallback<ParseObject>() {
                     @Override
                     public void done(List<ParseObject> objects, ParseException e) {


                         if (selectedimages.size() != 0) {
                             for (int i = 0; i < selectedimages.size(); i++) {
                                 new ImageAsync(createalbum.this,objects.get(0)).execute(Uri.parse(selectedimages.get(i)));
                             }
                         }
                         Toast.makeText(createalbum.this, "Album saved successfully!", Toast.LENGTH_SHORT).show();
                         if (bprivacy){
                            final ParseACL allOfThem=new ParseACL();
                           allOfThem.setPublicReadAccess(true);
                             allOfThem.setPublicWriteAccess(true);
                             objects.get(0).setACL(allOfThem);
                             objects.get(0).saveInBackground();
                             finish();
                         }
                         else {
                             // Adding users that can access the album!
                             Intent invite=new Intent(createalbum.this,inviteusers.class);
                             invite.putExtra("firsttime",true);
                             invite.putExtra("name",salbumname);
                             startActivity(invite);
                             finish();
                         }
                     }
                 });
                    }

                });
                        }

    });
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK&&requestCode==upload_images){
            removechild.removeView(findViewById(R.id.addpicsInCreateAlbum));
            removechild.removeView(findViewById(R.id.textView6));

            selectedimages.add(data.getData().toString());
           adapter=new CustomAdapter(this,R.layout.imagestoupload,selectedimages);
            gridView.setAdapter(adapter);
            addmorepicstext.setText("Add more pictures!");
            addmorepics.setImageResource(R.mipmap.addpics);
            addmorepics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent fill=new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
                    startActivityForResult(fill, add_more_pics);
                }
            });

        }
        if (resultCode==RESULT_OK&&requestCode==add_more_pics){
            selectedimages.add(data.getData().toString());
            adapter.notifyDataSetChanged();
        }
    }
}
