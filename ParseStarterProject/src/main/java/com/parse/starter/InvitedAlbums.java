package com.parse.starter;


import android.content.Intent;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class InvitedAlbums extends Fragment {
GridView gridView;
    ArrayList<AlbumCover> invited=new ArrayList<>();

    public InvitedAlbums() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invited_albums, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }
    @Override
    public void onResume() {
        super.onResume();
        invited.clear();
        gridView=(GridView)getActivity().findViewById(R.id.gridViewForInvitedAlbums);
        final AlbumAdapter adapter=new AlbumAdapter(getActivity(),R.layout.albumviewforadapter,invited);
        gridView.setAdapter(adapter);
        String s=ParseUser.getCurrentUser().getObjectId();
//        Log.d("object id", s);
        ParseQuery<ParseObject> all=ParseQuery.getQuery("albums");

        all.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.d("demo", "in invited" + objects.size());
                    for (int i = 0; i < objects.size(); i++) {
                        if ((objects.get(i).get("owner") != ParseUser.getCurrentUser()) && !objects.get(i).getACL().getPublicWriteAccess()) {
                            ParseACL acl = objects.get(i).getACL();
                            if (acl.getReadAccess(ParseUser.getCurrentUser())) {

                                final String name = objects.get(i).getString("name");
                                ParseQuery<ParseObject> pic = ParseQuery.getQuery("Photos");
                                pic.whereEqualTo("residesAt", objects.get(i));
                                pic.getFirstInBackground(new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject object, ParseException e) {
                                        if (e == null) {

                                            String url = object.getParseFile("url").getUrl();
                                            AlbumCover ac = new AlbumCover(name, url);
                                            invited.add(ac);
                                            adapter.notifyDataSetChanged();

                                        } else {
                                            Log.d("demo invited err", e.toString());
                                            String url = "default";
                                            AlbumCover ac = new AlbumCover(name, url);
                                            invited.add(ac);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent viewalbum = new Intent(getActivity(), com.parse.starter.viewalbum.class);
                viewalbum.putExtra("name", invited.get(i).getName());
                startActivity(viewalbum);
            }
        });
    }
}
/*
    ParseQuery<ParseObject> all=ParseQuery.getQuery("albums");

        all.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.d("demo", "in invited" + objects.size());
                    for (int i = 0; i < objects.size(); i++) {
                        if ((objects.get(i).get("owner") != ParseUser.getCurrentUser()) && !objects.get(i).getACL().getPublicWriteAccess()) {
                            ParseACL acl = objects.get(i).getACL();
                            if (acl.getReadAccess(ParseUser.getCurrentUser())) {

                                final String name = objects.get(i).getString("name");
                                ParseQuery<ParseObject> pic = ParseQuery.getQuery("Photos");
                                pic.whereEqualTo("residesAt", objects.get(i));
                                pic.getFirstInBackground(new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject object, ParseException e) {
                                        if (e == null) {

                                            String url = object.getParseFile("url").getUrl();
                                            AlbumCover ac = new AlbumCover(name, url);
                                            invited.add(ac);
                                            adapter.notifyDataSetChanged();

                                        } else {
                                            Log.d("demo invited err", e.toString());
                                            String url = "default";
                                            AlbumCover ac = new AlbumCover(name, url);
                                            invited.add(ac);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });
*/
