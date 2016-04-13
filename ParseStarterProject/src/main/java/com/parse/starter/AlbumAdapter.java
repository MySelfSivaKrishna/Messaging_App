package com.parse.starter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Prithvi Macherla on 12/12/2015.
 */
public class AlbumAdapter extends ArrayAdapter {
    Context context;
    int resource;
    List<AlbumCover> objects;
    ImageView cover;
    public AlbumAdapter(Context context, int resource, List<AlbumCover> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            LayoutInflater inflate=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflate.inflate(resource,parent,false);
        }

        cover= (ImageView) convertView.findViewById(R.id.albumcover);
        TextView name= (TextView) convertView.findViewById(R.id.albumname);

        AlbumCover ac=objects.get(position);
if (!ac.getName().equals(""))
        name.setText(ac.getName());

        if (objects.get(position).getUrl().equals("default")){
            cover.setImageResource(R.mipmap.default_avatar);
        }
        else {
            Picasso.with(context).load(ac.getUrl()).into(cover);
        }



        return convertView;
    }
}
