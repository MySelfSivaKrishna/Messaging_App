package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link signup.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class signup extends Fragment {
    EditText fname,email,p,cp,lname;
    Button signUpsignUp,cancel;
    Switch gender;

    private OnFragmentInteractionListener mListener;

    public signup() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
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
        // TODO: Update argument type and name

    }

    public void run2(final ParseFile b1){

        final String slname,sfname,sEmail,sP,sCp,sgender;
        sfname=fname.getText().toString();
        slname=lname.getText().toString();
        sEmail=email.getText().toString();
        sP=p.getText().toString();
        sCp=cp.getText().toString();
        if(gender.isChecked()){
            sgender="female";
        }
        else {
            sgender="male";
        }


        if (slname.equals("")||sfname.equals("")||sEmail.equals("")||sP.equals("")||sCp.equals("")||!sP.equals(sCp)) {
            if (!sP.equals(sCp)) {
                Toast.makeText(getActivity(), "Password and Confirm Password do not match!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Field(s) cannot be empty", Toast.LENGTH_SHORT).show();
            }
        }
        else {

            ParseUser user=new ParseUser();
            user.setUsername(sEmail);
            user.setPassword(sP);
            user.setEmail(sEmail);
            user.put("fname", sfname);
            user.put("lname", slname);
            user.put("gender",sgender);
            user.put("profilepic",b1);
            Log.d("demo",sEmail+sP+sEmail+sfname+slname+sgender+b1.toString());
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                    if (e == null) {
                        Toast.makeText(getActivity(), "Successfully Logged in!", Toast.LENGTH_LONG).show();
                        //Intent i=new Intent(getActivity(),login.class);
                        //startActivity(i);
                    } else {
                        Toast.makeText(getActivity(), "Email already exists!", Toast.LENGTH_SHORT).show();
                        Log.d("error",e.toString());
                    }
                }
            });

        }
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fname=(EditText)getActivity().findViewById(R.id.fnameInSignUp);
        lname= (EditText) getActivity().findViewById(R.id.lnameInSignUp);
        email=(EditText)getActivity().findViewById(R.id.emailInSignUp);
        p=(EditText)getActivity().findViewById(R.id.passwordInSignUp);
        cp=(EditText)getActivity().findViewById(R.id.confirmpasswordInSignUp);
        signUpsignUp=(Button) getActivity().findViewById(R.id.signupInSignUp);
        cancel=(Button)getActivity().findViewById(R.id.cancelinSignUp);
        gender= (Switch) getActivity().findViewById(R.id.malefemale);

        signUpsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String slname,sfname,sEmail,sP,sCp,sgender;
                sfname=fname.getText().toString();
                slname=lname.getText().toString();
                sEmail=email.getText().toString();
                sP=p.getText().toString();
                sCp=cp.getText().toString();
                if(gender.isChecked()){
                    sgender="female";
                }
                else {
                    sgender="male";
                }
                if (slname.equals("")||sfname.equals("")||sEmail.equals("")||sP.equals("")||sCp.equals("")||!sP.equals(sCp)) {
                    if (!sP.equals(sCp)) {
                        Toast.makeText(getActivity(), "Password and Confirm Password do not match!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Field(s) cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                }
                else {

                    ParseUser user=new ParseUser();
                    user.setUsername(sEmail);
                    user.setPassword(sP);
                    user.setEmail(sEmail);
                    user.put("fname", sfname);
                    user.put("lname", slname);
                    user.put("gender",sgender);
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            if (e == null) {
                                Toast.makeText(getActivity(), "Successfully Logged in!", Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(getActivity(),DP.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(getActivity(), "Email already exists!", Toast.LENGTH_SHORT).show();
                                Log.d("error",e.toString());
                            }
                        }
                    });

                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//mListener.gotoLogin();
                getFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

}
