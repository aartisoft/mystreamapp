package com.domain.mystream;

/* ================================

    - MyStream -

    created by cubycode @2018
    All Rights reserved

===================================*/

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.domain.mystream.Adpater.InboxAdpater;
import com.domain.mystream.Adpater.MessageAdpater;
import com.domain.mystream.Model.GetChatModel;
import com.domain.mystream.Model.InboxChat;
import com.domain.mystream.Model.MessageModel;
import com.facebook.appevents.internal.AutomaticAnalyticsLogger;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.cast.framework.media.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.domain.mystream.Login.editor;
import static com.domain.mystream.Login.myPref;

public class InboxActivity extends AppCompatActivity {

    /* Views */
    Button optionsButt, sendButt, uploadPicButt;
    TextView usernameTxt;
    EditText messageTxt;
    RecyclerView iListView;
    ImageView imgPreview;
    InboxChat inboxChat;
    String iD, userid, sentOn, userName;
    JSONObject jsonObject;
    Integer chatMessageId, chatId, receiverId;
    Boolean isRead, isRemoved;
    GetChatModel[] getChatModel;

    /* Variables */
    ParseUser userObj;
    ParseObject adObj;
    List<ParseObject> inboxArray;
    List<ParseObject> chatsArray;
    String lastMessageStr = null;
    Timer refreshTimerForInboxQuery = new Timer();
    Bitmap imageToSend = null;
    MarshMallowPermission mmp = new MarshMallowPermission(this);
    InboxAdpater inboxAdpater;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox_activity);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Hide ActionBar
//        getSupportActionBar().hide();
        final SharedPreferences sharedPreferences = getSharedPreferences(myPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userid = sharedPreferences.getString("userid", "0");

        // Get objectID from previous .java
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        iD = extras.getString("identifier");
        userName = extras.getString("username");

        optionsButt = findViewById(R.id.inOptionsButt);
        uploadPicButt = findViewById(R.id.inUploadPicButt);
        sendButt = findViewById(R.id.inSendButt);
        sendButt.setTypeface(Configs.titSemibold);
        usernameTxt = findViewById(R.id.inUsernameTxt);
        usernameTxt.setTypeface(Configs.titSemibold);
        usernameTxt.setText(userName);
        messageTxt = findViewById(R.id.inMessageTxt);
        messageTxt.setTypeface(Configs.titRegular);

        iListView = findViewById(R.id.inInboxListView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        iListView.setLayoutManager(linearLayoutManager);
        iListView.setHasFixedSize(true);
        linearLayoutManager.setStackFromEnd(true);
        // Hide imgPreview
        imgPreview = (ImageView) findViewById(R.id.inImagePreview);
        ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(imgPreview.getLayoutParams());
        marginParams.setMargins(0, 5000, 0, 0);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
        imgPreview.setLayoutParams(layoutParams);

        // Hide imgPreview on click
        imgPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(imgPreview.getLayoutParams());
                marginParams.setMargins(0, 5000, 0, 0);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
                imgPreview.setLayoutParams(layoutParams);
            }
        });


        // Get userObj
