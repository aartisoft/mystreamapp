package com.domain.mystream;

/* ================================

    - MyStream -

    created by cubycode @2018
    All Rights reserved

===================================*/

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.domain.mystream.Adpater.CellStreamPostAdpater;
import com.domain.mystream.Adpater.MyStreamPostAdpater;
import com.domain.mystream.Model.Comment;
import com.domain.mystream.Model.Comments;
import com.domain.mystream.Model.Company;
import com.domain.mystream.Model.Likes;
import com.domain.mystream.Model.PostModel;
import com.domain.mystream.Model.PostType;
import com.domain.mystream.Model.User;
import com.parse.CountCallback;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.domain.mystream.Login.myPref;
import static com.domain.mystream.StreamDetails.hideProgress;
import static com.domain.mystream.StreamDetails.showProgress;

public class Account extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    /* Views */
    RecyclerView streamsListView;
    ImageView avatarImg, coverImg;
    SwipeRefreshLayout refreshControl;
    TextView usernameTxt, fullNameTxt, aboutMeTxt;
    Button followersButt, followingButt,settButt,addStreamButt;
    String uasername,fullname;

    List<PostModel> postModelList;
    String userid;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    /* Variables */
    List<ParseObject> streamsArray;
    MarshMallowPermission mmp = new MarshMallowPermission(getActivity());

    public static Account newInstance() {
        Account fragment = new Account();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account, container, false);




        // Hide ActionBar
     //   getActivity().getSupportActionBar().hide();

        // Change StatusBar color
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(myPref, Context.MODE_PRIVATE);
        uasername= sharedPreferences.getString("username",null);
        userid = sharedPreferences.getString("userid", "0");



        // Init views
        avatarImg =view. findViewById(R.id.accAvatarImg);
        coverImg = view. findViewById(R.id.accCoverImg);
        usernameTxt =view.  findViewById(R.id.accUsernameTxt);
        usernameTxt.setTypeface(Configs.titSemibold);
        usernameTxt.setText(uasername);
        followersButt =view.  findViewById(R.id.accFollowersButt);
        followersButt.setTypeface(Configs.titRegular);
        followingButt = view. findViewById(R.id.accFollowingButt);
        followingButt.setTypeface(Configs.titRegular);
        fullNameTxt = view. findViewById(R.id.accFullnameTxt);
        fullNameTxt.setTypeface(Configs.titBlack);
        fullNameTxt.setText(fullname);
        aboutMeTxt = view. findViewById(R.id.accAboutMeTxt);
        aboutMeTxt.setTypeface(Configs.titRegular);
        streamsListView = view. findViewById(R.id.accStreamsListView1);
        LinearLayoutManager llmm = new LinearLayoutManager(getActivity());
        llmm.setOrientation(LinearLayoutManager.VERTICAL);
        streamsListView.setLayoutManager(llmm);
        streamsListView.setItemAnimator(null);



        settButt = view.findViewById(R.id.accSettingsButt);



        // Init a refreshControl
        refreshControl =view.  findViewById(R.id.swiperefresh);
        refreshControl.setOnRefreshListener(this);

        settButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Settings.class);
                startActivity(intent);
            }
        });
        addStreamButt = view.findViewById(R.id.accAddStreamButt);
        addStreamButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddStream.class));
            }});

        sharedPreferences = getActivity().getSharedPreferences(myPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Init TabBar buttons
     /*   Button tab_one = findViewById(R.id.tab_one);
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



*/
        getPost();
                return view;
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
        Configs.showPD("Please wait...", getActivity());
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

                    //reloadData();

                    // error in query
                } else {
                    Configs.hidePD();
                    Configs.simpleAlert(e.getMessage(), getActivity());
        }}});
    }







    // MARK: - RELOAD LISTVIEW DATA --------------------------------------------------------
 /*   void reloadData() {
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
        streamsListView.setAdapter(new ListAdapter(Account.this));*//*
    }*/



    private void getPost() {
 postModelList=new ArrayList<PostModel>();
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, "https://app_api_json.veamex.com/api/common/GetPosts?userId=1&currentUserId=2", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_LONG).show();
                //  hideProgress(getActivity());

                hideProgress(getActivity());


                try {


                    // Configs.hidePD();
                    JSONArray rs = new JSONArray(response);
                    for (int i = 0; i < rs.length(); i++) {
                        JSONObject object = rs.getJSONObject(i);
                        PostModel postModel = new PostModel();

                        postModel.setPostId(object.getString("PostId"));
                        postModel.setPostName(object.getString("PostName"));
                        postModel.setPostBody(object.getString("PostBody"));
                        postModel.setPostTypeId(object.getString("PostTypeId"));
                        postModel.setCreatedByUserId(object.getString("CreatedByUserId"));
                        postModel.setCreatedOnDate(object.getString("CreatedOnDate"));
                        postModel.setLastUpdatedOnDate(object.getString("LastUpdatedOnDate"));
                        postModel.setLastUpdatedByUserId(object.getString("LastUpdatedByUserId"));


                        JSONObject object1 = new JSONObject(object.getString("PostType"));

                        PostType postType = new PostType();
                        postType.setPostType1(object1.getString("PostType1"));
                        postType.setSystemId(object1.getString("SystemId"));
                        postType.setPostTypeId(object1.getString("PostTypeId"));

                        postModel.setPostType(postType);
                        JSONObject object2 = new JSONObject(object.getString("User"));

                        User user = new User();
                        user.setUserId(object2.getString("UserId"));
                        user.setUserType(object2.getString("UserType"));
                        user.setAddressId(object2.getString("AddressId"));
                        user.setUserName(object2.getString("UserName"));
                        user.setPassWord(object2.getString("PassWord"));
                        user.setFirstName(object2.getString("FirstName"));
                        user.setMiddleName(object2.getString("MiddleName"));
                        user.setLastName(object2.getString("LastName"));
                        user.setEmail(object2.getString("Email"));
                        user.setGender(object2.getString("Gender"));
                        user.setDateOfBirth(object2.getString("DateOfBirth"));
                        user.setAbout(object2.getString("About"));
                        user.setWebsiteUrl(object2.getString("WebsiteUrl"));
                        user.setAmbition(object2.getString("Ambition"));
                        user.setHobbies(object2.getString("Hobbies"));
                        user.setPosition(object2.getString("Position"));
                        user.setEducation(object2.getString("Education"));
                        user.setWorkExperience(object2.getString("WorkExperience"));
                        user.setWorkDomain(object2.getString("WorkDomain"));
                        user.setDateFormate(object2.getString("DateFormate"));
                        user.setLanguageCode(object2.getString("LanguageCode"));
                        user.setTimezone(object2.getString("Timezone"));
                        user.setShowAllTimezone(object2.getString("ShowAllTimezone"));
                        user.setProfilePic(object2.getString("ProfilePic"));
                        user.setCreatedOnDate(object2.getString("CreatedOnDate"));
                        user.setCreatedByUserId(object2.getString("CreatedByUserId"));
                        user.setLastUpdatedOnDate(object2.getString("LastUpdatedOnDate"));
                        user.setLastUpdatedByUserId(object2.getString("LastUpdatedByUserId"));
                        user.setCompanyId(object2.getString("CompanyId"));
                        user.setPhoneNumber(object2.getString("PhoneNumber"));
                        user.setResidence(object2.getString("Residence"));
                        user.setBranch(object2.getString("Branch"));
                        user.setCustom3(object2.getString("Custom3"));
                        user.setCustom4(object2.getString("Custom4"));
                        user.setTemplateColorThemeId(object2.getString("TemplateColorThemeId"));
                        user.setProfilePic(object2.getString("ProfilePic"));
                        postModel.setUser(user);


                        JSONArray array = object.getJSONArray("Likes");
                        List<Likes> likesList = new ArrayList<>();
                        if (array != null) {
                            for (int k = 0; k < array.length(); k++) {
                                JSONObject objectLike = array.getJSONObject(k);
                                Likes likes = new Likes();
                                likes.setInteractionByUserId(objectLike.getString("InteractionByUserId"));
                                likes.setPostTypeId(objectLike.getString("PostTypeId"));
                                likes.setReferenceId(objectLike.getString("ReferenceId"));
                                likes.setReactionTypeId(objectLike.getString("ReactionTypeId"));
                                if (userid.equals(objectLike.getString("InteractionByUserId"))) {
                                    postModel.setLikebyme(true);
                                }
                                likesList.add(likes);

                            }
                        }
                        postModel.setLikes(likesList);

                        JSONArray comment = object.getJSONArray("Comments");
                        List<com.domain.mystream.Model.Comments> commentsList = new ArrayList<>();
                        if (comment != null) {
                            for (int j = 0; j < comment.length(); j++) {

                                JSONObject objectComment = comment.getJSONObject(j);
                                com.domain.mystream.Model.Comments comments = new Comments();
                                comments.setReferenceTypeId(objectComment.getString("ReferenceTypeId"));
                                comments.setReferenceId(objectComment.getString("ReferenceId"));
                                comments.setCommentId(objectComment.getString("CommentId"));
                                JSONObject commenttext = new JSONObject(objectComment.getString("Comment"));
                                Comment comment2 = new Comment();
                                comment2.setParentCommentId(commenttext.getString("ParentCommentId"));
                                comment2.setText(commenttext.getString("Text"));
                                comments.setComment(comment2);
                            }
                        }
                        postModel.setComments(commentsList);

                        JSONObject object3 = new JSONObject(object2.getString("Company"));
                        Company company = new Company();
                        company.setCompanyId(object3.getString("CompanyId"));
                        company.setParentCompanyId(object3.getString("ParentCompanyId"));
                        company.setCompanyName(object3.getString("CompanyName"));
                        company.setDescription(object3.getString("Description"));
                        company.setEmailAddress(object3.getString("EmailAddress"));
                        company.setFaxnumber(object3.getString("Faxnumber"));
                        company.setPhoneNumber(object3.getString("PhoneNumber"));
                        company.setWebsiteUrl(object3.getString("WebsiteUrl"));
                        company.setLanguageCode(object3.getString("LanguageCode"));
                        company.setDateFormat(object3.getString("DateFormat"));
                        company.setTimeZone(object3.getString("TimeZone"));
                        company.setIsShowAllTimeZone(object3.getString("IsShowAllTimeZone"));
                        company.setSalesforceInTimeline(object3.getString("SalesforceInTimeline"));
                        company.setSalesForceDataImport(object3.getString("SalesForceDataImport"));
                        company.setCreatedOnDate(object3.getString("CreatedOnDate"));
                        company.setCreatedByUserId(object3.getString("CreatedByUserId"));
                        company.setLastUpdatedOnDate(object3.getString("LastUpdatedOnDate"));
                        company.setLastUpdatedByUserId(object3.getString("LastUpdatedByUserId"));
                        company.setThemeName(object3.getString("ThemeName"));
                        company.setCompanyTypeId(object3.getString("CompanyTypeId"));
                        company.setTemplateId(object3.getString("TemplateId"));
                        company.setSystemId(object3.getString("SystemId"));
                        postModel.setCompany(company);
                        if (postModel.getUser().getUserId().equals("1"))
                        postModelList.add(postModel);
                    }



                    MyStreamPostAdpater myStreamPostAdpater = new MyStreamPostAdpater(getActivity(), postModelList);
                    streamsListView.setAdapter(myStreamPostAdpater);
                    myStreamPostAdpater.notifyDataSetChanged();

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
                Toast.makeText(getActivity(), "Server error or No internet connection", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };
     showProgress(getActivity(), "Loading....", "Please wait!");
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }





    // MARK: - REFRESH DATA ----------------------------------------
    @Override
    public void onRefresh() {
        // Recall query
      //  queryStreams();
//getPost();
        if (refreshControl.isRefreshing()) {
            refreshControl.setRefreshing(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}//@end
