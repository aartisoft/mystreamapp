package com.domain.mystream;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.domain.mystream.Adpater.MessageAdpater;
import com.domain.mystream.Adpater.NewChatAdapter;
import com.domain.mystream.Model.MessageModel;
import com.domain.mystream.Model.NewChatModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.domain.mystream.Login.editor;
import static com.domain.mystream.Login.myPref;

public class NewChat extends AppCompatActivity {
    RecyclerView renewchat;
    String userid, checked, userIds;
    NewChatAdapter newChatAdapter;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);

        renewchat = findViewById(R.id.newchatrecycler);
        LinearLayoutManager llmm = new LinearLayoutManager(NewChat.this);
        llmm.setOrientation(LinearLayoutManager.VERTICAL);
        renewchat.setLayoutManager(llmm);
        button = findViewById(R.id.next);

        final SharedPreferences sharedPreferences = getSharedPreferences(myPref, Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();

        userid = sharedPreferences.getString("userid", "0");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userIds = sharedPreferences.getString("check", "0");
                if (!userIds.equals(" ") || !userIds.equals("0"))
                    getMultipleUser(userid, userIds);
            }
        });
        getConnectedUsers(userid);
    }


    private void getConnectedUsers(String userId) {

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, "https://app_api_json.veamex.com/api/common/GetConnectedUsers?userId=" + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_LONG).show();
                /*   hideProgress(getActivity());*/


                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                NewChatModel[] newChatModels = gson.fromJson(response, NewChatModel[].class);

                newChatAdapter = new NewChatAdapter(NewChat.this, newChatModels);
                renewchat.setAdapter(newChatAdapter);
                newChatAdapter.notifyDataSetChanged();

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
                Toast.makeText(NewChat.this, "Server error or No internet connection", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };
        /* showProgress(getActivity(), "Loading....", "Please wait!");*/
        RequestQueue requestQueue = Volley.newRequestQueue(NewChat.this);
        requestQueue.add(stringRequest);

    }


    private void getMultipleUser(String userid, String checked) {

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, "https://app_api_json.veamex.com/api/common/GetMultipleChatParticipants?userId=" + userid + "&userIds=" + checked, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_LONG).show();
                /*   hideProgress(getActivity());*/
                finish();


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
                Toast.makeText(NewChat.this, "Server error or No internet connection", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };
        /* showProgress(getActivity(), "Loading....", "Please wait!");*/
        RequestQueue requestQueue = Volley.newRequestQueue(NewChat.this);
        requestQueue.add(stringRequest);

    }
}