/*
        userObj = (ParseUser) ParseUser.createWithoutData(Configs.USER_CLASS_NAME, userID);
        try { userObj.fetchIfNeeded().getParseUser(Configs.USER_CLASS_NAME);

                // Call query
                queryInbox();

                // Get User's username
                usernameTxt.setText("@" + userObj.getString(Configs.USER_USERNAME));


                // Start refresh Timer for Inbox query
                startRefreshTimer();

                ParseUser currUser = ParseUser.getCurrentUser();
                List<String>hasBlocked = currUser.getList(Configs.USER_HAS_BLOCKED);
                Log.i("log-", "HAS BLOCKED: " + hasBlocked);

            } catch (ParseException e) { e.printStackTrace(); }
 */


        // MARK: - UPLOAD IMAGE BUTTON ------------------------------------
   /*     uploadPicButt.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              AlertDialog.Builder alert = new AlertDialog.Builder(InboxActivity.this);
              alert.setMessage("Select source")
                      .setTitle(R.string.app_name)
                      .setPositiveButton("Take a Picture", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {
                              if (!mmp.checkPermissionForCamera()) {
                                  mmp.requestPermissionForCamera();
                              } else {
                                  openCamera();
                              }
                          }})

                      .setNegativeButton("Pick from Gallery", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {
                              if (!mmp.checkPermissionForReadExternalStorage()) {
                                  mmp.requestPermissionForReadExternalStorage();
                              } else {
                                  openGallery();
                              }
                          }})

                      .setNeutralButton("Cancel", null)
                      .setIcon(R.drawable.logo);
              alert.create().show();
         }});

*/


        // MARK: - SEND MESSAGE BUTTON ---------------------------------------------
        sendButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (messageTxt.getText().toString().matches("")) {
                    Configs.simpleAlert("You must type something or send a picture!", InboxActivity.this);
                } else {
                    // getChat();
                  /*  chatId = sharedPreferences.getInt("chatid", 0);
                    chatMessageId = sharedPreferences.getInt("chatmsgid", 0);
                    sentOn = sharedPreferences.getString("senton", "0");
                    isRead = sharedPreferences.getBoolean("isread", false);
                    isRemoved = sharedPreferences.getBoolean("isremoved", false);
                    receiverId = sharedPreferences.getInt("revId", 0);
*/

                   /* Integer chatId = getChatModel[i].getChatId();
                    Integer chatMessageId = getChatModel[i].getChatMessageId();
                    Boolean isRead = getChatModel[i].getIsRead();
                    Boolean isRemoved = getChatModel[i].getIsRemoved();
                    String sentOn = getChatModel[i].getSentOn();
                    Integer receiverId =getChatModel[i].getReceiverId();

                    editor.putInt("chatid", chatId);
                    editor.putInt("chatmsgid", chatMessageId);
                    editor.putBoolean("isread", isRead);
                    editor.putBoolean("isremoved", isRemoved);
                    editor.putString("senton", sentOn);
                    editor.putInt("revId",receiverId);
                    editor.commit();
*/

                    jsonObject = new JSONObject();
                    try {
                        int po=getChatModel.length-1;
                        jsonObject.put("Identifier", iD);
                        jsonObject.put("IsRemoved", getChatModel[po].getIsRemoved());
                        jsonObject.put("ChatId", getChatModel[po].getChatId());
                        jsonObject.put("ChatMessageId", getChatModel[po].getChatMessageId());
                        jsonObject.put("IsRead", getChatModel[po].getIsRead());
                        jsonObject.put("MessageBody", messageTxt.getText().toString());
                        jsonObject.put("SenderId", Integer.parseInt(userid));
                        jsonObject.put("ReceiverId", getChatModel[po].getReceiverId());
                        jsonObject.put("SentOn", getChatModel[po].getSentOn());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    getMessage(jsonObject);
                }
            }
        });

