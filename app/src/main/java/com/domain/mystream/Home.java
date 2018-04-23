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
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity  {

    /*
     */
    /* Views *//*

    ListView streamsListView;
    ImageView currUserAvatarImg;
    SwipeRefreshLayout refreshControl;



    */
    /* Variables *//*

    List<ParseObject> streamsArray;
    MarshMallowPermission mmp = new MarshMallowPermission(this);




    // ON START() ---------------------------------------------
    @Override
    protected void onStart() {
        super.onStart();

        // Open Intro at startup
    */
/*    if (ParseUser.getCurrentUser().getUsername() == null) {
            startActivity(new Intent(Home.this, Intro.class));

        // USER IS LOGGED IN...
        } else {*//*

            ParseUser currUser = ParseUser.getCurrentUser();


            // Register GCM Sender ID in Inatllation class
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();

            // IMPORTANT: REPLACE "589147079404" WITH YOUR OWN GCM SENDER ID
            installation.put("GCMSenderId", "589147079404");

          */
/*  installation.put("userID", ParseUser.getCurrentUser().getObjectId());
            installation.put("username", ParseUser.getCurrentUser().getUsername());*//*

            installation.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Log.i("log-", "REGISTERED FOR PUSH NOTIFICATIONS!");
            }});



            // Get user's avatar
            Configs.getParseImage(currUserAvatarImg, currUser, Configs.USER_AVATAR);

            // Recall query in case something has been reported (either a User or a Stream)
            if (Configs.mustRefresh) {
                queryStreams();
                Configs.mustRefresh = false;
            }

      //  }



        // Request Storage permission
        if(!mmp.checkPermissionForReadExternalStorage()) {
            mmp.requestPermissionForReadExternalStorage();
        }
    }

*/


    // ON CREATE() -----------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Hide ActionBar
        getSupportActionBar().hide();

        // Change StatusBar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));


        // Init views
     /*   currUserAvatarImg = findViewById(R.id.hcurrUserAvatarImg);
        streamsListView = findViewById(R.id.hStreamsListView);

        // Init a refreshControl
        refreshControl = findViewById(R.id.swiperefresh);
        refreshControl.setOnRefreshListener(this);
*/

     FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, StreamFragment.newInstance());
        transaction.commit();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        selectedFragment = StreamFragment.newInstance();
                        break;
                  /*  case R.id.action_item2:
                        selectedFragment = ItemTwoFragment.newInstance();
                        break;
                    case R.id.action_item3:
                        selectedFragment = ItemThreeFragment.newInstance();
                        break;*/
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
                return true;
            }

        });

        // Init TabBar buttons
