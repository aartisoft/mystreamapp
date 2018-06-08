package com.domain.mystream.Fragment;

/* ================================

    - MyStream -

    created by cubycode @2018
    All Rights reserved

===================================*/

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.domain.mystream.Adpater.FollowingAdapter;
import com.domain.mystream.Constants.Configs;
import com.domain.mystream.Constants.MarshMallowPermission;
import com.domain.mystream.Model.NewChatModel;
import com.domain.mystream.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parse.ParseObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.domain.mystream.Constants.Configs.editor;
import static com.domain.mystream.Constants.Configs.error_toast;
import static com.domain.mystream.Constants.Configs.myPref;
import static com.domain.mystream.Constants.Configs.sharedPreferences;
import static com.domain.mystream.Constants.Configs.user_id;
import static com.domain.mystream.Constants.MyStreamApis.BASE_URL;
import static com.domain.mystream.Constants.MyStreamApis.CONNECTED_USERS;


public class Following extends Fragment {


    /* Views */
    RecyclerView streamsListView;
    TextView noStreamsTxt;
    FollowingAdapter followingAdapter;
    String userid;


    /* Variables */
    MarshMallowPermission mmp = new MarshMallowPermission(getActivity());
    List<ParseObject> streamsArray;
    List<ParseObject> followArray;


    public static Following newInstance() {
        Following fragment = new Following();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.following, container, false);

        // Hide ActionBar
        //  getActivity().getSupportActionBar().hide();

        // Change StatusBar color
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));


        // Init views
        streamsListView = view.findViewById(R.id.fingStreamsListView);
        LinearLayoutManager llmm = new LinearLayoutManager(getActivity());
        llmm.setOrientation(LinearLayoutManager.VERTICAL);
        streamsListView.setLayoutManager(llmm);
        noStreamsTxt = view.findViewById(R.id.fingNoStreamsTxt);
        noStreamsTxt.setTypeface(Configs.titRegular);

        sharedPreferences = getActivity().getSharedPreferences(myPref, Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();

        userid = sharedPreferences.getString("userid", "0");
        getConnectedUsers(userid);


        // MARK: - REFRESH BUTTON ------------------------------------
        Button refreshButt = view.findViewById(R.id.fingRefreshButt);
        refreshButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        // INTERSTITIAL AD IMPLEMENTATION ------------------------------------
/*        final InterstitialAd interstitialAd = new InterstitialAd(getActivity());
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
        return view;
    }// end onCreate()


    private void getConnectedUsers(final String userId) {

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, CONNECTED_USERS + "userId=" + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_LONG).show();
                /*   hideProgress(getActivity());*/
                if (response != null) {

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    NewChatModel[] newChatModels = gson.fromJson(response, NewChatModel[].class);
                    List<NewChatModel> newChatModels1 = new ArrayList<>();
                    for (int i = 0, j = 0; i < newChatModels.length; i++) {
                        int userId = newChatModels[i].getUserId();
                        if (Integer.valueOf(user_id) != userId) {
                            newChatModels1.add(newChatModels[i]);
                        }
                    }

                    followingAdapter = new FollowingAdapter(getActivity(), newChatModels1);
                    streamsListView.setAdapter(followingAdapter);
                    followingAdapter.notifyDataSetChanged();

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
        /* showProgress(getActivity(), "Loading....", "Please wait!");*/
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }


}


// @end
