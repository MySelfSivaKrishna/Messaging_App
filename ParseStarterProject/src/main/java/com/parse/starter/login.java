package com.parse.starter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;



public class login extends Fragment {
EditText username,password;
    Button login,signup,facebook,twitter;
    private OnFragmentInteractionListener mListener;

    public login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {

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
        public void signUP();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        username=(EditText)getActivity().findViewById(R.id.usernameInLogin);
        password= (EditText) getActivity().findViewById(R.id.passwordInLogin);
        login= (Button) getActivity().findViewById(R.id.loginInLogin);
        signup= (Button) getActivity().findViewById(R.id.signUpInLogin);
        facebook= (Button) getActivity().findViewById(R.id.facebookInLogin);
        twitter= (Button) getActivity().findViewById(R.id.twitterInLogin);

        username.setText("q@q.q");
        password.setText("q");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sUsername=username.getText().toString();
                String sPassword=password.getText().toString();

                if (sUsername.equals("")||sPassword.equals("")){
                    Toast.makeText(getActivity(), "Field(s) cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    ParseUser.logInInBackground(sUsername, sPassword, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, com.parse.ParseException e) {
                            if (user != null) {
//Tell main activity.
                                Toast.makeText(getActivity(), "Successfully logged in!", Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(getActivity(), albumslists.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(getActivity(), "Login failed!", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.signUP();
            }
        });
//ParseFacebookUtils.
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ParseFacebookUtils.logInWithReadPermissionsInBackground(getActivity(),null, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException err) {
                        if (user == null) {
                            Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                        } else if (user.isNew()) {
                            Log.d("MyApp", "User signed up and logged in through Facebook!");
                            Log.d("username",user.getUsername());
                        } else {
                            Log.d("MyApp", "User logged in through Facebook!");
                        }
                    }
                });
            }
        });
    }
}
