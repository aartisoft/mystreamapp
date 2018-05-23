package com.domain.mystream;

/* ================================

    - MyStream -

    created by cubycode @2018
    All Rights reserved

===================================*/

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import com.domain.mystream.Model.CommentGsonModel;
import com.domain.mystream.Model.PostModel;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.domain.mystream.Login.editor;
import static com.domain.mystream.Login.myPref;

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
        SharedPreferences sharedPreferences = getSharedPreferences(myPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
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
        String c = summary.replace("src=\"","src=\"https://qas.veamex.com");
        String head = "<!DOCTYPE html><html><head><meta name=\"viewport\" content=\"width=device-width, user-scalable=yes\" /></head>";
        String html = head + "<body style='background-color:#ffffff;'>" + c + "</body></html>";
        webViewStream.loadData(html, "text/html", null);



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


        // Get objectID from previous .java
        // Bundle extras = getIntent().getExtras();
        String objectID = extras.getString("objectID");
        sObj = ParseObject.createWithoutData(Configs.STREAMS_CLASS_NAME, objectID);
       /* try { sObj.fetchIfNeeded().getParseObject(Configs.STREAMS_CLASS_NAME);

            // SHOW STREAM AND USER'S DETAILS ----------
            final ParseUser currUser = ParseUser.getCurrentUser();

            // Increment Stream views
            sObj.increment(Configs.STREAMS_VIEWS, 1);
            sObj.saveInBackground();


            // Get User Pointer
            sObj.getParseObject(Configs.STREAMS_USER_POINTER).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                public void done(final ParseObject userPointer, ParseException e) {
                    if (e == null) {

                        // Get full name
                        fullnameTxt.setText(userPointer.getString(Configs.USER_FULLNAME));

                        // Get Avatar
                        Configs.getParseImage(avatarImg, userPointer, Configs.USER_AVATAR);
                        avatarImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Increment profile clicks
                                sObj.increment(Configs.STREAMS_PROFILE_CLICKS, 1);
                                sObj.saveInBackground();

                                // Go to OtherUserProfile
                                Intent i = new Intent(StreamDetails.this, OtherUserProfile.class);
                                Bundle extras = new Bundle();
                                extras.putString("userID", userPointer.getObjectId());
                                i.putExtras(extras);
                                startActivity(i);
                        }});

                        // Get Username
                        usernameTxt.setText("@" + userPointer.getString(Configs.USER_USERNAME));

                        // Get Stream text
                        streamTxt.setText(sObj.getString(Configs.STREAMS_TEXT));


                        // Get like/liked
                        List<String> likedBy = sObj.getList(Configs.STREAMS_LIKED_BY);
                        if (likedBy.contains(currUser.getObjectId())) {
                            likeButt.setBackgroundResource(R.drawable.liked_butt_small);
                        } else {
                            likeButt.setBackgroundResource(R.drawable.like_butt_small);
                        }

                        // Get Likes
                        int likes = sObj.getInt(Configs.STREAMS_LIKES);
                        likesTxt.setText(Configs.roundThousandsIntoK(likes));

                        // Get Comments
                        int comments = sObj.getInt(Configs.STREAMS_COMMENTS);
                        commentsTxt.setText(Configs.roundThousandsIntoK(comments));


                        // Get Stream Image (if any)
                        if (sObj.getParseFile(Configs.STREAMS_IMAGE) != null) {
                            Configs.getParseImage(streamImg, sObj, Configs.STREAMS_IMAGE);
                        } else {
                            streamImg.getLayoutParams().height = 1;
                        }

                        // Get Stream Video (if any)
                        if (sObj.getParseFile(Configs.STREAMS_VIDEO) != null) {
                            playButt.setVisibility(View.VISIBLE);
                        }

                        // Get Stream Audio (if any)
                        if (sObj.getParseFile(Configs.STREAMS_AUDIO) != null) {
                            playButt.setVisibility(View.VISIBLE);
                            playingTimeTxt.setVisibility(View.VISIBLE);
                        }




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
                                    Configs.sendPushNotification(pushMessage, (ParseUser) userPointer, StreamDetails.this);

                                    // Save Activity
                                    Configs.saveActivity(currUser, sObj, pushMessage);
                                }
                            }});




                        // MARK: - COMMENTS BUTTON ------------------------------------
                        commentsButt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                  Intent i = new Intent(StreamDetails.this, Comments.class);
                                  Bundle extras = new Bundle();
                                  extras.putString("objectID", sObj.getObjectId());
                                  i.putExtras(extras);
                                  startActivity(i);
                        }});






                        // MARK: - SHARE BUTTON ------------------------------------
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
                                        bitmap = BitmapFactory.decodeResource(StreamDetails.this.getResources(), R.drawable.logo);
                                    }
                                    Uri uri = Configs.getImageUri(StreamDetails.this, bitmap);
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






                        // MARK: - PLAY VIDEO OR AUDIO BUTTON -----------------------------------------------
                        playButt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                // PLAY VIDEO PREVIEW -------------------
                                if (sObj.getParseFile(Configs.STREAMS_VIDEO) != null ){
                                    ParseFile videoFile = sObj.getParseFile(Configs.STREAMS_VIDEO);
                                    String videoURL = videoFile.getUrl();

                                    Intent i = new Intent(StreamDetails.this, ShowVideo.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("videoURL", videoURL);
                                    i.putExtras(extras);
                                    startActivity(i);




                                // PLAY AUDIO PREVIEW ----------------------
                                } else if (sObj.getParseFile(Configs.STREAMS_AUDIO) != null) {
                                    ParseFile audioFile = sObj.getParseFile(Configs.STREAMS_AUDIO);
                                    String audioURL = audioFile.getUrl();

                                    // Init mediaPlayer
                                    mediaPlayer = new MediaPlayer();

                                    // Start Audio playing
                                    if (!audioIsPlaying) {
                                        try {
                                            mediaPlayer.setDataSource(audioURL);
                                            mediaPlayer.prepare();
                                        } catch (IOException e) { e.printStackTrace(); }
                                        mediaPlayer.start();

                                        // SET PLAYER TIMER
                                        playingTimeTxt.setText("00:00");

                                        handler.postDelayed(new Runnable()  {
                                            long time = 0;
                                            @Override
                                            public void run() {
                                                time += 1000;

                                                @SuppressLint("DefaultLocale")
                                                String formattedTimer = String.format("%02d:%02d",
                                                        TimeUnit.MILLISECONDS.toMinutes(time),
                                                        TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
                                                );
                                                playingTimeTxt.setText(formattedTimer);

                                                handler.postDelayed(this, 1000);
                                        }}, 1000); // 1 second delay


                                        audioIsPlaying = true;
                                        playButt.setBackgroundResource(R.drawable.stop_butt);

                                        // Check when audio finished
                                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                            @Override
                                            public void onCompletion(MediaPlayer mp) {
                                                handler.removeCallbacksAndMessages(null);
                                                audioIsPlaying = false;
                                                playButt.setBackgroundResource(R.drawable.play_butt);
                                        }});


                                    // Stop Audio playing
                                    } else {
                                        handler.removeCallbacksAndMessages(null);
                                        mediaPlayer.stop();
                                        mediaPlayer.reset();
                                        mediaPlayer.release();
                                        mediaPlayer = null;
                                        audioIsPlaying = false;
                                        playButt.setBackgroundResource(R.drawable.play_butt);
                                    }

                                }// end IF
                            }});






                        // MARK: - OPTIONS BUTTON ------------------------------------
                        optionsButt.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {



                              AlertDialog.Builder alert = new AlertDialog.Builder(StreamDetails.this);
                              alert.setTitle("SELECT SOURCE")
                                      .setIcon(R.drawable.logo)
                                      .setItems(new CharSequence[]{
                                              "Delete"

                                      }, new DialogInterface.OnClickListener() {
                                          public void onClick(DialogInterface dialog, int which) {
                                              switch (which) {


                                                  // REPORT COMMENT -------------------------

                                                  case 0:

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
                                                      break;
                                                  // -------------------------------------------------------
                                                  case 1:
                                                      AlertDialog.Builder alert1 = new AlertDialog.Builder(StreamDetails.this);
                                                      alert1.setMessage("Are you sure you want to report this commment to the Admin?")
                                                              .setTitle(R.string.app_name)
                                                              .setPositiveButton("Report", new DialogInterface.OnClickListener() {
                                                                  @Override
                                                                  public void onClick(DialogInterface dialogInterface, int i) {

                                                                  }
                                                              })
                                                              .setNegativeButton("Cancel", null)
                                                              .setIcon(R.drawable.logo);
                                                      alert1.create().show();
                                                      break;


                                                  // COPY COMMMENT ---------------------------
                                                  case 2:
                                     *//*   ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData clip = ClipData.newPlainText("Copied Text", cObj.getString(Configs.COMMENTS_COMMENT));
                                        clipboard.setPrimaryClip(clip);*//*
                                                      break;
                                              }
                                          }
                                      })
                                      .setNegativeButton("Cancel", null);
                              alert.create().show();



*//*                              // Init list Items
                              List<String> listItems = new ArrayList<String>();
                              listItems.add("Report Stream");
                              listItems.add("Report @" + userPointer.getString(Configs.USER_USERNAME));
                              if (userPointer.getObjectId().matches(currUser.getObjectId())) {
                                  listItems.add("(X) Delete Stream");
                              }
                              final CharSequence[] options = listItems.toArray(new CharSequence[listItems.size()]);

                              // Fire alert
                              AlertDialog.Builder alert = new AlertDialog.Builder(StreamDetails.this);
                              alert.setTitle("SELECT SOURCE")
                                      .setIcon(R.drawable.logo)
                                      .setItems(options, new DialogInterface.OnClickListener() {
                                          public void onClick(DialogInterface dialog, int which) {
                                              switch (which) {

                                                  // REPORT STREAM ---------------------
                                                  case 0:
                                                      AlertDialog.Builder alert = new AlertDialog.Builder(StreamDetails.this);
                                                      alert.setMessage("Are you sure you want to report this Stream to the Admin?")
                                                              .setTitle(R.string.app_name)
                                                              .setPositiveButton("Report", new DialogInterface.OnClickListener() {
                                                                  @Override
                                                                  public void onClick(DialogInterface dialogInterface, int i) {
                                                                      Configs.showPD("Reporting Stream...", StreamDetails.this);
                                                                      List<String> reportedBy = sObj.getList(Configs.STREAMS_REPORTED_BY);
                                                                      reportedBy.add(currUser.getObjectId());
                                                                      sObj.put(Configs.STREAMS_REPORTED_BY, reportedBy);

                                                                      sObj.saveInBackground(new SaveCallback() {
                                                                          @Override
                                                                          public void done(ParseException e) {
                                                                              if (e == null) {
                                                                                  Configs.hidePD();
                                                                                  Configs.simpleAlert("Thanks for reporting this Stream, we'll check it out withint 24 hours!", StreamDetails.this);
                                                                                  Configs.mustRefresh = true;
                                                                              }}});

                                                              }})
                                                              .setNegativeButton("Cancel", null)
                                                              .setIcon(R.drawable.logo);
                                                      alert.create().show();

                                                      break;





                                                  // REPORT USER ----------------------------------
                                                  case 1:
                                                      AlertDialog.Builder alert2 = new AlertDialog.Builder(StreamDetails.this);
                                                      alert2.setMessage("Are you sure you want to report @" + userPointer.getString(Configs.USER_USERNAME) + " to the Admin?")
                                                              .setTitle(R.string.app_name)
                                                              .setPositiveButton("Report @" + userPointer.getString(Configs.USER_USERNAME), new DialogInterface.OnClickListener() {
                                                                  @Override
                                                                  public void onClick(DialogInterface dialogInterface, int i) {
                                                                      Configs.showPD("Reporting User...", StreamDetails.this);

                                                                      // Report user via Cloud Code
                                                                      HashMap<String, Object> params = new HashMap<String, Object>();
                                                                      params.put("userId", userPointer.getObjectId());
                                                                      params.put("reportMessage", "OFFENSIVE USER");

                                                                      ParseCloud.callFunctionInBackground("reportUser", params, new FunctionCallback<ParseUser>() {
                                                                          public void done(ParseUser user, ParseException error) {
                                                                              if (error == null) {
                                                                                  Configs.hidePD();
                                                                                  Configs.simpleAlert("Thanks for reporting " + userPointer.getString(Configs.USER_FULLNAME) + ". We'll check it out within 24h.", StreamDetails.this);

                                                                                  Configs.mustRefresh = true;

                                                                                  // Automatically report all User's streams
                                                                                  ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.STREAMS_CLASS_NAME);
                                                                                  query.whereEqualTo(Configs.STREAMS_USER_POINTER, userPointer);
                                                                                  query.findInBackground(new FindCallback<ParseObject>() {
                                                                                      @Override
                                                                                      public void done(List<ParseObject> objects, ParseException e) {
                                                                                          for (int i = 0; i < objects.size(); i++) {
                                                                                              ParseObject stObj = objects.get(i);
                                                                                              List<String> reportedBy = stObj.getList(Configs.STREAMS_REPORTED_BY);
                                                                                              reportedBy.add(currUser.getObjectId());
                                                                                              stObj.put(Configs.STREAMS_REPORTED_BY, reportedBy);
                                                                                              stObj.saveInBackground();
                                                                                          }
                                                                                      }
                                                                                  });


                                                                                  // Automatically report all User's comments
                                                                                  ParseQuery<ParseObject> query2 = ParseQuery.getQuery(Configs.COMMENTS_CLASS_NAME);
                                                                                  query2.whereEqualTo(Configs.COMMENTS_USER_POINTER, userPointer);
                                                                                  query2.findInBackground(new FindCallback<ParseObject>() {
                                                                                      @Override
                                                                                      public void done(List<ParseObject> objects, ParseException e) {

                                                                                          for (int i = 0; i < objects.size(); i++) {
                                                                                              ParseObject commObj = objects.get(i);
                                                                                              List<String> reportedBy = commObj.getList(Configs.COMMENTS_REPORTED_BY);
                                                                                              reportedBy.add(currUser.getObjectId());
                                                                                              commObj.put(Configs.COMMENTS_REPORTED_BY, reportedBy);
                                                                                              commObj.saveInBackground();
                                                                                          }
                                                                                      }
                                                                                  });

                                                                                  // Error in Cloud Code
                                                                              } else {
                                                                                  Configs.hidePD();
                                                                                  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                                                              }
                                                                          }
                                                                      });

                                                                  }
                                                              })
                                                              .setNegativeButton("Cancel", null)
                                                              .setIcon(R.drawable.logo);
                                                      alert2.create().show();

                                                      break;





                                                  // DELETE STREAM (IF IT'S YOURS) -----------------------------------------
                                                  case 2:
                                                      Configs.showPD("Please wait..", StreamDetails.this);
                                                      sObj.deleteInBackground(new DeleteCallback() {
                                                          @Override
                                                          public void done(ParseException e) {
                                                              if (e == null) {
                                                                  Configs.hidePD();
                                                                  Configs.mustRefresh = true;

                                                                  // Delete those rows from the Activity class which have this Stream as a Pointer
                                                                  ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.ACTIVITY_CLASS_NAME);
                                                                  query.whereEqualTo(Configs.ACTIVITY_STREAM_POINTER, sObj);
                                                                  query.findInBackground(new FindCallback<ParseObject>() {
                                                                      @Override
                                                                      public void done(List<ParseObject> objects, ParseException e) {
                                                                          if (e == null) {
                                                                              for (int i = 0; i < objects.size(); i++) {
                                                                                  ParseObject aObj = objects.get(i);
                                                                                  aObj.deleteInBackground();

                                                                                  if (i == objects.size()-1) { finish(); }
                                                                              }
                                                                          } else {
                                                                              Configs.simpleAlert(e.getMessage(), StreamDetails.this);
                                                                  }}});

                                                      }}});

                                                  break;


                              }}})
                              .setNegativeButton("Cancel", null);
                              alert.create().show();*//*
                        }});


                    // error
                    } else {
                        Configs.hidePD();
                        Configs.simpleAlert(e.getMessage(), StreamDetails.this);

            }}});// end userPointer



        } catch (ParseException e) { e.printStackTrace(); }
*/


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
        stringRequest = new StringRequest(Request.Method.GET, "https://app_api_json.veamex.com/api/common/DeletePost?postId=" + String.valueOf(postId), new Response.Listener<String>() {
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
        stringRequest = new StringRequest(Request.Method.GET, "https://app_api_json.veamex.com/api/common/NewPostReaction?referenceId=" + referenceId + "&referenceTypeId=" + referenceTypeId + "&reactionTypeId=" + reactionTypeId + "&interactionByUserId=" + interactionByUserId, new Response.Listener<String>() {
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
