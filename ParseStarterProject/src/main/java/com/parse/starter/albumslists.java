package com.parse.starter;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class albumslists extends AppCompatActivity implements ItemFragment.OnFragmentInteractionListener,PublicAlbums.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);

       ViewPager pager= (ViewPager) findViewById(R.id.tabspager);
TabAdapter adapter=new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new PublicAlbums(), "Public");
        adapter.addFragment(new InvitedAlbums(), "Invited");
        adapter.addFragment(new ItemFragment(), "My Albums");

        pager.setAdapter(adapter);
        TabLayout tabLayout= (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.afterlogin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.editProfile) {
            Intent i=new Intent(this,editing.class);
            startActivity(i);

            return true;
        }
        else if(id==R.id.createAlbum){
Intent i=new Intent(this,createalbum.class);
            startActivity(i);
            return true;
        }
        else if (id==R.id.message){
            Intent intent=new Intent(this,UserList.class);
            startActivity(intent);

            return  true;
        }


        return super.onOptionsItemSelected(item);
    }
}
