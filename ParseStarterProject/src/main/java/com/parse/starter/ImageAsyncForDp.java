package com.parse.starter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by ssirigin on 12/11/2015.
 */

public class ImageAsyncForDp extends AsyncTask<Uri, Void, byte[]> {
    Bitmap bmp;
    DP context;
    ParseFile photo;
    ImageView pic;

    public ImageAsyncForDp(DP context, ImageView profilepic, ParseFile photo) {
        this.context = context;
        pic=profilepic;
        this.photo=photo;
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
        super.onPostExecute(bytes);
        bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        //pic.setImageBitmap(bmp);
        //Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(bmp,100, 100);
        pic.setImageBitmap(bmp);
        photo=new ParseFile("profilepic",bytes);
        photo.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(context,
                            "Error saving: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                } else {
                    context.run(photo);
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
