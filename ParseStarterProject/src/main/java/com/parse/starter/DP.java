package com.parse.starter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;

public class DP extends AppCompatActivity {
    ParseFile photo;
    ImageView dp;
    Button upload;
    TextView skip;
    int fill_image=1;
    Uri selectedImage;
    String options[]={"Set Picture","Remove"};
    boolean set=false;
    static  int RESULT_LOAD_IMAGE=1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK&&requestCode==fill_image){
            selectedImage = data.getData();
            dp.setImageURI(selectedImage);
            set=true;
        }
        if (requestCode == RESULT_LOAD_IMAGE && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();





            new ImageAsyncForDp(DP.this,dp,photo).execute(selectedImage);




        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_display_picture);

        dp= (ImageView) findViewById(R.id.pictureInDisplayPicture);
        upload= (Button) findViewById(R.id.uploadInDisplayPicture);
        skip= (TextView) findViewById(R.id.skipInDisplayPicture);

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
                            set=false;
                            //there is more.. edit it parse!
                        }
                    }
                });

//ImageView click listner(shows alert dialog)

        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alt.show();
            }
        });

//upload button click listner

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        if(set){
            Bitmap bitmap;
            bitmap= BitmapFactory.decodeResource(getResources(),
                    R.mipmap.ic_launcher);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            photo=new ParseFile("profilepic",stream.toByteArray());
            photo.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        run2(photo);
                    }
                }
            });
            Intent i=new Intent(DP.this, albumslists.class);
            startActivity(i);
        }
     else {
            Toast.makeText(DP.this, "Select an image to upload!", Toast.LENGTH_SHORT).show();
        }
            }

        });

//skip click listner

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.getCurrentUser().put("photo", "nil");
                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.d("left empty", "success");
                        Intent i = new Intent(DP.this, albumslists.class);
                        startActivity(i);
                    }
                });

            }
        });
    }


    public void run(final ParseFile b1){
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    ParseUser user=ParseUser.getCurrentUser();
                    user.put("profilepic",b1);
                    Log.d("demo",b1.toString());
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(DP.this, "Successfully Uploaded!", Toast.LENGTH_LONG).show();
                                Intent i=new Intent(DP.this,albumslists.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(DP.this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                                Log.d("error",e.toString());
                            }
                        }
                    });



            }
        });

    }

    public void run2(final ParseFile b1){

        ParseUser user=ParseUser.getCurrentUser();
        user.put("profilepic",b1);
        Log.d("demo",b1.toString());
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(DP.this, "Successfully Uploaded", Toast.LENGTH_LONG).show();
                    Intent i=new Intent(DP.this,albumslists.class);
                    startActivity(i);
                } else {
                    Toast.makeText(DP.this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.d("error",e.toString());
                }
            }
        });

    }
}
