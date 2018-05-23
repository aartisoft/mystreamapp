package com.domain.mystream;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.domain.mystream.Adpater.CommentsAdpater;
import com.domain.mystream.Adpater.ReplyAdpater;
import com.domain.mystream.Model.CommentGsonModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.domain.mystream.Login.myPref;
import static com.domain.mystream.StreamDetails.hideProgress;
import static com.domain.mystream.StreamDetails.showProgress;

public class RepltComment extends AppCompatActivity {

    RecyclerView recyclerView;
    String  userid,  name, comment, parentCommentId, postId, postType, commmentText, parentComment,fullname,datetext,cmntxt,pic;
    ReplyAdpater replyAdpater;
    EditText replyTxt;
    ImageView commentUserpic;
    TextView userName,date,commenttext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replt_comment);

        recyclerView =findViewById(R.id.replyListView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        replyTxt = findViewById(R.id.replyCommentTxt);
        replyTxt.setTypeface(Configs.titRegular);

        SharedPreferences sharedPreferences = getSharedPreferences(myPref, Context.MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "0");

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            postId = extras.getString("PostId");
            postType = extras.getString("PostTypeId");
            parentComment = extras.getString("parentcoment");
            if (parentComment == null) {
                parentComment = "0";

            }
            commmentText = extras.getString("Comment");
            name = extras.getString("FullName");
            //streamtxt = extras.getString("PostBody");

            fullname =extras.getString("fullname");
            datetext=extras.getString("date");
            cmntxt=extras.getString("comment");
            pic =extras.getString("img");


        }

        userName = findViewById(R.id.crcommFullnameTxt);
        userName.setTypeface(Configs.titSemibold);
        userName.setText(fullname);

        date = findViewById(R.id.crcommDateTxt);
        date.setTypeface(Configs.titRegular);
        date.setText(datetext);

        commenttext =findViewById(R.id.crcommCommTxt);
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
                commentPost(postId, postType, parentComment, userid, comment);

            }
        });
    }
    private void commentPost(String referenceId, String referenceTypeId, String parentCommentId, String interactionByUserId, final String comment) {

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, "https://app_api_json.veamex.com/api/common/NewComment?referenceId=" + referenceId + "&referenceTypeId=" + referenceTypeId + "&parentCommentId=" + parentCommentId + "&interactionByUserId=" + interactionByUserId + "&comment=" + comment, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideProgress(RepltComment.this);

                Toast.makeText(RepltComment.this, "comented", Toast.LENGTH_SHORT).show();

                replyTxt.setText(" ");
                replyAdpater.notifyDataSetChanged();
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
                Toast.makeText(RepltComment.this, "Server errgor or No internet connection", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };
        showProgress(RepltComment.this, "Loading....", "Please wait!");
        RequestQueue requestQueue = Volley.newRequestQueue(RepltComment.this);
        requestQueue.add(stringRequest);

    }

}
