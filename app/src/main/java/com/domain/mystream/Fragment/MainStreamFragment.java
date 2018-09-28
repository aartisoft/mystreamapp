package com.domain.mystream.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.bumptech.glide.Glide;
import com.domain.mystream.Activity.AddStream;
import com.domain.mystream.Adpater.CellStreamPostAdpater;
import com.domain.mystream.Constants.Configs;
import com.domain.mystream.Constants.MarshMallowPermission;
import com.domain.mystream.Model.Comment;
import com.domain.mystream.Model.Comments;
import com.domain.mystream.Model.Company;
import com.domain.mystream.Model.Likes;
import com.domain.mystream.Model.PostModel;
import com.domain.mystream.Model.PostType;
import com.domain.mystream.Model.User;
import com.domain.mystream.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.domain.mystream.Activity.StreamDetails.hideProgress;
import static com.domain.mystream.Activity.StreamDetails.showProgress;
import static com.domain.mystream.Constants.Configs.editor;
import static com.domain.mystream.Constants.Configs.error_toast;
import static com.domain.mystream.Constants.Configs.myPref;
import static com.domain.mystream.Constants.Configs.sharedPreferences;
import static com.domain.mystream.Constants.Configs.user_id;
import static com.domain.mystream.Constants.MyStreamApis.BASE_URL;
import static com.domain.mystream.Constants.MyStreamApis.GET_POST;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainStreamFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    String currentUserId = "1";
    CellStreamPostAdpater cellStreamPostAdpater;
    List<PostModel> postModelList = new ArrayList<PostModel>();
    private ShimmerFrameLayout mShimmerViewContainer;
    Button addStreamButt;
    Button addPhotoButt;
    Button addAudioButt;
    Button addVideoButt;
    ImageView currUserAvatarImg;
    MarshMallowPermission mmp;
    SwipeRefreshLayout refreshControl;
    Context context;

    public static MainStreamFragment newInstance() {
        MainStreamFragment fragment = new MainStreamFragment();
        return fragment;
    }

    RecyclerView listView;

    @Override
    public void onStart() {
        super.onStart();
        if (!mmp.checkPermissionForReadExternalStorage()) {
            mmp.requestPermissionForReadExternalStorage();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_stream, container, false);
        sharedPreferences = getActivity().getSharedPreferences(myPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);

        Button addStreamButt = view.findViewById(R.id.hAddStreamButt);
        addStreamButt.setTypeface(Configs.titRegular);
        addStreamButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddStream.class));
            }
        });

        mmp = new MarshMallowPermission(getActivity());

        // MARK: - ADD PHOTO BUTTON ------------------------------------
        addPhotoButt = view.findViewById(R.id.hAddPhotoButt);
        addPhotoButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mmp.checkPermissionForReadExternalStorage()) {
                    mmp.requestPermissionForReadExternalStorage();
                } else {
                    Intent i = new Intent(getActivity(), AddStream.class);
                    Bundle extras = new Bundle();
                    extras.putString("streamAttachment", "image");
                    i.putExtras(extras);
                    startActivity(i);
                }
            }
        });


        // MARK: - ADD VIDEO BUTTON ------------------------------------
        addVideoButt = view.findViewById(R.id.hAddVideoButt);
        addVideoButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mmp.checkPermissionForCamera()) {
                    mmp.requestPermissionForCamera();
                } else {
                    Intent i = new Intent(getActivity(), AddStream.class);
                    Bundle extras = new Bundle();
                    extras.putString("streamAttachment", "video");
                    i.putExtras(extras);
                    startActivity(i);
                }
            }
        });


        // MARK: - ADD AUDIO BUTTON ------------------------------------
     /*   addAudioButt = view.findViewById(R.id.hAddAudioButt);
        addAudioButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mmp.checkPermissionForRecord()) {
                    mmp.requestPermissionForRecord();
                } else {
                    Intent i = new Intent(getActivity(), AddStream.class);
                    Bundle extras = new Bundle();
                    extras.putString("streamAttachment", "audio");
                    i.putExtras(extras);
                    startActivity(i);
                }
            }
        });*/

        // Init views
        currUserAvatarImg = view.findViewById(R.id.hcurrUserAvatarImg);

        // Init a refreshControl
        refreshControl = view.findViewById(R.id.swiperefresh);
        refreshControl.setOnRefreshListener(this);

        sharedPreferences = getActivity().getSharedPreferences(myPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String pp = sharedPreferences.getString("ProfilePic", null);
        Glide.with(getActivity())
                .load(pp)
                .into(currUserAvatarImg);
        listView = (RecyclerView) view.findViewById(R.id.hStreamsListView);
        LinearLayoutManager llmm = new LinearLayoutManager(getActivity());
        llmm.setOrientation(LinearLayoutManager.VERTICAL);
        llmm.removeAllViews();
        listView.setLayoutManager(llmm);
        listView.setItemAnimator(null);
        //  listView.setHasFixedSize(true);

        user_id = sharedPreferences.getString("userid", "0");
        //getPost();
        return view;

    }

    private void getPost(final String user_id, String currentUserId) {

        postModelList.clear();
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, GET_POST+"currentUserId=" + user_id + "&userId=" + currentUserId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_LONG).show();
                //   hideProgress(getActivity());
                if (response != null) {
                    refreshControl.setRefreshing(false);
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

                        CellStreamPostAdpater cellStreamPostAdpater = new CellStreamPostAdpater(getActivity(), postModelList);
                        listView.setAdapter(cellStreamPostAdpater);
                        cellStreamPostAdpater.notifyDataSetChanged();

                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

                refreshControl.setRefreshing(false);
                Toast.makeText(getActivity(), error_toast, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };
        //  showProgress(getActivity(), "Loading....", "Please wait!");
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRefresh() {

        if (refreshControl.isRefreshing()) {

            Log.d(currentUserId+"="+user_id,"=iddddkaram");
            getPost(user_id, currentUserId);
            //  refreshControl.setRefreshing(false);
        }

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
        getPost(user_id, currentUserId);
    }

}
