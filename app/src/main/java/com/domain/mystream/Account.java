package com.domain.mystream;

/* ================================

    - MyStream -

    created by cubycode @2018
    All Rights reserved

===================================*/

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.CountCallback;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class Account extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    /* Views */
    ListView streamsListView;
    ImageView avatarImg, coverImg;
    SwipeRefreshLayout refreshControl;
    TextView usernameTxt, fullNameTxt, aboutMeTxt;
    Button followersButt, followingButt;


    /* Variables */
    List<ParseObject> streamsArray;
    MarshMallowPermission mmp = new MarshMallowPermission(this);


    @Override
    protected void onStart() {
        super.onStart();

        // Call queries
        showUserDetails();
        getFollowersAndFollowing();

        // Recall query in case something has been reported (either a User or a Stream)
        if (Configs.mustRefresh) {
            queryStreams();
            Configs.mustRefresh = false;
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Hide ActionBar
        getSupportActionBar().hide();

        // Change StatusBar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));


        // Init views
        avatarImg = findViewById(R.id.accAvatarImg);
        coverImg = findViewById(R.id.accCoverImg);
        usernameTxt = findViewById(R.id.accUsernameTxt);
        usernameTxt.setTypeface(Configs.titSemibold);
        followersButt = findViewById(R.id.accFollowersButt);
        followersButt.setTypeface(Configs.titRegular);
        followingButt = findViewById(R.id.accFollowingButt);
        followingButt.setTypeface(Configs.titRegular);
        fullNameTxt = findViewById(R.id.accFullnameTxt);
        fullNameTxt.setTypeface(Configs.titBlack);
        aboutMeTxt = findViewById(R.id.accAboutMeTxt);
        aboutMeTxt.setTypeface(Configs.titRegular);
        streamsListView = findViewById(R.id.accStreamsListView);

        // Init a refreshControl
        refreshControl = findViewById(R.id.swiperefresh);
        refreshControl.setOnRefreshListener(this);



        // Init TabBar buttons
        Button tab_one = findViewById(R.id.tab_one);
        Button tab_two = findViewById(R.id.tab_two);
        Button tab_three = findViewById(R.id.tab_three);
        Button tab_four = findViewById(R.id.tab_five);

        tab_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account.this, Home.class));
                overridePendingTransition(0, 0);
            }});

        tab_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account.this, Search.class));
                overridePendingTransition(0, 0);
            }});

        tab_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account.this, Following.class));
                overridePendingTransition(0, 0);
            }});

        tab_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account.this, Messages.class));
                overridePendingTransition(0, 0);
            }});

        // Call query
        queryStreams();




        // MARK: - ADD STREAM BUTTON ------------------------------------
        final Button addStreamButt = findViewById(R.id.accAddStreamButt);
        addStreamButt.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              startActivity(new Intent(Account.this, AddStream.class));
        }});




        // MARK: - SETTINGS BUTTON ------------------------------------
        final Button settButt = findViewById(R.id.accSettingsButt);
        settButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Account.this, Settings.class));
        }});


        // MARK: - FOLLOWING BUTTON ------------------------------------
        followingButt.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent i = new Intent(Account.this, Follow.class);
              Bundle extras = new Bundle();
              extras.putString("isFollowing", "true");
              extras.putString("userID", ParseUser.getCurrentUser().getObjectId());
              i.putExtras(extras);
              startActivity(i);
        }});



        // MARK: - FOLLOWERS BUTTON ------------------------------------
        followersButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Account.this, Follow.class);
                Bundle extras = new Bundle();
                extras.putString("isFollowing", "false");
                extras.putString("userID", ParseUser.getCurrentUser().getObjectId());
                i.putExtras(extras);
                startActivity(i);
        }});

    }// end onCreate()







    // MARK: - GET FOLLOWERS AND FOLLOWING AMOUNT -------------------------------------------------
    void getFollowersAndFollowing() {
        final ParseUser currUser = ParseUser.getCurrentUser();

        // QUERY FOLLOWERS
        final ParseQuery<ParseObject>query = ParseQuery.getQuery(Configs.FOLLOW_CLASS_NAME);
        query.whereEqualTo(Configs.FOLLOW_IS_FOLLOWING, currUser);
        query.orderByDescending(Configs.FOLLOW_CREATED_AT);
        query.countInBackground(new CountCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void done(int amount, ParseException e) {
                if (e == null) {
                    String foll = Configs.roundThousandsIntoK(amount);
                    followersButt.setText(foll+ "\nfollowers");

                    // QUERY FOLLOWING
                    ParseQuery<ParseObject>query2 = ParseQuery.getQuery(Configs.FOLLOW_CLASS_NAME);
                    query2.whereEqualTo(Configs.FOLLOW_CURR_USER, currUser);
                    query2.orderByDescending(Configs.FOLLOW_CREATED_AT);
                    query2.countInBackground(new CountCallback() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void done(int amount, ParseException e) {
                            if (e == null) {
                                String foll = Configs.roundThousandsIntoK(amount);
                                followingButt.setText(foll + "\nfollowing");
                    }}});
        }}});

    }






    // MARK: - SHOW USER'S DETAILS ------------------------------------------------------
    void showUserDetails() {
        ParseUser currUser = ParseUser.getCurrentUser();

        // Get username
        usernameTxt.setText("@" + currUser.getString(Configs.USER_USERNAME));
        // Get fullName
        fullNameTxt.setText(currUser.getString(Configs.USER_FULLNAME));

        // Get aboutMe
        if (currUser.getString(Configs.USER_ABOUT_ME) != null ){
            aboutMeTxt.setText(currUser.getString(Configs.USER_ABOUT_ME));
        } else { aboutMeTxt.setText(""); }

        // Get avatar
        Configs.getParseImage(avatarImg, currUser, Configs.USER_AVATAR);

        // Get cover
        if (currUser.getParseFile(Configs.USER_COVER_IMAGE) != null){
            Configs.getParseImage(coverImg, currUser, Configs.USER_COVER_IMAGE);
        } else { coverImg.setImageDrawable(null); }

    }






    // MARK: - QUERY STREAMS -------------------------------------------------
    void queryStreams() {
        Configs.showPD("Please wait...", Account.this);
        ParseUser currUser = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.STREAMS_CLASS_NAME);
        query.whereEqualTo(Configs.STREAMS_USER_POINTER, currUser);
        query.orderByDescending(Configs.STREAMS_CREATED_AT);
        query.setLimit(10000);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    streamsArray = objects;
                    Configs.hidePD();

                    reloadData();

                    // error in query
                } else {
                    Configs.hidePD();
                    Configs.simpleAlert(e.getMessage(), Account.this);
        }}});
    }







    // MARK: - RELOAD LISTVIEW DATA --------------------------------------------------------
    void reloadData() {
        class ListAdapter extends BaseAdapter {
            private Context context;
            public ListAdapter(Context context) {
                super();
                this.context = context;
            }

            // CONFIGURE CELL
            @Override
            public View getView(final int position, View cell, ViewGroup parent) {
                if (cell == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    assert inflater != null;
                    cell = inflater.inflate(R.layout.cell_stream, null);
                }

                // Get Parse obj
                final ParseObject sObj = streamsArray.get(position);
                final ParseUser currUser = ParseUser.getCurrentUser();

                // Init views
                final ImageView avatarImg = cell.findViewById(R.id.csAvatarImg);
                final ImageView streamImg = cell.findViewById(R.id.csStreamImg);
                final TextView streamTxt = cell.findViewById(R.id.csStreamTxt);
                streamTxt.setTypeface(Configs.titRegular);
                final TextView likesTxt = cell.findViewById(R.id.csLikesTxt);
                likesTxt.setTypeface(Configs.titRegular);
                final TextView commentsTxt = cell.findViewById(R.id.csCommentsTxt);
                commentsTxt.setTypeface(Configs.titRegular);
                final TextView fullnameTxt = cell.findViewById(R.id.csFullnameTxt);
                fullnameTxt.setTypeface(Configs.titSemibold);
                final TextView usernameTimeTxt = cell.findViewById(R.id.csUsernameTimeTxt);
                usernameTimeTxt.setTypeface(Configs.titRegular);
                final Button likeButt = cell.findViewById(R.id.csLikeButt);
                final Button commentsButt = cell.findViewById(R.id.csCommentsButt);
                final Button shareButt = cell.findViewById(R.id.csShareButt);
                final Button deleteButt = cell.findViewById(R.id.csDeleteButt);
                deleteButt.setVisibility(View.VISIBLE);
                final Button statsButt = cell.findViewById(R.id.csStatsButt);
                statsButt.setVisibility(View.VISIBLE);


                // Get userPointer
                sObj.getParseObject(Configs.STREAMS_USER_POINTER).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                    @SuppressLint("SetTextI18n")
                    public void done(final ParseObject userPointer, ParseException e) {

                        // Get Stream image
                        if (sObj.getParseFile(Configs.STREAMS_IMAGE) != null) {
                            Configs.getParseImage(streamImg, sObj, Configs.STREAMS_IMAGE);
                            streamImg.setVisibility(View.VISIBLE);
                            streamImg.getLayoutParams().height = 200;

                            streamImg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(Account.this, StreamDetails.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("objectID", sObj.getObjectId());
                                    i.putExtras(extras);
                                    startActivity(i);
                            }});


                        // No Stream image
                        } else {
                            streamImg.setVisibility(View.INVISIBLE);
                            streamImg.getLayoutParams().height = 1;
                        }


                        // Get Stream text
                        streamTxt.setText(sObj.getString(Configs.STREAMS_TEXT));
                        streamTxt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(Account.this, StreamDetails.class);
                                Bundle extras = new Bundle();
                                extras.putString("objectID", sObj.getObjectId());
                                i.putExtras(extras);
                                startActivity(i);
                            }});



                        // Get likes
                        int likes = sObj.getInt(Configs.STREAMS_LIKES);
                        likesTxt.setText(Configs.roundThousandsIntoK(likes));

                        // Show liked icon
                        List<String>likedBy = sObj.getList(Configs.STREAMS_LIKED_BY);
                        if (likedBy.contains(currUser.getObjectId()) ){
                            likeButt.setBackgroundResource(R.drawable.liked_butt_small);
                        } else {
                            likeButt.setBackgroundResource(R.drawable.like_butt_small);
                        }

                        // Get comments
                        int comments = sObj.getInt(Configs.STREAMS_COMMENTS);
                        commentsTxt.setText(Configs.roundThousandsIntoK(comments));



                        // Get userPointer details
                        Configs.getParseImage(avatarImg, userPointer, Configs.USER_AVATAR);

                        fullnameTxt.setText(userPointer.getString(Configs.USER_FULLNAME));

                        String sDate = Configs.timeAgoSinceDate(sObj.getCreatedAt());
                        usernameTimeTxt.setText("@ " + userPointer.getString(Configs.USER_USERNAME) + " • " + sDate);





                        // MARK: - AVATAR BUTTON ------------------------------------
                        avatarImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(Account.this, OtherUserProfile.class);
                                Bundle extras = new Bundle();
                                extras.putString("userID", userPointer.getObjectId());
                                i.putExtras(extras);
                                startActivity(i);
                            }});




                        // MARK: - LIKE STREAM BUTTON ------------------------------------
                        likeButt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                // Get likedBy
                                List<String>likedBy = sObj.getList(Configs.STREAMS_LIKED_BY);

                                // UNLIKE THIS STREAM
                                if (likedBy.contains(currUser.getObjectId()) ){
                                    likedBy.remove(currUser.getObjectId());
                                    sObj.put(Configs.STREAMS_LIKED_BY, likedBy);
                                    sObj.increment(Configs.STREAMS_LIKES, -1);
                                    sObj.saveInBackground();

                                    likeButt.setBackgroundResource(R.drawable.like_butt_small);
                                    int likes = sObj.getInt(Configs.STREAMS_LIKES);
                                    likesTxt.setText(Configs.roundThousandsIntoK(likes));


                                    // LIKE THIS STREAM
                                } else {
                                    likedBy.add(currUser.getObjectId());
                                    sObj.put(Configs.STREAMS_LIKED_BY, likedBy);
                                    sObj.increment(Configs.STREAMS_LIKES, 1);
                                    sObj.saveInBackground();

                                    likeButt.setBackgroundResource(R.drawable.liked_butt_small);
                                    int likes = sObj.getInt(Configs.STREAMS_LIKES);
                                    likesTxt.setText(Configs.roundThousandsIntoK(likes));

                                    // Send push notification
                                    String pushMessage = currUser.getString(Configs.USER_FULLNAME) + " liked your Stream: '" +
                                            sObj.getString(Configs.STREAMS_TEXT) + "'";
                                    Configs.sendPushNotification(pushMessage, (ParseUser) userPointer, Account.this);

                                    // Save Activity
                                    Configs.saveActivity(currUser, sObj, pushMessage);
                                }

                            }});





                        // MARK: - COMMENTS BUTTON -------------------------------------------
                        commentsButt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                  Intent i = new Intent(Account.this, Comments.class);
                                  Bundle extras = new Bundle();
                                  extras.putString("objectID", sObj.getObjectId());
                                  i.putExtras(extras);
                                  startActivity(i);
                            }});




                        // MARK: - SHARE BUTTON ------------------------------------------------
                        shareButt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!mmp.checkPermissionForWriteExternalStorage()) {
                                    mmp.requestPermissionForWriteExternalStorage();
                                } else {
                                    Bitmap bitmap;
                                    if (sObj.getParseFile(Configs.STREAMS_IMAGE) != null) {
                                        bitmap = ((BitmapDrawable) streamImg.getDrawable()).getBitmap();
                                    } else {
                                        bitmap = BitmapFactory.decodeResource(Account.this.getResources(), R.drawable.logo);
                                    }
                                    Uri uri = Configs.getImageUri(Account.this, bitmap);
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("image/jpeg");
                                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                                    intent.putExtra(Intent.EXTRA_TEXT, sObj.getString(Configs.STREAMS_TEXT) +
                                            " on #" + getString(R.string.app_name));
                                    startActivity(Intent.createChooser(intent, "Share on..."));
                                }


                                // Increment shares amount
                                sObj.increment(Configs.STREAMS_SHARES, 1);
                                sObj.saveInBackground();

                            }});






                        // MARK: - STATISTICS BUTTON ------------------------------------
                        statsButt.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              Intent i = new Intent(Account.this, Statistics.class);
                              Bundle extras = new Bundle();
                              extras.putString("objectID", sObj.getObjectId());
                              i.putExtras(extras);
                              startActivity(i);
                        }});






                        // MARK: - DELETE STREAM BUTTON ------------------------------------------
                        deleteButt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(Account.this);
                                alert.setMessage("Are you sure you want to delete this Stream?")
                                    .setTitle(R.string.app_name)
                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Configs.showPD("Please wait...", Account.this);
                                            sObj.deleteInBackground(new DeleteCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        Configs.hidePD();
                                                        Configs.mustRefresh = true;
                                                        streamsArray.remove(position);
                                                        streamsListView.invalidateViews();
                                                        streamsListView.refreshDrawableState();


                                                        // Delete those rows from the Activity class which have this Stream as a Pointer
                                                        ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.ACTIVITY_CLASS_NAME);
                                                        query.whereEqualTo(Configs.ACTIVITY_STREAM_POINTER, sObj);
                                                        query.findInBackground(new FindCallback<ParseObject>() {
                                                            @Override
                                                            public void done(List<ParseObject> objects, ParseException e) {
                                                                if (e == null) {
                                                                    for (int i = 0; i<objects.size(); i++) {
                                                                        ParseObject aObj = objects.get(i);
                                                                        aObj.deleteInBackground();
                                                                    }
                                                        }}});

                                                    // error on deletion
                                                    } else {
                                                        Configs.hidePD();
                                                        Configs.simpleAlert(e.getMessage(), Account.this);
                                            }}});

                                        }})
                                    .setNegativeButton("Cancel", null)
                                    .setIcon(R.drawable.logo);
                                alert.create().show();


                        }});

                    }});// end userPointer


                return cell;
            }

            @Override public int getCount() { return streamsArray.size(); }
            @Override public Object getItem(int position) { return streamsArray.get(position); }
            @Override public long getItemId(int position) { return position; }
        }


        // Init ListView and set its adapter
        streamsListView.setAdapter(new ListAdapter(Account.this));
    }









    // MARK: - REFRESH DATA ----------------------------------------
    @Override
    public void onRefresh() {
        // Recall query
        queryStreams();

        if (refreshControl.isRefreshing()) {
            refreshControl.setRefreshing(false);
        }
    }


}//@end
