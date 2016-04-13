package com.parse.starter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class bigimage extends AppCompatActivity {
ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bigimage);
        String url=getIntent().getExtras().getString("url");
        iv= (ImageView) findViewById(R.id.imageViewInBigImage);
        Picasso.with(this).load(url).into(iv);
    }
}
