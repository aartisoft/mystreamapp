package com.domain.mystream;

/* ================================

    - MyStream -

    created by cubycode @2018
    All Rights reserved

===================================*/

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import com.domain.mystream.Model.CommentGsonModel;
import com.domain.mystream.Model.PostModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.parse.ParseObject;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.domain.mystream.Login.myPref;
import static com.domain.mystream.StreamDetails.hideProgress;
import static com.domain.mystream.StreamDetails.showProgress;

public class Comments extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    /* Views */
    RecyclerView commentsListView;
    TextView fullnameTxt, streamTextTxt;
    EditText commentTxt;
    SwipeRefreshLayout refreshControl;

    List<PostModel> postModelList;
    CellStreamPostAdpater cellStreamPostAdpater;
    String referenceId, userid, streamtxt, name, comment,
            parentCommentId, postId, postType, commmentText, parentComment="0";

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

        SharedPreferences sharedPreferences = getSharedPreferences(myPref, Context.MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "0");

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            postId = extras.getString("PostId");
            postType = extras.getString("PostTypeId");

            commmentText = extras.getString("Comment");
            name = extras.getString("FullName");
            streamtxt = extras.getString("PostBody");
        }
        // Init views
        commentsListView = findViewById(R.id.commListView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        commentsListView.setLayoutManager(linearLayoutManager);
        commentsListView.setHasFixedSize(true);
        fullnameTxt = findViewById(R.id.commFullnameTxt);
        fullnameTxt.setTypeface(Configs.titSemibold);
        fullnameTxt.setText(name);
        streamTextTxt = findViewById(R.id.commStreamTxt);
        streamTextTxt.setTypeface(Configs.titRegular);
        streamTextTxt.setText(streamtxt);
        commentTxt = findViewById(R.id.commCommentTxt);
        commentTxt.setTypeface(Configs.titRegular);


        // Init a refreshControl
        refreshControl = findViewById(R.id.swiperefresh);
        refreshControl.setOnRefreshListener(this);


        // Get objectID from previous .java
       /* Bundle extras = getIntent().getExtras();
        String objectID = extras.getString("objectID");
        sObj = ParseObject.createWithoutData(Configs.STREAMS_CLASS_NAME, objectID);
        try {

            sObj.fetchIfNeeded().getParseObject(Configs.STREAMS_CLASS_NAME);

            // Get User Pointer
            sObj.getParseObject(Configs.STREAMS_USER_POINTER).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject userPointer, ParseException e) {
                    if (e == null) {
                        // Get full name
                        fullnameTxt.setText(userPointer.getString(Configs.USER_FULLNAME));

                        // Get Stream text
                        streamTextTxt.setText(sObj.getString(Configs.STREAMS_TEXT));

                        // Call query
                        commentPost();

                    } else {
                        Configs.simpleAlert(e.getMessage(), Comments.this);
            }}}); // end userPointer*/


        // MARK: - SEND COMMENT BUTTON ------------------------------------
        Button sendButt = findViewById(R.id.commSendButt);
        sendButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = commentTxt.getText().toString().trim();
                commentPost(postId, postType, parentComment, userid, comment);

            }
        });
          /*  sendButt.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if (commentTxt.getText().toString().matches("") ){
                      Configs.simpleAlert("You must type something!", Comments.this);

                  } else {
                      Configs.showPD("Please wait...", Comments.this);
                      ParseObject cObj = new ParseObject(Configs.COMMENTS_CLASS_NAME);
                      final ParseUser currUser = ParseUser.getCurrentUser();


                      // Save data
                      cObj.put(Configs.COMMENTS_USER_POINTER, currUser);
                      cObj.put(Configs.COMMENTS_STREAM_POINTER, sObj);
                      cObj.put(Configs.COMMENTS_COMMENT, commentTxt.getText().toString());
                      List<String>reportedBy = new ArrayList<>();
                      cObj.put(Configs.COMMENTS_REPORTED_BY, reportedBy);

                      // Saving block
                      cObj.saveInBackground(new SaveCallback() {
                          @Override
                          public void done(ParseException e) {
                              if (e == null) {
                                  Configs.hidePD();
                                  dismissKeyboard();
                                  commentTxt.setText("");

                                  // Increment comments of this Stream
                                  sObj.increment(Configs.STREAMS_COMMENTS, 1);
                                  sObj.saveInBackground();

                                  // Get userPointer
                                  sObj.getParseObject(Configs.STREAMS_USER_POINTER).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                                      public void done(ParseObject userPointer, ParseException e) {

                                          // Send push notification
                                          String pushMessage = currUser.getString(Configs.USER_FULLNAME) + " commented on your Stream: '"
                                                  + sObj.getString(Configs.STREAMS_TEXT) + "'";
                                          Configs.sendPushNotification(pushMessage, (ParseUser) userPointer, Comments.this);

                                          // Save Activity
                                          Configs.saveActivity(currUser, sObj, pushMessage);

                                      }});// end userPointer


                                  // Recall query
                                  commentPost();

                              // error
                              } else {
                                  Configs.hidePD();
                                  Configs.simpleAlert(e.getMessage(), Comments.this);
                      }}});
                  }
            }});


        } catch (ParseException e) {
            e.printStackTrace(); }
*/


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


    // MARK: - QUERY COMMENTS ------------------------------------------------
   /* void queryComments() {
        Configs.showPD("Please wait...", Comments.this);
        ParseUser currUser = ParseUser.getCurrentUser();
        List<String>currUserID = new ArrayList<>();
        currUserID.add(currUser.getObjectId());


        ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.COMMENTS_CLASS_NAME);
        query.whereEqualTo(Configs.COMMENTS_STREAM_POINTER, sObj);
        query.whereNotContainedIn(Configs.COMMENTS_REPORTED_BY, currUserID);
        query.orderByDescending(Configs.COMMENTS_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Configs.hidePD();
                    commentsArray = objects;
                     reloadData();

                } else {
                    Configs.hidePD();
                    Configs.simpleAlert(e.getMessage(), Comments.this);
            }}});
    }*/


    // MARK: - RELOAD LISTVIEW DATA ----------------------------------------------------------
  /*  void reloadData() {
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
                      cell = inflater.inflate(R.layout.cell_comment, null);
                  }

                  // Init views
                  final ImageView avatarImg = cell.findViewById(R.id.ccommAvatarimg);
                  final TextView fullnameTxt = cell.findViewById(R.id.ccommFullnameTxt);
                  fullnameTxt.setTypeface(Configs.titSemibold);
                  final TextView commentTxt = cell.findViewById(R.id.ccommCommTxt);
                  commentTxt.setTypeface(Configs.titRegular);
                  final TextView dateTxt = cell.findViewById(R.id.ccommDateTxt);
                  dateTxt.setTypeface(Configs.titRegular);
                  final Button optionsButt = cell.findViewById(R.id.ccommOptionsButt);

                  // Get Parse obj
                  final ParseObject cObj = commentsArray.get(position);

                  // Get userPointer
                  cObj.getParseObject(Configs.COMMENTS_USER_POINTER).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                      public void done(final ParseObject userPointer, ParseException e) {

                          // Get Avatar
                          Configs.getParseImage(avatarImg, userPointer, Configs.USER_AVATAR);

                          // TAP AVATAR TO SEE OTHE USER PROFILE -----------------------------
                          avatarImg.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  dismissKeyboard();

                                  Intent i = new Intent(Comments.this, OtherUserProfile.class);
                                  Bundle extras = new Bundle();
                                  extras.putString("userID", userPointer.getObjectId());
                                  i.putExtras(extras);
                                  startActivity(i);
                          }});


                          // Get full name
                          fullnameTxt.setText(userPointer.getString(Configs.USER_FULLNAME));

                          // Get comment
                          commentTxt.setText(cObj.getString(Configs.COMMENTS_COMMENT));

                          // Get comment date
                          Date now = new Date();
                          String cDate = Configs.timeAgoSinceDate(now);
                          dateTxt.setText(cDate);





                          // MARK: - OPTIONS BUTTON ---------------------------------------------
                          optionsButt.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  final ParseUser currUser = ParseUser.getCurrentUser();
                                  dismissKeyboard();

                                  AlertDialog.Builder alert  = new AlertDialog.Builder(Comments.this);
                                  alert.setTitle("SELECT SOURCE")
                                          .setIcon(R.drawable.logo)
                                          .setItems(new CharSequence[] {
                                                  "Report comment",
                                                  "Copy"
                                          }, new DialogInterface.OnClickListener() {
                                              public void onClick(DialogInterface dialog, int which) {
                                                  switch (which) {

                                                      // REPORT COMMENT -------------------------
                                                      case 0:
                                                          AlertDialog.Builder alert = new AlertDialog.Builder(Comments.this);
                                                          alert.setMessage("Are you sure you want to report this commment to the Admin?")
                                                                  .setTitle(R.string.app_name)
                                                                  .setPositiveButton("Report", new DialogInterface.OnClickListener() {
                                                                      @Override
                                                                      public void onClick(DialogInterface dialogInterface, int i) {
                                                                          Configs.showPD("Please wait...", Comments.this);
                                                                          List<String>reportedBy = cObj.getList(Configs.COMMENTS_REPORTED_BY);
                                                                          reportedBy.add(currUser.getObjectId());
                                                                          cObj.put(Configs.COMMENTS_REPORTED_BY, reportedBy);

                                                                          cObj.saveInBackground(new SaveCallback() {
                                                                              @Override
                                                                              public void done(ParseException e) {
                                                                                  if (e == null) {
                                                                                      Configs.hidePD();
                                                                                      Configs.simpleAlert("Thanks for reporting this comment! We'll check it out within 24h.", Comments.this);

                                                                                      // Remove selected row
                                                                                      commentsArray.remove(position);
                                                                                      commentsListView.invalidateViews();
                                                                                      commentsListView.refreshDrawableState();
                                                                              }}});
                                                                  }})
                                                                  .setNegativeButton("Cancel", null)
                                                                  .setIcon(R.drawable.logo);
                                                          alert.create().show();
                                                          break;



                                                      // COPY COMMMENT ---------------------------
                                                      case 1:
                                                          ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                                          ClipData clip = ClipData.newPlainText("Copied Text", cObj.getString(Configs.COMMENTS_COMMENT));
                                                          clipboard.setPrimaryClip(clip);
                                                      break;
                                              }}})
                                          .setNegativeButton("Cancel", null);
                                          alert.create().show();

                          }});// end optionsButt


                      }});// end userPointer


                  return cell;
              }

              @Override public int getCount() { return commentsArray.size(); }
              @Override public Object getItem(int position) { return commentsArray.get(position); }
              @Override public long getItemId(int position) { return position; }
          }


          // Init ListView and set its adapter
          commentsListView.setAdapter(new ListAdapter(Comments.this));


    }*/


    // MARK: - DISMISS KEYBOARD
    void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(commentTxt.getWindowToken(), 0);
    }


    // MARK: - REFRESH DATA ----------------------------------------
    @Override
    public void onRefresh() {
        // Recall query
       // commentPost(postId, postType, parentComment, userid, comment);
        GetPostComments(postId, postType);

        if (refreshControl.isRefreshing()) {
            refreshControl.setRefreshing(false);
        }
    }

    private void commentPost(String referenceId, String referenceTypeId, String parentCommentId, String interactionByUserId, final String comment) {

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, "https://app_api_json.veamex.com/api/common/NewComment?referenceId=" + referenceId + "&referenceTypeId=" + referenceTypeId + "&parentCommentId=" + parentCommentId + "&interactionByUserId=" + interactionByUserId + "&comment=" + comment, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideProgress(Comments.this);

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
                Toast.makeText(Comments.this, "Server errgor or No internet connection", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };
         showProgress(Comments.this, "Loading....", "Please wait!");
        RequestQueue requestQueue = Volley.newRequestQueue(Comments.this);
        requestQueue.add(stringRequest);

    }


    private void GetPostComments(String referenceId, String referenceTypeId) {

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, "https://app_api_json.veamex.com/api/common/GetPostComments?referenceId=" + referenceId + "&referenceTypeId=" + referenceTypeId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_LONG).show();
               hideProgress(Comments.this);


                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                CommentGsonModel[] commentGson = gson.fromJson(response, CommentGsonModel[].class);

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
                Toast.makeText(Comments.this, "Server error or No internet connection", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };
     showProgress(Comments.this, "Loading....", "Please wait!");
        RequestQueue requestQueue = Volley.newRequestQueue(Comments.this);
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        GetPostComments(postId, postType);
    }
}// @end