/*
        Button tab_one = findViewById(R.id.tab_two);
        Button tab_two = findViewById(R.id.tab_three);
        Button tab_three = findViewById(R.id.tab_four);
        Button tab_four = findViewById(R.id.tab_five);


        tab_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Search.class));
                overridePendingTransition(0, 0);
        }});

        tab_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Following.class));
                overridePendingTransition(0, 0);
        }});


        tab_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Account.class));
                overridePendingTransition(0, 0);
        }});

        tab_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Messages.class));
                overridePendingTransition(0, 0);
            }});

*/

        // Call query
      /*  if (ParseUser.getCurrentUser().getObjectId() != null) { queryStreams(); }




        // MARK: - ADD STREAM BUTTON ------------------------------------
        Button addStreamButt = findViewById(R.id.hAddStreamButt);
        addStreamButt.setTypeface(Configs.titRegular);
        addStreamButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, AddStream.class));
        }});



        // MARK: - ADD PHOTO BUTTON ------------------------------------
        Button addPhotoButt = findViewById(R.id.hAddPhotoButt);
        addPhotoButt.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if(!mmp.checkPermissionForReadExternalStorage()) {
                  mmp.requestPermissionForReadExternalStorage();
              } else {
                  Intent i = new Intent(Home.this, AddStream.class);
                  Bundle extras = new Bundle();
                  extras.putString("streamAttachment", "image");
                  i.putExtras(extras);
                  startActivity(i);
              }
        }});




        // MARK: - ADD VIDEO BUTTON ------------------------------------
        Button addVideoButt = findViewById(R.id.hAddVideoButt);
        addVideoButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mmp.checkPermissionForCamera()) {
                    mmp.requestPermissionForCamera();
                } else {
                    Intent i = new Intent(Home.this, AddStream.class);
                    Bundle extras = new Bundle();
                    extras.putString("streamAttachment", "video");
                    i.putExtras(extras);
                    startActivity(i);
                }
        }});



        // MARK: - ADD AUDIO BUTTON ------------------------------------
        Button addAudioButt = findViewById(R.id.hAddAudioButt);
        addAudioButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mmp.checkPermissionForRecord()) {
                    mmp.requestPermissionForRecord();
                } else {
                    Intent i = new Intent(Home.this, AddStream.class);
                    Bundle extras = new Bundle();
                    extras.putString("streamAttachment", "audio");
                    i.putExtras(extras);
                    startActivity(i);
                }
        }});






        // INTERSTITIAL AD IMPLEMENTATION ------------------------------------
       *//* final InterstitialAd interstitialAd = new InterstitialAd(this);
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

*//*
    }// end onCreate()







    // MARK: - QUERY STREAMS -------------------------------------------------
    void queryStreams() {
        Configs.showPD("Please wait...", Home.this);
        ParseUser currUser = ParseUser.getCurrentUser();
        List<String>currUserID = new ArrayList<>();
        currUserID.add(currUser.getObjectId());

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.STREAMS_CLASS_NAME);
        query.whereNotContainedIn(Configs.STREAMS_REPORTED_BY, currUserID);
        query.orderByDescending(Configs.STREAMS_CREATED_AT);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    streamsArray = objects;
                    Configs.hidePD();

                    reloadData();

                // error in query
                } else {
                    Configs.hidePD();
                    Configs.simpleAlert(e.getMessage(), Home.this);
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
                                    Intent i = new Intent(Home.this, StreamDetails.class);
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
                                Intent i = new Intent(Home.this, StreamDetails.class);
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
                                Intent i = new Intent(Home.this, OtherUserProfile.class);
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
                                    Configs.sendPushNotification(pushMessage, (ParseUser) userPointer, Home.this);

                                    // Save Activity
                                    Configs.saveActivity(currUser, sObj, pushMessage);
                                }

                            }});





                        // MARK: - COMMENTS BUTTON ------------------------------------
                        commentsButt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                  Intent i = new Intent(Home.this, Comments.class);
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
                                        bitmap = BitmapFactory.decodeResource(Home.this.getResources(), R.drawable.logo);
                                    }
                                    Uri uri = Configs.getImageUri(Home.this, bitmap);
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
        streamsListView.setAdapter(new ListAdapter(Home.this));
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





    private void getPost() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://app_api_json.veamex.com/api/common/GetPosts?userId=1&&currentUserId=1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_LONG).show();
                //  hideProgress(getActivity());


                try {
                    Configs.hidePD();
                    JSONObject rs = new JSONObject(response);
                    String userId = rs.optString("UserId");
                    String userName=rs.optString("UserName");

                    if (!userName.equals("null")) {

                        // Go to Home screen
                        //startActivity(new Intent(Login.this, Home.class));


                        // error
                    } else {
                        Configs.simpleAlert("Not a valid user", Home.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Configs.hidePD();
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject jsonObject = new JSONObject(responseBody);
                } catch (Exception e) {
                    //Handle a malformed json response

                }
                Toast.makeText(Home.this, "Server error or No internet connection", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };
        // showProgress(getActivity(), "Loading....", "Please wait!");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
*/
    }
}//@end
