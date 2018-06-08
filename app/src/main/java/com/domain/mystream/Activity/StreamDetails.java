package com.domain.mystream.Activity;

/* ================================

    - MyStream -

    created by cubycode @2018
    All Rights reserved

===================================*/

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
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
import com.bumptech.glide.Glide;
import com.domain.mystream.Constants.Configs;
import com.domain.mystream.Constants.MarshMallowPermission;
import com.domain.mystream.Model.CommentGsonModel;
import com.domain.mystream.Model.PostModel;
import com.domain.mystream.R;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.parse.ParseObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.domain.mystream.Constants.Configs.myPref;
import static com.domain.mystream.Constants.Configs.sharedPreferences;
import static com.domain.mystream.Constants.MyStreamApis.BASE_URL;
import static com.domain.mystream.Constants.MyStreamApis.DELETE_POST;
import static com.domain.mystream.Constants.MyStreamApis.LIKE_POST;

public class StreamDetails extends AppCompatActivity {

    /* Views */
    ImageView avatarImg, streamImg;
    TextView fullnameTxt, usernameTxt, streamTxt, likesTxt, commentsTxt, playingTimeTxt;
    Button optionsButt, likeButt, commentsButt, shareButt, playButt;
    List<PostModel> postModelList;
    String userid;
    Integer postId;
    CommentGsonModel[] commentGsonModels;
    /* Variables */
    ParseObject sObj;
    MarshMallowPermission mmp = new MarshMallowPermission(this);
    boolean audioIsPlaying = false;
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    JSONObject jsonObject;
    String createdOnDate;
    String postid;
    String postbody;
    String podtname;
    String posttype;
    String createdbyuser;
    String lastupdateduser;
    String fullname, img;
    String username;
    int commenttext;
    WebView webViewStream;

    int liketext;
    Boolean likestatus;
    public static IOSDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream_details);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
         sharedPreferences = getSharedPreferences(myPref, Context.MODE_PRIVATE);
        Configs.editor = sharedPreferences.edit();
        // Hide ActionBar
//        getSupportActionBar().hide();

        // Change StatusBar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.light_blue));

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            postid = extras.getString("PostId");
            postbody = extras.getString("PostBody");
            podtname = extras.getString("PostName");
            createdbyuser = extras.getString("CreatedByUserId");
            lastupdateduser = extras.getString("LastUpdatedByUserId");
            posttype = extras.getString("PostTypeId");
            createdOnDate = extras.getString("CreatedOnDate");
            fullname = extras.getString("FullName");
            username = extras.getString("UserName");
            likestatus = extras.getBoolean("Likes");
            liketext = extras.getInt("LikesText", 0);
            commenttext = extras.getInt("CommentsValue", 0);
            img = extras.getString("img");


        }


        userid = sharedPreferences.getString("userid", "0");


        // Init views
        avatarImg = findViewById(R.id.sdAvatarImg);
        Glide.with(StreamDetails.this)
                .load(img)
                .into(avatarImg);
        webViewStream = findViewById(R.id.sdStreamImg);
        fullnameTxt = findViewById(R.id.sdFullnameTxt);
        fullnameTxt.setTypeface(Configs.titSemibold);
        fullnameTxt.setText(fullname);
        usernameTxt = findViewById(R.id.sdUsernameTxt);
        usernameTxt.setTypeface(Configs.titRegular);
        usernameTxt.setText(username);

        String summary =postbody;
        String c = summary.replace("src=\"","src=\"https://app_api_json.veamex.com");
        String head = "<!DOCTYPE html><html><head><meta name=\"viewport\" content=\"width=device-width, user-scalable=yes\" /></head>";
        String html = head + "<body style='background-color:#ffffff;'>" + c + "</body></html>";
        webViewStream.loadData(html, "text/html", null);
        WebSettings webSettings = webViewStream.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webSettings.setDomStorageEnabled(true);

        optionsButt = findViewById(R.id.sdOptionsButt);
        optionsButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(StreamDetails.this);
                alert.setMessage("Are you sure you want to delete this commment?")
                        .setTitle(R.string.app_name)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int ss) {

                                deletePost(Integer.valueOf(postid));


                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .setIcon(R.drawable.logo);
                alert.create().show();
            }

        });
        likeButt = findViewById(R.id.sdLikeButt);

        commentsButt = findViewById(R.id.sdCommentsButt);
        commentsButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StreamDetails.this, Comments.class);
                startActivity(intent);
            }
        });
        shareButt = findViewById(R.id.sdShareButt);
        likesTxt = findViewById(R.id.sdLikesTxt);
        likesTxt.setTypeface(Configs.titRegular);

        commentsTxt = findViewById(R.id.sdCommentsTxt);
        commentsTxt.setTypeface(Configs.titRegular);
        commentsTxt.setText(commenttext + "");

        playButt = findViewById(R.id.sdPlayButt);
        playingTimeTxt = findViewById(R.id.sdPlayingTimeTxt);
        playingTimeTxt.setTypeface(Configs.titSemibold);


        if (liketext > 0) {

            likesTxt.setText(String.valueOf(liketext));
        } else {
            likesTxt.setText(String.valueOf("0"));
        }


        if (likestatus) {
            likeButt.setBackgroundResource(R.drawable.liked_butt_small);
        } else {
            likeButt.setBackgroundResource(R.drawable.like_butt_small);
        }
        likeButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (likestatus) {
                    likeButt.setBackgroundResource(R.drawable.like_butt_small);

                    if (liketext > 0) {
                        likesTxt.setText(String.valueOf(liketext - 1));
                    } else {
                        likesTxt.setText(String.valueOf(0));

                    }
                    likestatus = false;


                } else {
                    likeButt.setBackgroundResource(R.drawable.liked_butt_small);

                    if (liketext > 0) {
                        likesTxt.setText(String.valueOf(liketext + 1));
                    } else {
                        likesTxt.setText(String.valueOf(1));
                    }
                    likestatus = true;

                }


                likePost(postid, posttype, "1", userid);
            }
        });




        // MARK: - BACK BUTTON ------------------------------------
        Button backButt = findViewById(R.id.sdBackButt);
        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    handler.removeCallbacksAndMessages(null);
                }
                finish();
            }
        });


    }// end onCreate()

    private void deletePost(final Integer postId) {

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, DELETE_POST+"postId=" + String.valueOf(postId), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_LONG).show();
                hideProgress(StreamDetails.this);

                finish();
