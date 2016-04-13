package com.parse.starter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Prithvi Macherla on 10/26/2015.
 */
public class CustomAdapter extends ArrayAdapter {
    Context context;
    int resource;
    List<String> objects;
    boolean flag;
    public CustomAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
        this.flag=flag;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            LayoutInflater inflate=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflate.inflate(resource,parent,false);
        }

ImageView iv= (ImageView) convertView.findViewById(R.id.selectedimageToupload);
        iv.setImageURI(Uri.parse(objects.get(position)));
        return convertView;
    }
}
