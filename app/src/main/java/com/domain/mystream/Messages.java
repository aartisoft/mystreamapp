package com.domain.mystream;

/* ================================

    - MyStream -

    created by cubycode @2018
    All Rights reserved

===================================*/

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Messages extends AppCompatActivity {


    /* Views */
    ListView messListView;
    RelativeLayout noMessLayout;



    /* Variables */
    List<ParseObject> messagesArray;





    @Override
    protected void onStart() {
        super.onStart();

        // Call query
        queryMessages();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Hide ActionBar
        getSupportActionBar().hide();


        // Init views
        messListView = findViewById(R.id.messagesListView);
        noMessLayout = findViewById(R.id.messNoMessLayout);


        // Init TabBar buttons
        Button tab_one = findViewById(R.id.tab_one);
        Button tab_two = findViewById(R.id.tab_two);
        Button tab_three = findViewById(R.id.tab_three);
        Button tab_four = findViewById(R.id.tab_four);


        tab_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Messages.this, Home.class));
                overridePendingTransition(0, 0);
            }});

        tab_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Messages.this, Search.class));
                overridePendingTransition(0, 0);
            }});


        tab_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Messages.this, Following.class));
                overridePendingTransition(0, 0);
            }});

        tab_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Messages.this, Account.class));
                overridePendingTransition(0, 0);
            }});



    }// end onCreate()





    // MARK: - QUERY CHATS ----------------------------------------------------------------------
    void queryMessages() {
        Configs.showPD("Please wait...", Messages.this);

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.MESSAGES_CLASS_NAME);
        query.include(Configs.USER_CLASS_NAME);
        query.whereContains(Configs.MESSAGES_ID, ParseUser.getCurrentUser().getObjectId());
        query.orderByDescending(Configs.MESSAGES_CREATED_AT);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, final ParseException error) {
                if (error == null) {
                    messagesArray = objects;
                    Configs.hidePD();

                    // Show/hide NO messages Layout
                    if (messagesArray.size() == 0) {
                        messListView.setVisibility(View.INVISIBLE);
                        noMessLayout.setVisibility(View.VISIBLE);
                    } else {
                        messListView.setVisibility(View.VISIBLE);
                        noMessLayout.setVisibility(View.INVISIBLE);
                    }


                    // CUSTOM LIST ADAPTER
                    class ListAdapter extends BaseAdapter {
                        private Context context;
                        public ListAdapter(Context context, List<ParseObject> objects) {
                            super();
                            this.context = context;
                        }

                        // CONFIGURE CELL
                        @Override
                        public View getView(int position, View cell, ViewGroup parent) {
                            if (cell == null) {
                                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                assert inflater != null;
                                cell = inflater.inflate(R.layout.cell_messages, null);
                            }
                            final View finalCell = cell;


                            // Get Parse object
                            final ParseObject mObj = messagesArray.get(position);
                            final ParseUser currUser = ParseUser.getCurrentUser();

                                    // Get userPointer
                                    mObj.getParseObject(Configs.MESSAGES_USER_POINTER).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                                        public void done(final ParseObject userPointer, ParseException e) {

                                            // Get otherPointer
                                            mObj.getParseObject(Configs.MESSAGES_OTHER_USER).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                                                @SuppressLint("SetTextI18n")
                                                public void done(ParseObject otherPointer, ParseException e) {

                                                    // Init views
                                                    ImageView avatarImg = finalCell.findViewById(R.id.cmessAvatarImg);
                                                    TextView fullnameTxt = finalCell.findViewById(R.id.cmessFullnameTxt);
                                                    fullnameTxt.setTypeface(Configs.titSemibold);
                                                    TextView senderTxt = finalCell.findViewById(R.id.cmessSenderTxt);
                                                    senderTxt.setTypeface(Configs.titRegular);


                                                    // Get Sender's username
                                                    if (userPointer.getObjectId().matches(currUser.getObjectId()) ){
                                                        senderTxt.setText("You wrote:");
                                                        Configs.getParseImage(avatarImg, otherPointer, Configs.USER_AVATAR);
                                                        fullnameTxt.setText(otherPointer.getString(Configs.USER_FULLNAME));
                                                    } else {
                                                        senderTxt.setText("@" + userPointer.getString(Configs.USER_USERNAME));
                                                        fullnameTxt.setText(userPointer.getString(Configs.USER_FULLNAME));
                                                        Configs.getParseImage(avatarImg, userPointer, Configs.USER_AVATAR);
                                                    }


                                                    // Get date
                                                    TextView dateTxt = finalCell.findViewById(R.id.cmessDateTxt);
                                                    dateTxt.setTypeface(Configs.titRegular);
                                                    dateTxt.setText(Configs.timeAgoSinceDate(mObj.getCreatedAt()));

                                                    // Get last Message
                                                    TextView lastMessTxt = finalCell.findViewById(R.id.cmessLastMessTxt);
                                                    lastMessTxt.setTypeface(Configs.titRegular);
                                                    lastMessTxt.setText(mObj.getString(Configs.MESSAGES_LAST_MESSAGE));


                                            }});// end otherUser

                                    }});// end userPointer


                        return cell;
                        }

                        @Override public int getCount() { return messagesArray.size(); }
                        @Override public Object getItem(int position) { return messagesArray.get(position); }
                        @Override public long getItemId(int position) { return position; }
                    }


                    // Init ListView and set its adapter
                    messListView.setAdapter(new ListAdapter(Messages.this, messagesArray));
                    messListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                            final ParseObject mObj = messagesArray.get(position);
                            
                            // Get userPointer
                            mObj.getParseUser(Configs.MESSAGES_USER_POINTER).fetchIfNeededInBackground(new GetCallback<ParseUser>() {
                                public void done(final ParseUser userPointer, final ParseException e) {

                                    // Get otherPointer
                                    mObj.getParseUser(Configs.MESSAGES_OTHER_USER).fetchIfNeededInBackground(new GetCallback<ParseUser>() {
                                        public void done(final ParseUser otherUser, ParseException e) {

                                            ParseUser currUser = ParseUser.getCurrentUser();
                                            List<String> blockedUsers = new ArrayList<>();
                                            String blockMessage = "";

                                            if (userPointer.getObjectId().matches(currUser.getObjectId()) ){
                                                blockedUsers = otherUser.getList(Configs.USER_HAS_BLOCKED);
                                                blockMessage = "Sorry, @" + otherUser.getString(Configs.USER_USERNAME) +
                                                        " has blocked you, you can't chat with this user.";
                                            } else {
                                                blockedUsers = userPointer.getList(Configs.USER_HAS_BLOCKED);
                                                blockMessage = "Sorry, @" + userPointer.getString(Configs.USER_USERNAME) +
                                                        " has blocked you, you can't chat with this user.";
                                            }

                                            // otherUser user has blocked you
                                            if (blockedUsers.contains(currUser.getObjectId())) {
                                                Configs.simpleAlert(blockMessage, Messages.this);

                                            // You can chat with otherUser
                                            } else {
                                                Intent i = new Intent(Messages.this, InboxActivity.class);
                                                Bundle extras = new Bundle();
                                                String userID = "";
                                                if (userPointer.getObjectId().matches(currUser.getObjectId())) {
                                                    userID = otherUser.getObjectId();
                                                } else {
                                                    userID = userPointer.getObjectId();
                                                }

                                                extras.putString("userID", userID);
                                                i.putExtras(extras);
                                                startActivity(i);
                                            }

                                    }});// end otherPointer

                            }});// end userPointer

                    }});


                // Error in query
                } else {
                    Configs.hidePD();
                    Configs.simpleAlert(error.getMessage(), Messages.this);
        }}});

    }






}//@end
