package com.parse.starter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;
import com.parse.starter.R;

import java.util.List;

import static com.parse.starter.R.id.userFullName;

/**
 * Created by Prithvi Macherla on 12/8/2015.
 */
public class CustomAdapterForUserList extends ArrayAdapter {
    Context context;
    int resource;
    List<ParseUser> objects;
    public CustomAdapterForUserList(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            LayoutInflater inflate=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflate.inflate(resource,parent,false);
        }
        TextView tv= (TextView) convertView.findViewById(R.id.userFullName);
        tv.setText(objects.get(position).getString("fname")+" "+objects.get(position).getString("lname"));
return convertView;
    }
}
