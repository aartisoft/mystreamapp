package com.domain.mystream.Fragment;

/* ================================

    - MyStream -

    created by cubycode @2018
    All Rights reserved

===================================*/

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.domain.mystream.Activity.AddStream;
import com.domain.mystream.Activity.Settings;


import com.domain.mystream.Adpater.MyStreamPostAdpater;
import com.domain.mystream.Constants.Configs;
import com.domain.mystream.Constants.MarshMallowPermission;
import com.domain.mystream.Constants.VolleyMultipartRequest;
import com.domain.mystream.Model.Comment;
import com.domain.mystream.Model.Comments;
import com.domain.mystream.Model.Company;
import com.domain.mystream.Model.Likes;
import com.domain.mystream.Model.PostModel;
import com.domain.mystream.Model.PostType;
import com.domain.mystream.Model.User;
import com.domain.mystream.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static android.app.Activity.RESULT_OK;
import static android.media.MediaRecorder.VideoSource.CAMERA;
import static com.domain.mystream.Activity.StreamDetails.hideProgress;
import static com.domain.mystream.Activity.StreamDetails.showProgress;
import static com.domain.mystream.Constants.Configs.editor;
import static com.domain.mystream.Constants.Configs.error_toast;
import static com.domain.mystream.Constants.Configs.myPref;
import static com.domain.mystream.Constants.Configs.sharedPreferences;
import static com.domain.mystream.Constants.Configs.user_id;
import static com.domain.mystream.Constants.MyStreamApis.BASE_URL;
import static com.domain.mystream.Constants.MyStreamApis.GET_POST;

public class Account extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    /* Views */
    RecyclerView streamsListView;
    ImageView avatarImg, coverImg;
    SwipeRefreshLayout refreshControl;
    TextView usernameTxt, fullNameTxt, aboutMeTxt;
    Button followersButt, followingButt, settButt, addStreamButt;
    String uasername, fullname,about;
    private int PICK_IMAGE_REQUEST = 1;
    List<PostModel> postModelList;
    Uri imageURI;
    File file;
    Bitmap bitmap;
    AlertDialog addStreamPanel;
    int GALLERY = 2;
    MarshMallowPermission mmp = new MarshMallowPermission(getActivity());

    public static Account newInstance() {
        Account fragment = new Account();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account, container, false);

        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
        sharedPreferences = getActivity().getSharedPreferences(myPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        uasername = sharedPreferences.getString("username", null);
        user_id = sharedPreferences.getString("userid", "0");
        uasername= sharedPreferences.getString("username","0");
        fullname = sharedPreferences.getString("fullname","0");
        about =sharedPreferences.getString("About",null);


        // Init views
        avatarImg = view.findViewById(R.id.accAvatarImg);
        coverImg = view.findViewById(R.id.accCoverImg);
        usernameTxt = view.findViewById(R.id.accUsernameTxt);
        usernameTxt.setTypeface(Configs.titSemibold);
        usernameTxt.setText(uasername);
        followersButt = view.findViewById(R.id.accFollowersButt);
        followersButt.setTypeface(Configs.titRegular);
        followingButt = view.findViewById(R.id.accFollowingButt);
        followingButt.setTypeface(Configs.titRegular);
        fullNameTxt = view.findViewById(R.id.accFullnameTxt);
        fullNameTxt.setTypeface(Configs.titBlack);
        fullNameTxt.setText(fullname);
        aboutMeTxt = view.findViewById(R.id.accAboutMeTxt);
        aboutMeTxt.setTypeface(Configs.titRegular);
        streamsListView = view.findViewById(R.id.accStreamsListView1);
        LinearLayoutManager llmm = new LinearLayoutManager(getActivity());
        llmm.setOrientation(LinearLayoutManager.VERTICAL);
        streamsListView.setLayoutManager(llmm);
        streamsListView.setItemAnimator(null);

        settButt = view.findViewById(R.id.accSettingsButt);


        // Init a refreshControl
        refreshControl = view.findViewById(R.id.swiperefresh);
        refreshControl.setOnRefreshListener(this);

        settButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Settings.class);
                startActivity(intent);
            }
        });
        addStreamButt = view.findViewById(R.id.accAddStreamButt);
        addStreamButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddStream.class));
            }
        });

        sharedPreferences = getActivity().getSharedPreferences(myPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String pp = sharedPreferences.getString("ProfilePic",null);
        Glide.with(getActivity())
                .load(pp)
                .into(avatarImg);

        getPost(user_id, user_id);
        Log.d("StringRequest"+user_id,"StringRequest="+user_id+"=");
        return view;
    }

    private void getPost(final String user_id, String user) {
        postModelList = new ArrayList<PostModel>();
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, GET_POST+"userId=" + user_id + "&currentUserId=" + user, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                hideProgress(getActivity());


                try {


                    // Configs.hidePD();
                    JSONArray rs = new JSONArray(response);
                    for (int i = 0; i < rs.length(); i++) {
                        JSONObject object = rs.getJSONObject(i);
                        PostModel postModel = new PostModel();
Log.d(postModel.getPostId()+postModel.getPostTypeId(),"postID");
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
                Toast.makeText(getActivity(), error_toast, Toast.LENGTH_LONG).show();
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
        if (refreshControl.isRefreshing()) {
            refreshControl.setRefreshing(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        uasername = sharedPreferences.getString("username", null);
        user_id = sharedPreferences.getString("userid", "0");
        uasername= sharedPreferences.getString("username","0") ;
        fullname = sharedPreferences.getString("fullname","0");
        String pp = sharedPreferences.getString("ProfilePic",null);
        about =sharedPreferences.getString("About",null);
        Glide.with(getActivity())
                .load(pp)
                .into(avatarImg);

        fullNameTxt.setText(fullname);
        aboutMeTxt.setText(about);
    }

}
//@end
