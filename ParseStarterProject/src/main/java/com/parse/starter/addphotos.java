package com.parse.starter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class addphotos extends AppCompatActivity {
    TextView albumName;
    ImageView addpics,addmorepics;
    TextView addmorepicstext;
    Button add,cancel;
    LinearLayout removechild,gridviewholder;
    GridView gridView;
    int upload_images=0,add_more_pics=1;
    ArrayList<String> selectedimages=new ArrayList<>();
    CustomAdapter adapter;
    String salbumname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addphotos);


        albumName=(TextView)findViewById(R.id.albumnameInAddPhotos);
        addpics= (ImageView) findViewById(R.id.addpicsInAddPhotos);
        add= (Button) findViewById(R.id.addInAddPhotos);
        cancel= (Button) findViewById(R.id.cancelInAddPhotos);
        removechild= (LinearLayout) findViewById(R.id.removechildfromthiscontainer);
        gridviewholder= (LinearLayout) findViewById(R.id.gridviewHolder);
        addmorepicstext= (TextView) findViewById(R.id.addmorepicstextInAddPhotos);
        addmorepics= (ImageView) findViewById(R.id.addmorepicsInAddPhotos);
        gridView= (GridView) findViewById(R.id.gridViewInAddPhotos);


        salbumname=getIntent().getExtras().getString("name");
        albumName.setText(salbumname);

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


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                        ParseQuery<ParseObject> query = ParseQuery.getQuery("albums");
                        query.whereEqualTo("name", salbumname);
                        query.whereEqualTo("owner", ParseUser.getCurrentUser());
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {


                                if (selectedimages.size() != 0) {
                                    for (int i = 0; i < selectedimages.size(); i++) {
                                        new ImageAsync(addphotos.this,objects.get(0)).execute(Uri.parse(selectedimages.get(i)));
                                    }
                                }
                                Toast.makeText(addphotos.this, "Photos saved successfully!", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        });
                    }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK&&requestCode==upload_images){
            removechild.removeView(findViewById(R.id.addpicsInAddPhotos));
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
