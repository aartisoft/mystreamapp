package com.domain.mystream.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.domain.mystream.Constants.Configs;
import com.domain.mystream.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.domain.mystream.Activity.StreamDetails.hideProgress;
import static com.domain.mystream.Activity.StreamDetails.showProgress;
import static com.domain.mystream.Constants.Configs.editor;
import static com.domain.mystream.Constants.Configs.myPref;
import static com.domain.mystream.Constants.Configs.myPref;
import static com.domain.mystream.Constants.Configs.myPref;
import static com.domain.mystream.Constants.Configs.myPref;
import static com.domain.mystream.Constants.Configs.myPref;
import static com.domain.mystream.Constants.Configs.sharedPreferences;
import static com.domain.mystream.Constants.Configs.user_id;
import static com.domain.mystream.Constants.MyStreamApis.BASE_URL;
import static com.domain.mystream.Constants.MyStreamApis.SHARE_POST;


public class ShareActivity extends AppCompatActivity {
    ImageView profilePic;
    TextView profileName;
    EditText editText;
    WebView shareWeb;
    Button button;
    String postid, postbody, posttype, fullname, img, username, postText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        sharedPreferences = getSharedPreferences(myPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        user_id = sharedPreferences.getString("userid", "0");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            postid = extras.getString("PostId");
            postbody = extras.getString("PostBody");
            posttype = extras.getString("PostTypeId");
            fullname = extras.getString("FullName");
            img = extras.getString("Image");

        }

        profileName = findViewById(R.id.sFullnameTxt);
        profileName.setText(fullname);
        profilePic = findViewById(R.id.sAvatarImg);
        Glide.with(ShareActivity.this)
                .load(img)
                .into(profilePic);
        editText = findViewById(R.id.edittext);


        shareWeb = findViewById(R.id.shareweb);
        String summary = postbody;
        String c = summary.replace("src=\"", "src=\"https://app_api_json.veamex.com");
        String head = "<!DOCTYPE html><html><head><meta name=\"viewport\" content=\"width=device-width, user-scalable=yes\" /></head>";
        String html = head + "<body style='background-color:#ffffff;'>" + c + "</body></html>";
        shareWeb.loadData(html, "text/html", null);

        WebSettings webSettings = shareWeb.getSettings();
        webSettings.setDomStorageEnabled(true);

        button = findViewById(R.id.profileButt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postText = editText.getText().toString();
                sharePost(postid, user_id, postText);
            }
        });


    }

    private void sharePost(final String postid, final String userid, final String postText) {
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, SHARE_POST+"postId=" + postid + "&currentUserId=" + userid + "&additionalPostBody=" + postText, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //  Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_LONG).show();
                hideProgress(ShareActivity.this);


                AlertDialog.Builder alert = new AlertDialog.Builder(ShareActivity.this);
                alert.setMessage("Your post has been share!")
                        .setTitle(R.string.app_name)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Dismiss Activity
                                Configs.mustRefresh = true;
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .setIcon(R.drawable.logo);
                alert.create().show();
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }
        };
        showProgress(ShareActivity.this, "Loading....", "Please wait!");
        RequestQueue requestQueue = Volley.newRequestQueue(ShareActivity.this);
        requestQueue.add(stringRequest);

    }
}