/*
        // OPTIONS BUTTON -------------------------------------------------
        optionsButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ParseUser currUser = ParseUser.getCurrentUser();
                final List<String> hasBlocked = currUser.getList(Configs.USER_HAS_BLOCKED);

                // Set blockUser  Action title
                String blockTitle = "";
                if (hasBlocked.contains(userObj.getObjectId())) {
                    blockTitle = "Unblock User";
                } else {
                    blockTitle = "Block User";
                }


                AlertDialog.Builder alert = new AlertDialog.Builder(InboxActivity.this);
                final String finalBlockTitle = blockTitle;
                AlertDialog.Builder builder = alert.setTitle("Select option")
                        .setIcon(R.drawable.logo)
                        .setItems(new CharSequence[]{
                                blockTitle,
                                "Delete Chat"
                        }, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {

                                    // BLOCK/UNBLOCK USER --------------------------------------------------
                                    case 0:

                                        // Block User
                                        if (finalBlockTitle.matches("Block User")) {
                                            hasBlocked.add(userObj.getObjectId());
                                            currUser.put(Configs.USER_HAS_BLOCKED, hasBlocked);
                                            currUser.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    AlertDialog.Builder alert = new AlertDialog.Builder(InboxActivity.this);
                                                    alert.setMessage("You've blocked this User, you will no longer get Chat messages from @" + userObj.getString(Configs.USER_USERNAME))
                                                            .setTitle(R.string.app_name)
                                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    finish();
                                                                }
                                                            })
                                                            .setIcon(R.drawable.logo);
                                                    alert.create().show();
                                                }
                                            });

                                            // Unblock User
                                        } else {
                                            hasBlocked.remove(userObj.getObjectId());
                                            currUser.put(Configs.USER_HAS_BLOCKED, hasBlocked);
                                            currUser.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    Configs.simpleAlert("You've unblocked @" + userObj.getString(Configs.USER_USERNAME), InboxActivity.this);
                                                }
                                            });
                                        }

                                        break;


                                    // DELETE CHAT --------------------------------------------------------
                                    case 1:

                                        AlertDialog.Builder alert = new AlertDialog.Builder(InboxActivity.this);
                                        alert.setMessage("Are you sure you want to delete this Chat? @" + userObj.getString(Configs.USER_USERNAME) + " will not be able to see these messages either.")
                                                .setTitle(R.string.app_name)
                                                .setPositiveButton("Delete Chat", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                        // Delete all Inbox messages
                                                        for (int j = 0; j < inboxArray.size(); j++) {
                                                            ParseObject iObj = inboxArray.get(j);
                                                            iObj.deleteInBackground(new DeleteCallback() {
                                                                @Override
                                                                public void done(ParseException e) {
                                                                    Log.i("log-", "DELETED INBOX ITEM!");
                                                                }
                                                            });
                                                        }


                                                        // Delete Chat in Chats class
                                                        String inboxId1 = ParseUser.getCurrentUser().getObjectId() + userObj.getObjectId();
                                                        String inboxId2 = userObj.getObjectId() + ParseUser.getCurrentUser().getObjectId();

                                                        final ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.MESSAGES_CLASS_NAME);
                                                        String[] ids = {inboxId1, inboxId2};
                                                        query.whereContainedIn("chatID", Arrays.asList(ids));
                                                        query.whereContainedIn("chatID", Arrays.asList(ids));

                                                        query.findInBackground(new FindCallback<ParseObject>() {
                                                            public void done(List<ParseObject> objects, ParseException error) {
                                                                if (error == null) {
                                                                    chatsArray = objects;
                                                                    ParseObject chatsObj = chatsArray.get(0);
                                                                    chatsObj.deleteInBackground(new DeleteCallback() {
                                                                        @Override
                                                                        public void done(ParseException e) {
                                                                            if (e == null) {
                                                                                Log.i("log-", "CHAT DELETED IN THE 'Messages' CLASS");
                                                                                finish();
                                                                            }
                                                                        }
                                                                    });

                                                                }
                                                            }
                                                        });

                                                    }
                                                })// end alert

                                                .setNegativeButton("Cancel", null)
                                                .setIcon(R.drawable.logo);
                                        alert.create().show();

                                        break;

                                }
                            }
                        })
                        .setNegativeButton("Cancel", null);
                alert.create().show();

            }
        });*/


        // BACK BUTTON -------------------------------------------------
        Button backButt = findViewById(R.id.inBackButt);
        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshTimerForInboxQuery.cancel();
                imageToSend = null;
                finish();
            }
        });

        getChat();
    }


    // RESET imageToSend TO NULL -------------------------------------------------
    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageToSend = null;
    }


    // MARK: - DISMISS KEYBOARD ---------------------------------------------------------------
    public void dismisskeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(messageTxt.getWindowToken(), 0);
        messageTxt.setText("");
    }

    private void getChat() {

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, "https://app_api_json.veamex.com/api/common/GetChatMessages?id=" + iD + "&userId=" + userid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                getChatModel = gson.fromJson(response, GetChatModel[].class);



                inboxAdpater = new InboxAdpater(InboxActivity.this, getChatModel);
                iListView.setAdapter(inboxAdpater);
                inboxAdpater.notifyDataSetChanged();

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
                Toast.makeText(InboxActivity.this, "Server error or No internet connection", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };
        /* showProgress(getActivity(), "Loading....", "Please wait!");*/
        RequestQueue requestQueue = Volley.newRequestQueue(InboxActivity.this);
        requestQueue.add(stringRequest);

    }

    public void getMessage(JSONObject jsonObject) {

        final String requestBody = jsonObject.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://app_api_json.veamex.com/api/common/InsertUpdateChatMessages", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  textView.setText(response);
                /*  Toast.makeText(InboxActivity.this, response.toString(), Toast.LENGTH_LONG).show();*/
                getChat();
                messageTxt.setText("");
                inboxAdpater.notifyDataSetChanged();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textView.setText(error.toString());

                Toast.makeText(InboxActivity.this, "No Internet Connection or Request time out", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(InboxActivity.this);
        requestQueue.add(stringRequest);

    }


}
