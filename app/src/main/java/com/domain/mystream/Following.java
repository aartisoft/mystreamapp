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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Following extends AppCompatActivity {


    /* Views */
    ListView streamsListView;
    TextView noStreamsTxt;



    /* Variables */
    MarshMallowPermission mmp = new MarshMallowPermission(this);
    List<ParseObject> streamsArray;
    List<ParseObject>followArray;





    @Override
    protected void onStart() {
        super.onStart();
        // Recall query in case something has been reported (either a User or a Stream)
        if (Configs.mustRefresh) {
            queryStreamsOfFollowing();
            Configs.mustRefresh = false;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.following);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Hide ActionBar
        getSupportActionBar().hide();

        // Change StatusBar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));


        // Init views
        streamsListView = findViewById(R.id.fingStreamsListView);
        noStreamsTxt = findViewById(R.id.fingNoStreamsTxt);
        noStreamsTxt.setTypeface(Configs.titRegular);


        // Init TabBar buttons
        Button tab_one = findViewById(R.id.tab_one);
        Button tab_two = findViewById(R.id.tab_two);
        Button tab_three = findViewById(R.id.tab_four);
        Button tab_four = findViewById(R.id.tab_five);

        tab_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Following.this, Home.class));
                overridePendingTransition(0, 0);
            }});

        tab_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Following.this, Search.class));
                overridePendingTransition(0, 0);
            }});


        tab_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Following.this, Account.class));
                overridePendingTransition(0, 0);
            }});

        tab_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Following.this, Messages.class));
                overridePendingTransition(0, 0);
            }});




        // Call query
        if (ParseUser.getCurrentUser().getObjectId() != null) { queryStreamsOfFollowing(); }




        // MARK: - REFRESH BUTTON ------------------------------------
        Button refreshButt = findViewById(R.id.fingRefreshButt);
        refreshButt.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if (streamsArray != null) {
                  // Recall query
                  queryStreamsOfFollowing();
              }
        }});





        // INTERSTITIAL AD IMPLEMENTATION ------------------------------------
        final InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.ADMOB_INTERSTITIAL_UNIT_ID));
        AdRequest requestForInterstitial = new AdRequest.Builder().build();
        interstitialAd.loadAd(requestForInterstitial);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.i("log-", "INTERSTITIAL is loaded!");
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
        }}});

    }// end onCreate()






    // MARK: - QUERY STREAMS OF FOLLOWING -------------------------------------------------------
    void queryStreamsOfFollowing() {
        Configs.showPD("Please wait..", Following.this);
        ParseUser currUser = ParseUser.getCurrentUser();
        final List<String>currUserID = new ArrayList<>();
        currUserID.add(currUser.getObjectId());
        streamsArray = new ArrayList<>();

        ParseQuery query = ParseQuery.getQuery(Configs.FOLLOW_CLASS_NAME);
        query.whereEqualTo(Configs.FOLLOW_CURR_USER, currUser);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException error) {
                if (error == null) {
                    followArray = objects;

                    // You're following someone:
                    if (followArray.size() != 0) {
                        for (int i = 0; i<followArray.size(); i++) {
                            ParseObject fObj = followArray.get(i);

                            // Show streamsListView
                            noStreamsTxt.setVisibility(View.INVISIBLE);
                            streamsListView.setVisibility(View.VISIBLE);


                            // Get userPointer
                            fObj.getParseObject(Configs.FOLLOW_IS_FOLLOWING).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                                public void done(ParseObject userPointer, ParseException e) {
                                    if (!userPointer.getBoolean(Configs.USER_IS_REPORTED)) {

                                        // Query Streams
                                        ParseQuery query = ParseQuery.getQuery(Configs.STREAMS_CLASS_NAME);
                                        query.whereEqualTo(Configs.STREAMS_USER_POINTER, userPointer);
                                        query.whereNotContainedIn(Configs.STREAMS_REPORTED_BY, currUserID);
                                        query.orderByDescending(Configs.STREAMS_CREATED_AT);
                                        query.findInBackground(new FindCallback<ParseObject>() {
                                            public void done(List<ParseObject> objects, ParseException error) {
                                                if (error == null) {
                                                    if (objects != null){
                                                        streamsArray.addAll(objects);
                                                    }
                                                    Configs.hidePD();
                                                    reloadData();


                                                // Error in query
                                                } else {
                                                    Configs.hidePD();
                                                    Configs.simpleAlert(error.getMessage(), Following.this);
                                        }}});

                                    }
                                }});

                        } // end FOR loop




                    // No following: Show noStreamsTxt and hide streamsListView
                    } else {
                        Configs.hidePD();
                        noStreamsTxt.setVisibility(View.VISIBLE);
                        streamsListView.setVisibility(View.INVISIBLE);

                    }


                    // Error in query Following
                } else {
                    Configs.hidePD();
                    Configs.simpleAlert(error.getMessage(), Following.this);
            }}});

    }





    // MARK: - RELOAD LISTVIEW DATA --------------------------------------------------------
    void reloadData() {
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
                                    Intent i = new Intent(Following.this, StreamDetails.class);
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
                                Intent i = new Intent(Following.this, StreamDetails.class);
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
                                Intent i = new Intent(Following.this, OtherUserProfile.class);
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
                                    Configs.sendPushNotification(pushMessage, (ParseUser) userPointer, Following.this);

                                    // Save Activity
                                    Configs.saveActivity(currUser, sObj, pushMessage);
                                }

                            }});





                        // MARK: - COMMENTS BUTTON ------------------------------------
                        commentsButt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                  Intent i = new Intent(Following.this, Comments.class);
                                  Bundle extras = new Bundle();
                                  extras.putString("objectID", sObj.getObjectId());
                                  i.putExtras(extras);
                                  startActivity(i);
                            }});




                        // MARK: - SHARE BUTTON ------------------------------------
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
                                        bitmap = BitmapFactory.decodeResource(Following.this.getResources(), R.drawable.logo);
                                    }
                                    Uri uri = Configs.getImageUri(Following.this, bitmap);
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



                    }});// end userPointer


                return cell;
            }

            @Override public int getCount() { return streamsArray.size(); }
            @Override public Object getItem(int position) { return streamsArray.get(position); }
            @Override public long getItemId(int position) { return position; }
        }


        // Init ListView and set its adapter
        streamsListView.setAdapter(new ListAdapter(Following.this, streamsArray));
    }





}// @end
