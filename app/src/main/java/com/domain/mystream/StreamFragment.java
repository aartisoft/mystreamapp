package com.domain.mystream;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.domain.mystream.Adpater.StreamPostAdpater;
import com.domain.mystream.Model.Comment;
import com.domain.mystream.Model.Comments;
import com.domain.mystream.Model.CommentsModel;
import com.domain.mystream.Model.Company;
import com.domain.mystream.Model.Likes;
import com.domain.mystream.Model.PostModel;
import com.domain.mystream.Model.PostType;
import com.domain.mystream.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.domain.mystream.StreamDetails.hideProgress;
import static com.domain.mystream.StreamDetails.showProgress;


/**
 * A simple {@link Fragment} subclass.
 */
public class StreamFragment extends Fragment {
    String userId, userid, currentUserId;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static final String myPref = "mypref";
    StreamPostAdpater streamPostAdpater;
    List<PostModel> postModelList;

    public static StreamFragment newInstance() {
        StreamFragment fragment = new StreamFragment();
        return fragment;
    }

    RecyclerView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stream, container, false);


        sharedPreferences = getActivity().getSharedPreferences(myPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        listView = (RecyclerView) view.findViewById(R.id.hStreamsListView);
        LinearLayoutManager llmm = new LinearLayoutManager(getActivity());
        llmm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llmm);
        //  listView.setHasFixedSize(true);

        userid = sharedPreferences.getString("userid", userId);
        getPost();
        return view;

    }

    private void getPost() {
        postModelList = new ArrayList<PostModel>();
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, "https://app_api_json.veamex.com/api/common/GetPosts?userId=1&currentUserId=1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_LONG).show();
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
                        postModel.setUser(user);
/*

                               "Company":{"CompanyId":1,"ParentCompanyId":0,"CompanyName":"First Company","Description":"Test",
                               "EmailAddress":"developer01@mixedlearning.com","Faxnumber":"0","PhoneNumber":"0",
                               "WebsiteUrl":"0","LanguageCode":"NL","DateFormat":null,"TimeZone":null,"IsShowAllTimeZone":null,
                                   "SalesforceInTimeline":null,"SalesForceDataImport":null,"CreatedOnDate":"2018-04-18T00:00:00",
                                   "CreatedByUserId":1,"LastUpdatedOnDate":"2018-04-18T00:00:00","LastUpdatedByUserId":1,"ThemeName":null,
                                   "CompanyTypeId":1,"TemplateId":null,"SystemId":0},"FullName":"Test  Elearning","ConnectionCount":0,
                                   "PhotosCount":0,"VideoCount":0,"Identifier":"3C4FAD11-340C-4F94-95E2-EA5F3EECBF85","Interest":"int",
                                   "CurrentFunction":"fun","SystemId":0,"CurrentStatus":"online","NotifySms":true,"NotifyEmail":true},
                           "Likes":[],"Comments":[],"Shares":[],"RepliedUsers":[],"IsRemoved":false,"IsFullPost":true,"SystemId":0,
                               "UserGroupId":null
*/

                        JSONArray array = new JSONArray("Likes");
                        List<Likes> likesList=new ArrayList<>();
                        if (array!=null){
                            for (int k = 0; k < array.length(); k++){
                                JSONObject objectLike = array.getJSONObject(k);
                                Likes likes = new Likes();
                                likes.setInteractionByUserId(objectLike.getString("InteractionByUserId"));
                                likes.setPostTypeId(objectLike.getString("PostTypeId"));
                                likes.setReferenceId(objectLike.getString("ReferenceId"));
                                likes.setReactionTypeId(objectLike.getString("ReactionTypeId"));
                                if(userId.equals(likes.getInteractionByUserId()))
                                {
                                    postModel.setLikebyme(true);
                                }
                                likesList.add(likes);

                            }
                        }
                        postModel.setLikes(likesList);

                        JSONArray comment =new JSONArray("Comments");
                        List<Comments> commentsList=new ArrayList<>();
                        if (comment!=null){
                            for (int j = 0; j < comment.length(); j++){

                                JSONObject objectComment = comment.getJSONObject(j);
                                Comments comments = new Comments();
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
                        postModelList.add(postModel);
                   /* postModel.(object3.getString("FullName"));
                       postModel.setAddressId(object3.getString("ConnectionCount"));
                       postModel.setAddressId(object3.getString("PhotosCount"));
                       postModel.setAddressId(object3.getString("VideoCount"));
                    company.setAddressId(object3.getString("Identifier"));
                    company.setAddressId(object3.getString("Interest"));
                    company.setAddressId(object3.getString("CurrentFunction"));
                    company.setAddressId(object3.getString("CurrentFunction"));
                    company.setAddressId(object3.getString("CurrentFunction"));
*/
                    }

                    StreamPostAdpater streamPostAdpater = new StreamPostAdpater(getActivity(), postModelList);
                    listView.setAdapter(streamPostAdpater);
                    streamPostAdpater.notifyDataSetChanged();
                  /*  if (!userName.equals("null")) {

                        // Go to Home screen
                        //startActivity(new Intent(Login.this, Home.class));

                        // error
                    } else {
                        Configs.simpleAlert("Not a valid user", getActivity());
                    }
*/
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
}
