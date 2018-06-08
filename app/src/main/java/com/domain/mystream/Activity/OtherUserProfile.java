package com.domain.mystream.Activity;

/* ================================

    - MyStream -

    created by cubycode @2018
    All Rights reserved

===================================*/

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.domain.mystream.Adpater.CellStreamPostAdpater;
import com.domain.mystream.Model.Comment;
import com.domain.mystream.Model.Comments;
import com.domain.mystream.Model.Company;
import com.domain.mystream.Model.Likes;
import com.domain.mystream.Model.OtherProfileModel;
import com.domain.mystream.Constants.Configs;
import com.domain.mystream.Constants.MarshMallowPermission;
import com.domain.mystream.Model.PostModel;
import com.domain.mystream.Model.PostType;
import com.domain.mystream.Model.User;
import com.domain.mystream.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.domain.mystream.Constants.Configs.editor;
import static com.domain.mystream.Constants.Configs.myPref;
import static com.domain.mystream.Constants.Configs.sharedPreferences;
import static com.domain.mystream.Constants.Configs.user_id;
import static com.domain.mystream.Constants.Configs.user_id;
import static com.domain.mystream.Constants.MyStreamApis.BASE_URL;
import static com.domain.mystream.Constants.MyStreamApis.GET_POST;
import static com.domain.mystream.Constants.MyStreamApis.OTHER_USER_PROFILE_URL;

public class OtherUserProfile extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    /* Views */
    RecyclerView streamsListView;
    ImageView avatarImg, coverImg;
    SwipeRefreshLayout refreshControl;
    TextView usernameTxt, fullNameTxt, aboutMeTxt;
    Button followersButt, followingButt, reportUserButt, followButt;
    int otherUserId;
    List<PostModel> postModelList = new ArrayList<PostModel>();
    private ShimmerFrameLayout mShimmerViewContainer;

    /* Variables */
    List<ParseObject> streamsArray;
    ParseUser userObj;
    MarshMallowPermission mmp = new MarshMallowPermission(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_user_profile);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Hide ActionBar
//        getSupportActionBar().hide();

        // Change StatusBar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
        sharedPreferences = getSharedPreferences(myPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        user_id = sharedPreferences.getString("userid", "0");
        otherUserId = sharedPreferences.getInt("otherUserId", 0);
        String frndPic = sharedPreferences.getString("img","0");
        mShimmerViewContainer = findViewById(R.id.shimmer_view);

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

        Glide.with(OtherUserProfile.this)
                .load(frndPic)
                .into(avatarImg);

        LinearLayoutManager llmm = new LinearLayoutManager(OtherUserProfile.this);
        llmm.setOrientation(LinearLayoutManager.VERTICAL);
        llmm.removeAllViews();
        streamsListView.setLayoutManager(llmm);
        streamsListView.setItemAnimator(null);
        // Init a refreshControl
        refreshControl = findViewById(R.id.swiperefresh);
        refreshControl.setOnRefreshListener(this);


        // MARK: - MESSAGE BUTTON ------------------------------------
          /*  Button messageButt = findViewById(R.id.oupMessageButt);
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

*/


        getOtherProfile(otherUserId, user_id);


        // MARK: - BACK BUTTON ------------------------------------
        Button backButt = findViewById(R.id.oupBackButt);
        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }// end onCreate()

    private void getOtherProfile(int otherUserId, String userid) {
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, OTHER_USER_PROFILE_URL+"userId=" + otherUserId + "&currentUserId=" + userid, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                OtherProfileModel otherProfileModel = gson.fromJson(response, OtherProfileModel.class);
                usernameTxt.setText(otherProfileModel.getUserName());
                aboutMeTxt.setText(otherProfileModel.getAbout());
                fullNameTxt.setText(otherProfileModel.getFullName());


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(OtherUserProfile.this);
        requestQueue.add(stringRequest);

    }



    // MARK: - REFRESH DATA ----------------------------------------
    @Override
    public void onRefresh() {
        // Recall query
        // queryStreams();
        getPost(user_id, otherUserId);
        if (refreshControl.isRefreshing()) {
            refreshControl.setRefreshing(false);
        }
    }

    private void getPost(final String user_id, int otherUserId) {

        postModelList.clear();
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, GET_POST+"currentUserId=" + user_id + "&userId=" + otherUserId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_LONG).show();
                //   hideProgress(getActivity());


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
                                if (user_id.equals(objectLike.getString("InteractionByUserId"))) {
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
                                commentsList.add(comments);
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
                        postModelList.add(postModel);
                    }

                    CellStreamPostAdpater cellStreamPostAdpater = new CellStreamPostAdpater(OtherUserProfile.this, postModelList);
                    streamsListView.setAdapter(cellStreamPostAdpater);
                    cellStreamPostAdpater.notifyDataSetChanged();

                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);

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
                //Toast.makeText(getActivity(), "Server error or No internet connection", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };
        //  showProgress(getActivity(), "Loading....", "Please wait!");
        RequestQueue requestQueue = Volley.newRequestQueue(OtherUserProfile.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
        getPost(user_id, otherUserId);
    }

}//@end
