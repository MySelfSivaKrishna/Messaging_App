package com.parse.starter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 11/21/2015.
 */
public class UserList extends AppCompatActivity {
    public ArrayList<ParseUser> parseUsers;
    public static ParseUser user = ParseUser.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);
        updateUserStatus(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateUserStatus(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserList();
    }

    public void updateUserStatus(final boolean p){
        //    user.put("online", p);
        //  user.saveEventually();
        Log.d("p value",p+"");
        Log.d("user", ParseUser.getCurrentUser() + "");
        ParseUser.getCurrentUser().put("online", p);
       // ParseUser.getCurrentUser().setEmail("hello@gmail.com");
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {

                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "online unSucessfully",
                            Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            updateUserStatus(false);
            ParseUser.getCurrentUser().logOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadUserList(){
        final ProgressDialog pd = ProgressDialog.show(UserList.this,null,"Please Wait");
        ParseUser.getQuery().whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername())
                .findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        pd.dismiss();
                        if (objects != null) {
                            if (objects.size() == 0) {
                                Toast.makeText(UserList.this, "no user found", Toast.LENGTH_SHORT).show();
                            } else {
                                parseUsers = new ArrayList<ParseUser>(objects);
                                ListView listView = (ListView) findViewById(R.id.listView2);
                                listView.setAdapter(new UserAdapter());
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        Intent ilastActivity = new Intent(getApplicationContext(),
                                                Chat.class);
                                        ilastActivity.putExtra("DetailedValue", parseUsers.get(position).getUsername());
                                        startActivity(ilastActivity);

                                    }
                                });

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error occurred" + " " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }

    public  class UserAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return parseUsers.size();
        }

        @Override
        public ParseUser getItem(int position) {
            return parseUsers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.chat_list,null);
            ParseUser p = getItem(position);
            TextView uname = (TextView) convertView.findViewById(R.id.textView8);
            uname.setText(p.getUsername());
            ImageView iv = (ImageView) convertView.findViewById(R.id.imageView2);
            if(p.getBoolean("online")){
                iv.setImageResource(R.drawable.online4);
            }else{
                iv.setImageResource(R.drawable.offline5);
            }

            return convertView;
        }
    }
}