/*
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                *//*  CommentGsonModel[] commentGson = gson.fromJson(response, CommentGsonModel[].class);*//*
                CommentGsonModel[] commentGson1=new CommentGsonModel[commentGsonModels.length-1 ]; //= new ArrayList<>();



                for(int i = 0,j=0  ; i<postModelList.size(); i++){
                    if (commentGsonModels[i].getCommentId() != postId) {
                        commentGson1[j] = commentGsonModels[i];
                        j++;
                    }
                }
                commentGsonModels=commentGson1;*/


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
                Toast.makeText(StreamDetails.this, "Server error or No internet connection", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };
        // showProgress(getActivity(), "Loading....", "Please wait!");
        RequestQueue requestQueue = Volley.newRequestQueue(StreamDetails.this);
        requestQueue.add(stringRequest);
    }


    private void likePost(String referenceId, String referenceTypeId, String reactionTypeId, String interactionByUserId) {

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, BASE_URL+LIKE_POST+"referenceId=" + referenceId + "&referenceTypeId=" + referenceTypeId + "&reactionTypeId=" + reactionTypeId + "&interactionByUserId=" + interactionByUserId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_LONG).show();
                //  hideProgress(getActivity());




              /*  int likes = sObj.getInt(Configs.STREAMS_LIKES);
                holder.likesTxt.setText(Configs.roundThousandsIntoK(likes));

                // Show liked icon
                List<String>likedBy = sObj.getList(Configs.STREAMS_LIKED_BY);
                if (likedBy.contains(currUser.getObjectId()) ){
                 likeButt.setBackgroundResource(R.drawable.liked_butt_small);
                } else {
                    likeButt.setBackgroundResource(R.drawable.like_butt_small);
                }
*/
            /*    try {
                    // Configs.hidePD();
                    JSONArray rs = new JSONArray(response);
                    for(int i=0;i<rs.length();i++)
                    {
                        JSONObject object= rs.getJSONObject(i);
                        PostModel postModel=new PostModel();


                    }





                    CellStreamPostAdpater cellStreamPostAdpater = new CellStreamPostAdpater(context,postModelList);
                    cellStreamPostAdpater.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

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
                Toast.makeText(StreamDetails.this, "Server error or No internet connection", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };
        showProgress(StreamDetails.this, "Loading....", "Please wait!");
        RequestQueue requestQueue = Volley.newRequestQueue(StreamDetails.this);
        requestQueue.add(stringRequest);
    }



    public static void showProgress(Context context, String msg, String title) {

        progressDialog = new IOSDialog.Builder(context)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        //dialog1.show();
                    }
                })
                .setSpinnerColorRes(R.color.main_color)
                .setCancelable(true)
                .setSpinnerClockwise(false)
                .setSpinnerDuration(120)
                .setMessageContentGravity(Gravity.END)
                .build();
        progressDialog.show();

    }

    public static void hideProgress(Context context) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
