package com.parse.starter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment implements AbsListView.OnItemClickListener {
    String options[]={"Album Details","Change Privacy","Add Photos","View Album","Delete Album"};
GridView albumList;
    String sname,acl; //Ikkada thappu povachuu !!
    boolean sprivacy;
ArrayList<AlbumCover> mine=new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    public AlertDialog.Builder alt;
    ArrayList<String> intentusersnames;
    ArrayList<albumsharedwith> foreach=new ArrayList<>();
    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private AlbumAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // TODO: Change Adapter to display your content

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_grid, container, false);

        // Set the adapter
      //  mListView = (AbsListView) view.findViewById(android.R.id.list);
//        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
    //    mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
          //  mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //public void onFragmentInteraction(String id);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    @Override
    public void onResume() {
        super.onResume();
        mine.clear();
        foreach.clear();

        albumList= (GridView)getActivity().findViewById(R.id.gridViewForMyAlbums);

        final AlbumAdapter adapter = new AlbumAdapter(getActivity(), R.layout.albumviewforadapter, mine);
        albumList.setAdapter(adapter);


        ParseQuery<ParseObject> myalbums=ParseQuery.getQuery("albums");
        myalbums.whereEqualTo("owner", ParseUser.getCurrentUser());
        myalbums.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> objects, ParseException e) {
                if (e == null) {


                    for (int i = 0; i < objects.size(); i++) {

                        final String name = objects.get(i).getString("name");
                        final boolean privacy = objects.get(i).getBoolean("privacy");

                        final ParseACL acl = objects.get(i).getACL();

                        if (objects.get(i).getBoolean("privacy")) {
                            Log.d("demo", "working");
                            ParseQuery<ParseUser> all = ParseQuery.getQuery("_User");
                            all.findInBackground(new FindCallback<ParseUser>() {
                                @Override
                                public void done(List<ParseUser> objects, ParseException e) {
                                    ArrayList<String> usersnames = new ArrayList<String>();


                                    for (int i = 0; i < objects.size(); i++) {

                                        usersnames.add(objects.get(i).getString("fname") + " " + objects.get(i).getString("lname"));


                                    }
                                    albumsharedwith shared = new albumsharedwith(name, usersnames);
                                    foreach.add(shared);
                                }
                            });
                        } else {
                            ParseQuery<ParseUser> all = ParseQuery.getQuery("_User");
                            all.findInBackground(new FindCallback<ParseUser>() {
                                @Override
                                public void done(List<ParseUser> objects, ParseException e) {
                                    ArrayList<String> usersnames = new ArrayList<String>();
                                    for (int i = 0; i < objects.size(); i++) {
                                        if (acl.getReadAccess(objects.get(i))) {

                                            usersnames.add(objects.get(i).getString("fname") + " " + objects.get(i).getString("lname"));

                                        }
                                    }
                                    albumsharedwith shared = new albumsharedwith(name, usersnames);
                                    foreach.add(shared);
                                }
                            });
                        }


                        ParseQuery<ParseObject> pic = ParseQuery.getQuery("Photos");
                        pic.whereEqualTo("residesAt", objects.get(i));

                        pic.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                if (e == null) {

                                    String url = object.getParseFile("url").getUrl();
                                    AlbumCover ac = new AlbumCover(name, url);
                                    ac.setPrivacy(privacy);
                                    mine.add(ac);
                                    adapter.notifyDataSetChanged();
                                }
                                if (object == null) {
                                    String url = "default";
                                    AlbumCover ac = new AlbumCover(name, url);
                                    ac.setPrivacy(privacy);
                                    mine.add(ac);
                                    adapter.notifyDataSetChanged();

                                }

                            }
                        });

                    }
                }
            }
        });

        albumList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sname = mine.get(i).getName();
                if (mine.get(i).isPrivacy()) {
                    sprivacy = true;
                } else {
                    sprivacy = false;
                }
                for (int ii = 0; ii < foreach.size(); ii++) {
                    if (foreach.get(ii).getAlbum().equals(sname)) {
                        intentusersnames = foreach.get(ii).getName();
                    }
                }
                alt.show();
            }
        });


        alt=new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i==0) { // album details
                            Intent viewAlbumDetails=new Intent(getActivity(),AlbumDetails.class);
                            viewAlbumDetails.putExtra("name",sname);
                            viewAlbumDetails.putExtra("privacy",sprivacy);
                            viewAlbumDetails.putExtra("usersnames",intentusersnames);
                            startActivity(viewAlbumDetails);
                        }
                        if (i==1){ // changing privacy
                            Intent changePrivacy=new Intent(getActivity(),changeprivacy.class);
                            changePrivacy.putExtra("name",sname);
                            changePrivacy.putExtra("privacy",sprivacy);
                            startActivity(changePrivacy);
                        }
                        if (i==2){ //Adding photos
                            Intent addphotos=new Intent(getActivity(), com.parse.starter.addphotos.class);
                            addphotos.putExtra("name",sname);
                            startActivity(addphotos);
                        }
                        if (i==3){ //view album
                            Intent viewalbum=new Intent(getActivity(), com.parse.starter.viewalbum.class);
                            viewalbum.putExtra("name",sname);

                            startActivity(viewalbum);
                        }
                        if (i==4){
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
                                                                Toast.makeText(getActivity(), "Album deleted successfully!", Toast.LENGTH_SHORT).show();
                                                               for(int i=0;i<mine.size();i++){
                                                                   if (mine.get(i).getName().equals(sname)){
                                                                       mine.remove(i);
                                                                       adapter.notifyDataSetChanged();
                                                                   }
                                                               }


                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
    }
}
