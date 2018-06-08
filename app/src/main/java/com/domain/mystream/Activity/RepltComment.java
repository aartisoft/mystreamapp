package com.domain.mystream.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.domain.mystream.Adpater.CommentsAdpater;
import com.domain.mystream.Adpater.ReplyAdpater;
import com.domain.mystream.Constants.Configs;
import com.domain.mystream.Model.CommentGsonModel;
import com.domain.mystream.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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
import static com.domain.mystream.Constants.MyStreamApis.COMMENT_POST;
import static com.domain.mystream.Constants.MyStreamApis.GET_ALL_POST_COMMENT;

public class RepltComment extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    String name, comment, parentCommentId, postId, postType, commmentText, fullname, datetext, cmntxt, pic, replyPostType, replyPostId;
    ReplyAdpater replyAdpater;
    EditText replyTxt;
    ImageView commentUserpic;
    TextView userName, date, commenttext;
    Button replyBack;
    int parentComment;
    CommentGsonModel[] commentGson;
    SwipeRefreshLayout refreshControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replt_comment);

        recyclerView = findViewById(R.id.replyListView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        replyBack = findViewById(R.id.replyBackButt);
        replyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        replyTxt = findViewById(R.id.replyCommentTxt);
        replyTxt.setTypeface(Configs.titRegular);
        refreshControl = findViewById(R.id.replyswiperefresh);

        sharedPreferences = getSharedPreferences(myPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        user_id = sharedPreferences.getString("userid", "0");


        Bundle extras = getIntent().getExtras();

        if (extras != null) {

        /*    postId = extras.getString("PostId");
            postType = extras.getString("PostTypeId");*/
            parentComment = extras.getInt("parentcoment", 0);
            commmentText = extras.getString("Comment");
            name = extras.getString("FullName");
            //streamtxt = extras.getString("PostBody");

            fullname = extras.getString("fullname");
            datetext = extras.getString("date");
            cmntxt = extras.getString("comment");
            pic = extras.getString("img");

        }

     /*   postId= sharedPreferences.getString("postId",null);
        postType = sharedPreferences.getString("PostTypeId",null);*/


        replyPostId = sharedPreferences.getString("postId", null);
        replyPostType = sharedPreferences.getString("PostTypeId", null);

        userName = findViewById(R.id.crcommFullnameTxt);
        userName.setTypeface(Configs.titSemibold);
        userName.setText(fullname);

        date = findViewById(R.id.crcommDateTxt);
        date.setTypeface(Configs.titRegular);
        date.setText(datetext);

        commenttext = findViewById(R.id.crcommCommTxt);
        commenttext.setTypeface(Configs.titRegular);
        commenttext.setText(cmntxt);

        commentUserpic = findViewById(R.id.crcommAvatarimg);
        Glide.with(RepltComment.this)
                .load(pic)
                .into(commentUserpic);
        Button sendButt = findViewById(R.id.replySendButt);
        sendButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = replyTxt.getText().toString().trim();
                commentPost(replyPostId, replyPostType, String.valueOf(parentComment), user_id, comment);

            }
        });
    }

    private void commentPost(String referenceId, String referenceTypeId, final String parentCommentId, String interactionByUserId, final String comment) {
        String url = COMMENT_POST+"referenceId=" + referenceId + "&referenceTypeId=" + referenceTypeId + "&parentCommentId=" + parentCommentId + "&interactionByUserId=" + interactionByUserId + "&comment=" + comment;
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //hideProgress(RepltComment.this);

                //  Toast.makeText(RepltComment.this, "comented", Toast.LENGTH_SHORT).show();
                  GetPostComments(replyPostId, replyPostType);
                //refreshControl.setRefreshing(false);
                replyTxt.setText(" ");
                /*replyAdpater = new ReplyAdpater(RepltComment.this, commentGson, parentComment);
                recyclerView.setAdapter(replyAdpater);*/

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
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "text/html");


                return params;
            }
        };
        //  showProgress(RepltComment.this, "Loading....", "Please wait!");
        RequestQueue requestQueue = Volley.newRequestQueue(RepltComment.this);
        requestQueue.add(stringRequest);

    }


    @Override
    public void onRefresh() {
        GetPostComments(replyPostId, replyPostType);
        if (refreshControl.isRefreshing()) {
            //
            //  commentPost(replyPostId, replyPostType, String.valueOf(parentComment), user_id, comment);

        }
    }

    private void GetPostComments(String referenceId, String referenceTypeId) {

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, GET_ALL_POST_COMMENT+"referenceId=" + referenceId + "&referenceTypeId=" + referenceTypeId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_LONG).show();
                //  hideProgress(Comments.this);

                // refreshControl.setRefreshing(false);
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                commentGson = gson.fromJson(response, CommentGsonModel[].class);
                List<CommentGsonModel> commentGson1=new ArrayList<>();
                for(int i=0,j=0;i<commentGson.length;i++) {
                    try{
                    int id=commentGson[i].getComment().getParentCommentId();
                    if(String.valueOf(id)!=null) {
                        if (parentComment == id) {
                            commentGson1.add(commentGson[i]);
                            j++;
                        }
                    }}catch (Exception e){

                    }
                }
                replyAdpater = new ReplyAdpater(RepltComment.this, commentGson1, parentComment);
                recyclerView.setAdapter(replyAdpater);

                replyAdpater.notifyDataSetChanged();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {

                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject jsonObject = new JSONObject(responseBody);
                } catch (Exception e) {
                    //Handle a malformed json response

                }
                // refreshControl.setRefreshing(false);
                Toast.makeText(RepltComment.this, error_toast, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };
        //  showProgress(Comments.this, "Loading....", "Please wait!");
        RequestQueue requestQueue = Volley.newRequestQueue(RepltComment.this);
        requestQueue.add(stringRequest);

    }
    @Override
    protected void onResume() {
        super.onResume();
        GetPostComments(replyPostId, replyPostType);
    }

}
