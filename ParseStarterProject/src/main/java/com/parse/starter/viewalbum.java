package com.parse.starter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class viewalbum extends AppCompatActivity {
TextView name;
    GridView gridView;
    String sname;
    ArrayList<AlbumCover> urls=new ArrayList<>();
    AlbumAdapter adapter;
    String options[]={"View Photo"};
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewalbum);
        name= (TextView) findViewById(R.id.albumnameInViewAlbum);
        gridView= (GridView) findViewById(R.id.gridViewInViewAlbum);
sname=getIntent().getExtras().getString("name");

        name.setText(sname);
        ParseQuery<ParseObject> album=ParseQuery.getQuery("albums");
        album.whereEqualTo("name", sname);

        album.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {

                    Log.d("demo", "album name=" + object.getString("name"));

                    ParseQuery<ParseObject> allpics = ParseQuery.getQuery("Photos");
                    allpics.whereEqualTo("residesAt", object);
                    allpics.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {

                                Log.d("demo", "num of pics=" + objects.size());

                                for (int i = 0; i < objects.size(); i++) {
                                    String url = objects.get(i).getParseFile("url").getUrl();
                                    AlbumCover ac = new AlbumCover(url);
                                    urls.add(ac);
                                }
                                adapter = new AlbumAdapter(viewalbum.this, R.layout.albumviewforadapter, urls);
                                gridView.setAdapter(adapter);
                            }
                            if (e != null) {

                                Log.d("demo", "error in inner query=" + e.toString());

                            }
                        }
                    });

                    Log.d("demo", "urls size=" + urls.size());


                }
                if (e != null) {

                    Log.d("demo", "error in outer query=" + e.toString());

                }
            }
        });
        final AlertDialog.Builder alt=new AlertDialog.Builder(this)
                .setCancelable(true)
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int ii) {
                        if(ii==0){

                            Intent bigimage = new Intent(viewalbum.this, com.parse.starter.bigimage.class);
                            bigimage.putExtra("url", urls.get(position).getUrl());
                            startActivity(bigimage);
                        }
                        if(ii==1){
                            ParseQuery<ParseObject> deletepic=ParseQuery.getQuery("Photos");
                            deletepic.whereEqualTo("residesAt",sname);
                            deletepic.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e==null){
                                        objects.get(position).deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e==null){
                                                    Toast.makeText(viewalbum.this, "Photo Deleted!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        position = i;
        alt.show();


    }
});


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inviewalbum, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
if (id==R.id.addphotos){

    Log.d("demo", "name in addpic menu=" + sname);

    Intent addphotos=new Intent(this,addphotos.class);
    addphotos.putExtra("name", sname);
    startActivity(addphotos);



    return true;
}

        else if (id==R.id.deleteAlbum){
            ParseQuery<ParseObject> delete=ParseQuery.getQuery("albums");
            delete.whereEqualTo("name",sname);
            delete.whereEqualTo("owner",ParseUser.getCurrentUser());
            delete.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(final ParseObject object, ParseException e) {
                    if (e==null){
                        ParseQuery<ParseObject> pics=ParseQuery.getQuery("Photos");
                        pics.whereEqualTo("residesAt",object);
                        pics.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e==null){
                                    for(int i=0;i<objects.size();i++){
                                        objects.get(i).deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {

                                            }
                                        });
                                    }
                                    object.deleteInBackground(new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e==null){
                                                Toast.makeText(viewalbum.this, "Album deleted successfully!", Toast.LENGTH_SHORT).show();
                                              finish();


                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });
            return true;
        }
        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
