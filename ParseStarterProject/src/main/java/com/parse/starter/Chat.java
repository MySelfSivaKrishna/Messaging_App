package com.parse.starter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Vinay on 11/22/2015.
 */
public class Chat extends AppCompatActivity {

    private ArrayList<Conversation> convList;
    private EditText txt;
    private String buddy;
    private Date lastMsgDate;
    private boolean isRunning;
    private static android.os.Handler handler;
    private ChatAdapter adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        convList = new ArrayList<Conversation>();
        ListView list = (ListView) findViewById(R.id.listView);
        ImageButton btn = (ImageButton) findViewById(R.id.imageButton);
        adp = new ChatAdapter();
        list.setAdapter(adp);
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setStackFromBottom(true);
        txt = (EditText) findViewById(R.id.editText);
        txt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        buddy = getIntent().getStringExtra("DetailedValue");
        handler = new android.os.Handler();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        isRunning =true;
        loadConversationList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning= false;
    }
    public void sendMessage(){

        if(txt.length() == 0){

            Toast.makeText(Chat.this, "Message cannot be left empty", Toast.LENGTH_SHORT).show();
        }

        String s =txt.getText().toString();
        final Conversation c =  new Conversation(s,new Date(), UserList.user.getUsername());
        c.setStatus(Conversation.STATUS_SENDING);
        convList.add(c);
        adp.notifyDataSetChanged();
        txt.setText(null);
        ParseObject po = new ParseObject("Chat");
        po.put("sender", UserList.user.getUsername());
        po.put("receiver",buddy);
        po.put("message",s);
        po.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null)
                    c.setStatus(Conversation.STATUS_SENT);
                else
                    c.setStatus(Conversation.STATUS_FAILED);
                adp.notifyDataSetChanged();
            }
        });


    }
    public void loadConversationList(){
        ParseQuery<ParseObject> q = ParseQuery.getQuery("Chat");
        if(convList.size() == 0){
            ArrayList<String> al = new ArrayList<String>();
            al.add(buddy);
            al.add(ParseUser.getCurrentUser().getUsername());
            q.whereContainedIn("sender",al);
            q.whereContainedIn("receiver",al);
        }else{

            if(lastMsgDate != null)
                q.whereGreaterThan("createdAt",lastMsgDate);
            q.whereEqualTo("sender",buddy);
            q.whereEqualTo("receiver", ParseUser.getCurrentUser().getUsername());
        }
        q.orderByDescending("createdAt");
        q.setLimit(30);
        q.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> li, ParseException e) {
                if(li != null && li.size() > 0){
                    for(int i =li.size() - 1; i >= 0; i--){
                        ParseObject po =li.get(i);
                        Conversation c = new Conversation(po.getString("message"),po.getCreatedAt(),po.getString("sender"));
                        convList.add(c);
                        if(lastMsgDate == null || lastMsgDate.before(c.getDate()))
                            lastMsgDate = c.getDate();
                        adp.notifyDataSetChanged();

                    }

                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(isRunning)
                            loadConversationList();
                    }
                },1000);
            }
        });
    }
    private class ChatAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return convList.size();
        }

        @Override
        public Conversation getItem(int position) {
            return convList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int pos, View v, ViewGroup parent) {
            Conversation c =getItem(pos);
            if(c.isSent())
                v= getLayoutInflater().inflate(R.layout.chat_sent,null);
            else
                v = getLayoutInflater().inflate(R.layout.chat_received,null);
            TextView lb1 = (TextView) v.findViewById(R.id.textView);
            lb1.setText(DateUtils.getRelativeDateTimeString(Chat.this,c.getDate().getTime()
                    ,DateUtils.SECOND_IN_MILLIS,DateUtils.DAY_IN_MILLIS,0));
            lb1 = (TextView) v.findViewById(R.id.textView2);
            lb1.setText(c.getMsg());
            lb1 = (TextView) v.findViewById(R.id.textView3);
            if(c.isSent()){
                if(c.getStatus() == Conversation.STATUS_SENT)
                    lb1.setText("Delivered");
                else if(c.getStatus() ==  Conversation.STATUS_SENDING)
                    lb1.setText("Sending");
                else
                    lb1.setText("Failed");
            }
            else
                lb1.setText("");

            return v;
        }
    }
}
