package com.domain.mystream.Activity;

/* ================================

    - MyStream -

    created by cubycode @2018
    All Rights reserved

===================================*/

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.domain.mystream.Adpater.InboxAdpater;
import com.domain.mystream.Constants.Configs;
import com.domain.mystream.Constants.MarshMallowPermission;
import com.domain.mystream.Model.GetChatModel;
import com.domain.mystream.Model.InboxChat;
import com.domain.mystream.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import static com.domain.mystream.Constants.Configs.editor;
import static com.domain.mystream.Constants.Configs.error_toast;
import static com.domain.mystream.Constants.Configs.myPref;
import static com.domain.mystream.Constants.Configs.sharedPreferences;
import static com.domain.mystream.Constants.Configs.user_id;
import static com.domain.mystream.Constants.MyStreamApis.GET_CHAT;
import static com.domain.mystream.Constants.MyStreamApis.INSERT_CHAT;

public class InboxActivity extends AppCompatActivity {

    /* Views */
    Button optionsButt, sendButt, uploadPicButt;
    TextView usernameTxt;
    EditText messageTxt;
    RecyclerView iListView;
    ImageView imgPreview;
    InboxChat inboxChat;
    String iD, sentOn, userName;
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
        sharedPreferences = getSharedPreferences(myPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        user_id = sharedPreferences.getString("userid", "0");

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


        // MARK: - SEND MESSAGE BUTTON ---------------------------------------------
        sendButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (messageTxt.getText().toString().matches("")) {
                    Configs.simpleAlert("You must type something or send a picture!", InboxActivity.this);
                } else {

                    jsonObject = new JSONObject();
                    try {
                        int po = getChatModel.length - 1;
                        jsonObject.put("Identifier", iD);
                        jsonObject.put("IsRemoved", getChatModel[po].getIsRemoved());
                        jsonObject.put("ChatId", getChatModel[po].getChatId());
                        jsonObject.put("ChatMessageId", getChatModel[po].getChatMessageId());
                        jsonObject.put("IsRead", getChatModel[po].getIsRead());
                        jsonObject.put("MessageBody", messageTxt.getText().toString());
                        jsonObject.put("SenderId", Integer.parseInt(user_id));
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
        stringRequest = new StringRequest(Request.Method.GET, GET_CHAT+"id=" + iD + "&userId=" + user_id, new Response.Listener<String>() {
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
                Toast.makeText(InboxActivity.this, error_toast, Toast.LENGTH_LONG).show();
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, INSERT_CHAT, new Response.Listener<String>() {
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

                Toast.makeText(InboxActivity.this, error_toast, Toast.LENGTH_LONG).show();
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
