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
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.HashMap;
import java.util.List;

public class OtherUserProfile extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    /* Views */
    ListView streamsListView;
    ImageView avatarImg, coverImg;
    SwipeRefreshLayout refreshControl;
    TextView usernameTxt, fullNameTxt, aboutMeTxt;
    Button followersButt, followingButt, reportUserButt, followButt;


    /* Variables */
    List<ParseObject> streamsArray;
    ParseUser userObj;
    MarshMallowPermission mmp = new MarshMallowPermission(this);




    @Override
    protected void onStart() {
        super.onStart();

        // Get objectID from previous .java
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String objectID = extras.getString("userID");
        userObj = (ParseUser) ParseUser.createWithoutData(Configs.USER_CLASS_NAME, objectID);
        try { userObj.fetchIfNeeded().getParseObject(Configs.USER_CLASS_NAME);

            // Call queries
            showUserDetails();
            getFollowersAndFollowing();

            // Recall query in case something has been reported (either a User or a Stream)
            if (Configs.mustRefresh) {
                queryStreams();
                Configs.mustRefresh = false;
            }

        } catch (ParseException e) { e.printStackTrace(); }

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_user_profile);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Hide ActionBar
        getSupportActionBar().hide();

        // Change StatusBar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));


        // Init views
        avatarImg = findViewById(R.id.oupAvatarImg);
        coverImg = findViewById(R.id.oupCoverImg);
        usernameTxt = findViewById(R.id.oupUsernameTxt);
        usernameTxt.setTypeface(Configs.titSemibold);
        followersButt = findViewById(R.id.oupFollowersButt);
        followersButt.setTypeface(Configs.titRegular);
        followingButt = findViewById(R.id.oupFollowingButt);
        followingButt.setTypeface(Configs.titRegular);
        fullNameTxt = findViewById(R.id.oupFullnameTxt);
        fullNameTxt.setTypeface(Configs.titBlack);
        aboutMeTxt = findViewById(R.id.oupAboutMeTxt);
        aboutMeTxt.setTypeface(Configs.titRegular);
        streamsListView = findViewById(R.id.oupStreamsListView);
        reportUserButt = findViewById(R.id.oupReportUserButt);
        followButt = findViewById(R.id.oupFollowButt);

        // Init a refreshControl
        refreshControl = findViewById(R.id.swiperefresh);
        refreshControl.setOnRefreshListener(this);



        // Get objectID from previous .java
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String objectID = extras.getString("userID");
        userObj = (ParseUser) ParseUser.createWithoutData(Configs.USER_CLASS_NAME, objectID);
        try { userObj.fetchIfNeeded().getParseObject(Configs.USER_CLASS_NAME);


            // Call query
            queryStreams();





            // MARK: - MESSAGE BUTTON ------------------------------------
            Button messageButt = findViewById(R.id.oupMessageButt);
            messageButt.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  ParseUser currUser = ParseUser.getCurrentUser();
                  List<String>blockedUsers = userObj.getList(Configs.USER_HAS_BLOCKED);

                  // THIS USER HAS BLOCKED YOU!
                  if (blockedUsers.contains(currUser.getObjectId()) ){
                      Configs.simpleAlert("Sorry, @" + userObj.getString(Configs.USER_USERNAME) +
                              " has blocked you. You can't chat with this user.", OtherUserProfile.this);

                  // YOU CAN CHAT WITH THIS USER
                  } else {
                      Intent i = new Intent(OtherUserProfile.this, InboxActivity.class);
                      Bundle extras = new Bundle();
                      extras.putString("userID", userObj.getObjectId());
                      i.putExtras(extras);
                      startActivity(i);
                  }
            }});



        } catch (ParseException e) { e.printStackTrace(); }





        // MARK: - BACK BUTTON ------------------------------------
        Button backButt = findViewById(R.id.oupBackButt);
        backButt.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) { finish(); }});


    }// end onCreate()






    // MARK: - GET FOLLOWERS AND FOLLOWING AMOUNT -------------------------------------------------
    void getFollowersAndFollowing() {
        final ParseUser currUser = ParseUser.getCurrentUser();

        // QUERY FOLLOWERS
        final ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.FOLLOW_CLASS_NAME);
        query.whereEqualTo(Configs.FOLLOW_IS_FOLLOWING, userObj);
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
                    query2.whereEqualTo(Configs.FOLLOW_CURR_USER, userObj);
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
        final ParseUser currUser = ParseUser.getCurrentUser();

        // Get username
        usernameTxt.setText("@" + userObj.getString(Configs.USER_USERNAME));
        // Get fullName
        fullNameTxt.setText(userObj.getString(Configs.USER_FULLNAME));

        // Get aboutMe
        if (userObj.getString(Configs.USER_ABOUT_ME) != null ){
            aboutMeTxt.setText(userObj.getString(Configs.USER_ABOUT_ME));
        } else { aboutMeTxt.setText(""); }

        // Get avatar
        Configs.getParseImage(avatarImg, userObj, Configs.USER_AVATAR);

        // Get cover
        if (userObj.getParseFile(Configs.USER_COVER_IMAGE) != null){
            Configs.getParseImage(coverImg, userObj, Configs.USER_COVER_IMAGE);
        } else { coverImg.setImageDrawable(null); }


        if (userObj.getObjectId() != currUser.getObjectId()) {

            // Set Follow Button
            ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.FOLLOW_CLASS_NAME);
            query.whereEqualTo(Configs.FOLLOW_CURR_USER, currUser);
            query.whereEqualTo(Configs.FOLLOW_IS_FOLLOWING, userObj);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {

                        // You're following this user
                        if (objects.size() != 0) {
                            followButt.setText("Following");
                            followButt.setBackgroundResource(R.drawable.rounded_button);
                            followButt.setTextColor(Color.WHITE);

                            // You're not following this user
                        } else {
                            followButt.setText("Follow");
                            followButt.setBackgroundResource(R.drawable.rounded_button_empty);
                            followButt.setTextColor(Color.parseColor(Configs.MAIN_COLOR));
                        }

                    } else {
                        Configs.simpleAlert(e.getMessage(), OtherUserProfile.this);
                    }
                }
            });


        // HIDE FOLLOW BUTTON
        } else { followButt.setVisibility(View.INVISIBLE); }






        // MARK: - FOLLOWING BUTTON ------------------------------------
        followingButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OtherUserProfile.this, Follow.class);
                Bundle extras = new Bundle();
                extras.putString("isFollowing", "true");
                extras.putString("userID", userObj.getObjectId());
                i.putExtras(extras);
                startActivity(i);
        }});



        // MARK: - FOLLOWERS BUTTON ------------------------------------
        followersButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OtherUserProfile.this, Follow.class);
                Bundle extras = new Bundle();
                extras.putString("isFollowing", "false");
                extras.putString("userID", userObj.getObjectId());
                i.putExtras(extras);
                startActivity(i);
        }});



        // MARK: - FOLLOW/UNFOLLOW THIS USER BUTTON ------------------------------------
        followButt.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Configs.showPD("Please wait...", OtherUserProfile.this);
              ParseUser currUser = ParseUser.getCurrentUser();

              // FOLLOW USER --------------------------------
              if (followButt.getText().toString().matches("Follow")) {
                  ParseObject fObj = new ParseObject(Configs.FOLLOW_CLASS_NAME);

                  // Save data
                  fObj.put(Configs.FOLLOW_CURR_USER, currUser);
                  fObj.put(Configs.FOLLOW_IS_FOLLOWING, userObj);

                  // Saving block
                  fObj.saveInBackground(new SaveCallback() {
                      @Override
                      public void done(ParseException e) {
                          if (e == null) {
                              Configs.hidePD();

                              followButt.setText("Following");
                              followButt.setBackgroundResource(R.drawable.rounded_button);
                              followButt.setTextColor(Color.WHITE);

                              // error
                          } else {
                              Configs.hidePD();
                              Configs.simpleAlert(e.getMessage(), OtherUserProfile.this);
                          }
                      }
                  });


              // UNFOLLOW USER ---------------------------------------
              } else {
                  ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.FOLLOW_CLASS_NAME);
                  query.whereEqualTo(Configs.FOLLOW_CURR_USER, currUser);
                  query.whereEqualTo(Configs.FOLLOW_IS_FOLLOWING, userObj);
                  query.findInBackground(new FindCallback<ParseObject>() {
                      @Override
                      public void done(List<ParseObject> objects, ParseException e) {
                          if (e == null) {
                              ParseObject fObj = objects.get(0);
                              fObj.deleteInBackground(new DeleteCallback() {
                                  @Override
                                  public void done(ParseException e) {
                                      Configs.hidePD();
                                      followButt.setText("Follow");
                                      followButt.setBackgroundResource(R.drawable.rounded_button_empty);
                                      followButt.setTextColor(Color.parseColor(Configs.MAIN_COLOR));
                              }});

                          } else {
                              Configs.hidePD();
                              Configs.simpleAlert(e.getMessage(), OtherUserProfile.this);
                  }}});
              }

        }});






        // MARK: - REPORT USER BUTTON --------------------------------------------------------
        reportUserButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(OtherUserProfile.this);

                alert.setMessage("Are you sure you want to report @" + userObj.getString(Configs.USER_USERNAME) + " to the Admin?")
                .setTitle(R.string.app_name)
                .setIcon(R.drawable.logo)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Configs.showPD("Reporting User...", OtherUserProfile.this);

                        // Report user via Cloud Code
                        HashMap<String, Object> params = new HashMap<String, Object>();
                        params.put("userId", userObj.getObjectId());
                        params.put("reportMessage", "OFFENSIVE USER");

                        ParseCloud.callFunctionInBackground("reportUser", params, new FunctionCallback<ParseUser>() {
                            public void done(ParseUser user, ParseException error) {
                                if (error == null) {
                                    Configs.hidePD();
                                    Configs.mustRefresh = true;

                                    // Automatically report all User's streams
                                    ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.STREAMS_CLASS_NAME);
                                    query.whereEqualTo(Configs.STREAMS_USER_POINTER, userObj);
                                    query.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> objects, ParseException e) {
                                            if (e == null) {
                                                for (int i = 0; i<objects.size(); i++){
                                                    ParseObject stObj = objects.get(i);
                                                    List<String>reportedBy = stObj.getList(Configs.STREAMS_REPORTED_BY);
                                                    reportedBy.add(currUser.getObjectId());
                                                    stObj.put(Configs.STREAMS_REPORTED_BY, reportedBy);
                                                    stObj.saveInBackground();
                                                }
                                    }}});


                                    // Automatically report all User's comments
                                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery(Configs.COMMENTS_CLASS_NAME);
                                    query2.whereEqualTo(Configs.COMMENTS_USER_POINTER, userObj);
                                    query2.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> objects, ParseException e) {
                                            if (e == null) {
                                                for (int i = 0; i<objects.size(); i++){
                                                    ParseObject commObj = objects.get(i);
                                                    List<String>reportedBy = commObj.getList(Configs.COMMENTS_REPORTED_BY);
                                                    reportedBy.add(currUser.getObjectId());
                                                    commObj.put(Configs.COMMENTS_REPORTED_BY, reportedBy);
                                                    commObj.saveInBackground();
                                            }
                                    }}});



                                    // Show Alert
                                    AlertDialog.Builder alert = new AlertDialog.Builder(OtherUserProfile.this);
                                    alert.setMessage("Thanks for reporting " + userObj.getString(Configs.USER_FULLNAME) +
                                            ". We'll check it out within 24h.")
                                    .setTitle(R.string.app_name)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                    }})
                                    .setIcon(R.drawable.logo);
                                    alert.create().show();


                                // Error in Cloud Code
                                } else {
                                    Configs.hidePD();
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                        }});


                }});
                alert.create().show();


        }});

    }







    // MARK: - QUERY STREAMS -------------------------------------------------
    void queryStreams() {
        Configs.showPD("Please wait...", OtherUserProfile.this);
        ParseUser currUser = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.STREAMS_CLASS_NAME);
        query.whereEqualTo(Configs.STREAMS_USER_POINTER, userObj);
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
                    Configs.simpleAlert(e.getMessage(), OtherUserProfile.this);
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
            @SuppressLint("SetTextI18n")
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



                // Get Stream image
                if (sObj.getParseFile(Configs.STREAMS_IMAGE) != null) {
                    Configs.getParseImage(streamImg, sObj, Configs.STREAMS_IMAGE);
                    streamImg.setVisibility(View.VISIBLE);
                    streamImg.getLayoutParams().height = 200;

                    streamImg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(OtherUserProfile.this, StreamDetails.class);
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
                                Intent i = new Intent(OtherUserProfile.this, StreamDetails.class);
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



                // Get userObj details
                Configs.getParseImage(avatarImg, userObj, Configs.USER_AVATAR);

                fullnameTxt.setText(userObj.getString(Configs.USER_FULLNAME));

                String sDate = Configs.timeAgoSinceDate(sObj.getCreatedAt());
                usernameTimeTxt.setText("@ " + userObj.getString(Configs.USER_USERNAME) + " • " + sDate);





                // MARK: - AVATAR BUTTON ------------------------------------
                avatarImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(OtherUserProfile.this, OtherUserProfile.class);
                                Bundle extras = new Bundle();
                                extras.putString("userID", userObj.getObjectId());
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
                                    Configs.sendPushNotification(pushMessage, (ParseUser) userObj, OtherUserProfile.this);

                                    // Save Activity
                                    Configs.saveActivity(currUser, sObj, pushMessage);
                                }

                            }});





                        // MARK: - COMMENTS BUTTON -------------------------------------------
                        commentsButt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(OtherUserProfile.this, Comments.class);
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
                                        bitmap = BitmapFactory.decodeResource(OtherUserProfile.this.getResources(), R.drawable.logo);
                                    }
                                    Uri uri = Configs.getImageUri(OtherUserProfile.this, bitmap);
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


            return cell;
            }

            @Override public int getCount() { return streamsArray.size(); }
            @Override public Object getItem(int position) { return streamsArray.get(position); }
            @Override public long getItemId(int position) { return position; }
        }


        // Init ListView and set its adapter
        streamsListView.setAdapter(new ListAdapter(OtherUserProfile.this));
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
