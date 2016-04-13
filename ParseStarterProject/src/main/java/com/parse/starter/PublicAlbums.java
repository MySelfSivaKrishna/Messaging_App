package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

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
 * Activities that contain this fragment must implement the
 * {@link PublicAlbums.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PublicAlbums extends Fragment {
GridView gridView;
    private OnFragmentInteractionListener mListener;
ArrayList<AlbumCover> pubAlbums=new ArrayList<>();
    public PublicAlbums() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_public_albums, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
        //    mListener.onFragmentInteraction(uri);
        }
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
     //   public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        pubAlbums.clear();

        gridView=(GridView)getActivity().findViewById(R.id.gridViewForPublicAlbums);
        final AlbumAdapter adapter=new AlbumAdapter(getActivity(),R.layout.albumviewforadapter,pubAlbums);
        gridView.setAdapter(adapter);

        final ParseQuery<ParseObject> publicAlbums=ParseQuery.getQuery("albums");
        publicAlbums.whereEqualTo("privacy", true);
        publicAlbums.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        if (objects.get(i).get("owner") != ParseUser.getCurrentUser()) {

                            final String name = objects.get(i).getString("name");
                            ParseQuery<ParseObject> pic = ParseQuery.getQuery("Photos");
                            pic.whereEqualTo("residesAt", objects.get(i));
                            pic.getFirstInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject object, ParseException e) {
                                    if (e == null) {
                                        String url = object.getParseFile("url").getUrl();
                                        AlbumCover ac = new AlbumCover(name, url);
                                        pubAlbums.add(ac);

                                        adapter.notifyDataSetChanged();

                                    }
                                    if (e != null) {
                                        ;
                                        String url = "default";
                                        AlbumCover ac = new AlbumCover(name, url);
                                        pubAlbums.add(ac);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });

                        }
                    }


                }
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent viewalbum=new Intent(getActivity(), com.parse.starter.viewalbum.class);
                viewalbum.putExtra("name",pubAlbums.get(i).getName());
                startActivity(viewalbum);
            }
        });
    }
}
