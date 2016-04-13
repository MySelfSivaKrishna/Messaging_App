package com.parse.starter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ssirigin on 12/11/2015.
 */

public class ImageAsync extends AsyncTask<Uri, Void, byte[]> {
    Bitmap bmp;
    Activity context;
    ParseFile photo;
    ParseObject object;


    public ImageAsync(Activity context ,ParseObject object) {
        this.context = context;
        this.object=object;

    }

    @Override
    protected void onPostExecute(byte[] bytes) {
        super.onPostExecute(bytes);
        bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        photo=new ParseFile("albumpics",bytes);
        photo.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {

                } else {
                    ParseObject pic = new ParseObject("Photos");
                    pic.put("url", photo);
                    pic.put("residesAt", object);
                    ParseACL acl=new ParseACL();
                    acl.setPublicReadAccess(true);
                    acl.setPublicWriteAccess(true);
                    pic.setACL(acl);
                    pic.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e!=null)
                                Log.d("error in async",e.toString());
                            else {
                                Log.d("demo", "images getting saved in photos table.");
                            }

                        }
                    });
                }

            }
        });


    }

    @Override
    protected byte[] doInBackground(Uri... params) {
        try {
            bmp = getBitmapFromUri(params[0]);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 10, stream);

            return stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        assert parcelFileDescriptor != null;
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}
