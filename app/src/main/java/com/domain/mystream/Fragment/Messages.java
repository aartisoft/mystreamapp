package com.domain.mystream.Fragment;

/* ================================

    - MyStream -

    created by cubycode @2018
    All Rights reserved

===================================*/

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.domain.mystream.Activity.NewChat;
import com.domain.mystream.Adpater.MessageAdpater;
import com.domain.mystream.Constants.Configs;
import com.domain.mystream.Model.MessageModel;
import com.domain.mystream.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parse.ParseObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.domain.mystream.Constants.Configs.error_toast;
import static com.domain.mystream.Constants.Configs.myPref;
import static com.domain.mystream.Constants.Configs.sharedPreferences;
import static com.domain.mystream.Constants.MyStreamApis.BASE_URL;
import static com.domain.mystream.Constants.MyStreamApis.GET_ALL_MESSAGES;

public class Messages extends Fragment {


    /* Views */
    RecyclerView messListView;
    RelativeLayout noMessLayout;


    /* Variables */
    List<ParseObject> messagesArray;
    ImageView newchat;
    MessageAdpater messageAdpater;
    String userid, typeId = "1";


    public static Messages newInstance() {
        Messages fragment = new Messages();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.messages, container, false);

        sharedPreferences = getActivity().getSharedPreferences(myPref, Context.MODE_PRIVATE);

        Configs.editor = sharedPreferences.edit();

        userid = sharedPreferences.getString("userid", "0");

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


        getMessages(userid, typeId);
        return view;

    }


    private void getMessages(String userid, String typeId) {

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, GET_ALL_MESSAGES+"userId=" + userid + "&typeId=" + typeId, new Response.Listener<String>() {
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

                 /*   MessageModel[] messageModel1 = new MessageModel[messageModels.length];


                    for (int i = 0,j=0; i < messageModels.length; i++) {
                        try {
                            if (messageModels[i].getParticipants()!=null)
                                if (messageModels[i].getParticipants().equals("[]")) {



                                } else{
                                    messageModel1[j]=messageModels[i];
                                    j++;

                                }
                            messageModels=messageModel1;

                        }catch (Exception e){

                        }

                    }*/


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
                Toast.makeText(getActivity(), error_toast, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onResume() {
        super.onResume();
        getMessages(userid, typeId);
    }
}//@end
