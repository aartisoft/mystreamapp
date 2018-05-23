package com.domain.mystream;

/* ================================

    - MyStream -

    created by cubycode @2018
    All Rights reserved

===================================*/

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.domain.mystream.Adpater.CommentsAdpater;
import com.domain.mystream.Adpater.MessageAdpater;
import com.domain.mystream.Model.CommentGsonModel;
import com.domain.mystream.Model.MessageModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.domain.mystream.StreamDetails.hideProgress;
import static com.domain.mystream.StreamDetails.showProgress;

public class Messages extends Fragment {


    /* Views */
    RecyclerView messListView;
    RelativeLayout noMessLayout;


    /* Variables */
    List<ParseObject> messagesArray;
    ImageView newchat;
    MessageAdpater messageAdpater;


    public static Messages newInstance() {
        Messages fragment = new Messages();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.messages, container, false);


        // Hide ActionBar
        // getSupportActionBar().hide();


        // Init views
        newchat = view.findViewById(R.id.newchat);
        newchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewChat.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });
        messListView = view.findViewById(R.id.messagesListView);
        noMessLayout = view.findViewById(R.id.messNoMessLayout);
        LinearLayoutManager llmm = new LinearLayoutManager(getActivity());
        llmm.setOrientation(LinearLayoutManager.VERTICAL);
        messListView.setLayoutManager(llmm);
        messListView.setItemAnimator(null);


        getMessages();
        return view;

    }


    private void getMessages() {

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, "https://app_api_json.veamex.com/api/common/GetChatsByUserId?userId=1&typeId=1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_LONG).show();
                /*   hideProgress(getActivity());*/
                if (response.equals("[]")) {
                    noMessLayout.setVisibility(View.VISIBLE);
                    messListView.setVisibility(View.INVISIBLE);
                } else {


                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();

                    MessageModel[] messageModels = gson.fromJson(response, MessageModel[].class);

                    messageAdpater = new MessageAdpater(getActivity(), messageModels);
                    messListView.setAdapter(messageAdpater);
                    messageAdpater.notifyDataSetChanged();


                   /* for (int i = 0; i < messageModels.length; i++) {
                        if (messageModels[i].getParticipants().equals("[]")) {

                        } else

                    }
*/

                }
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
                Toast.makeText(getActivity(), "Server error or No internet connection", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };
        /* showProgress(getActivity(), "Loading....", "Please wait!");*/
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onResume() {
        super.onResume();
        getMessages();
    }
}//@end
