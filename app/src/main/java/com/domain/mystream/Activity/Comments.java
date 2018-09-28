package com.domain.mystream.Activity;

/* ================================

    - MyStream -

    created by cubycode @2018
    All Rights reserved

===================================*/

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.domain.mystream.Adpater.CellStreamPostAdpater;
import com.domain.mystream.Adpater.CommentsAdpater;

import com.domain.mystream.Constants.Configs;
import com.domain.mystream.Model.CommentGsonModel;
import com.domain.mystream.Model.PostModel;

import com.domain.mystream.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.parse.ParseObject;

import org.json.JSONObject;

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

public class Comments extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    /* Views */
    RecyclerView commentsListView;
    TextView fullnameTxt ;
    WebView streamTextTxt;
    EditText commentTxt;
    SwipeRefreshLayout refreshControl;
    CommentGsonModel[] commentGson;
    List<PostModel> postModelList;
    CellStreamPostAdpater cellStreamPostAdpater;
    String referenceId, streamtxt, name, comment,postBody,
            parentCommentId, postId, postType, commmentText, parentComment = "0";

    /* Variables */
    ParseObject sObj;
    List<ParseObject> commentsArray;
    CommentsAdpater adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Hide ActionBar
//        getSupportActionBar().hide();

        // Change StatusBar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.light_blue));

        sharedPreferences = getSharedPreferences(myPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        user_id = sharedPreferences.getString("userid", "0");


        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            postId = extras.getString("PostId");
            postType = extras.getString("PostTypeId");
            commmentText = extras.getString("Comment");
            postBody = extras.getString("PostBody");
            name = extras.getString("FullName");
            streamtxt = extras.getString("PostBody");
        }
        editor.putString("postId", postId);
        editor.putString("PostTypeId", postType);
        editor.apply();

        // Init views
        commentsListView = findViewById(R.id.commListView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        commentsListView.setLayoutManager(linearLayoutManager);
        commentsListView.setHasFixedSize(true);
        fullnameTxt = findViewById(R.id.commFullnameTxt);
        fullnameTxt.setTypeface(Configs.titSemibold);
        fullnameTxt.setText(name);
        streamTextTxt = findViewById(R.id.commStreamTxt);
        commentTxt = findViewById(R.id.commCommentTxt);
        commentTxt.setTypeface(Configs.titRegular);


        String summary =postBody;
        String c = summary.replace("src=\"","src=\"https://app_api_json.veamex.com");
        String head = "<!DOCTYPE html><html><head><meta name=\"viewport\" content=\"width=device-width, user-scalable=yes\" /></head>";
        String html = head + "<body style='background-color:#ffffff;'>" + c + "</body></html>";
        streamTextTxt.loadData(html, "text/html", null);
        WebSettings webSettings = streamTextTxt.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webSettings.setDomStorageEnabled(true);
        // Init a refreshControl
        refreshControl = findViewById(R.id.swiperefresh);
        refreshControl.setOnRefreshListener(this);


        // MARK: - SEND COMMENT BUTTON ------------------------------------
        Button sendButt = findViewById(R.id.commSendButt);
        sendButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = commentTxt.getText().toString().trim();

                commentPost(postId, postType, parentComment, user_id, comment);

            }
        });


        // MARK: - BACK BUTTON ------------------------------------
        Button backButt = findViewById(R.id.commBackButt);
        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // INTERSTITIAL AD IMPLEMENTATION ------------------------------------
       /* final InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.ADMOB_INTERSTITIAL_UNIT_ID));
        AdRequest requestForInterstitial = new AdRequest.Builder().build();
        interstitialAd.loadAd(requestForInterstitial);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.i("log-", "INTERSTITIAL is loaded!");
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }
        });*/

        GetPostComments(postId, postType);

    }// end onCreate()


    void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(commentTxt.getWindowToken(), 0);
    }


    // MARK: - REFRESH DATA ----------------------------------------
    @Override
    public void onRefresh() {
        // Recall query
        // commentPost(postId, postType, parentComment, userid, comment);

        if (refreshControl.isRefreshing()) {
            GetPostComments(postId, postType);

        }
    }

    private void commentPost(String referenceId, String referenceTypeId, String parentCommentId, String interactionByUserId, final String comment) {

        Log.d("referenceId","referenceId="+referenceId);
        Log.d("referenceIdddd","referenceIdd="+referenceTypeId);
        Log.d("referenceIdrrrrr=","referenceIdrrrrr="+parentCommentId);
        Log.d("referenceIdooooo=","referenceIdoooo="+interactionByUserId);
        Log.d("referenceIdkkkk=","referenceIdlkkkk="+comment);

        //Log.d("referenceId","referenceId"+referenceId);
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, COMMENT_POST+"referenceId=" + referenceId + "&referenceTypeId=" + referenceTypeId + "&parentCommentId=" + parentCommentId + "&interactionByUserId=" + interactionByUserId + "&comment=" + comment, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // hideProgress(Comments.this);

                Toast.makeText(Comments.this, "comented", Toast.LENGTH_SHORT).show();
                GetPostComments(postId, postType);
                commentTxt.setText(" ");
                adapter.notifyDataSetChanged();
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
                //  refreshControl.setRefreshing(false);
                Toast.makeText(Comments.this, error_toast, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };
        // showProgress(Comments.this, "Loading....", "Please wait!");
        RequestQueue requestQueue = Volley.newRequestQueue(Comments.this);
        requestQueue.add(stringRequest);

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


                adapter = new CommentsAdpater(Comments.this, commentGson);
                commentsListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


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
                // refreshControl.setRefreshing(false);
                Toast.makeText(Comments.this, error_toast, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };
        //  showProgress(Comments.this, "Loading....", "Please wait!");
        RequestQueue requestQueue = Volley.newRequestQueue(Comments.this);
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        GetPostComments(postId, postType);
    }


}// @end
