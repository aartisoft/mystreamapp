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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.domain.mystream.Constants.Configs;
import com.domain.mystream.Model.Company;
import com.domain.mystream.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.domain.mystream.Constants.Configs.editor;
import static com.domain.mystream.Constants.Configs.error_login;
import static com.domain.mystream.Constants.Configs.error_toast;
import static com.domain.mystream.Constants.Configs.myPref;
import static com.domain.mystream.Constants.Configs.sharedPreferences;
import static com.domain.mystream.Constants.MyStreamApis.BASE_URL;
import static com.domain.mystream.Constants.MyStreamApis.IMAGE_URL;
import static com.domain.mystream.Constants.MyStreamApis.LOGIN_URL;

public class Login extends AppCompatActivity {

    /* Views */
    EditText usernameTxt;
    EditText passwordTxt;
    String userName, pass, c_id = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPreferences = getSharedPreferences(myPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Init views
        usernameTxt = findViewById(R.id.usernameTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        usernameTxt.setTypeface(Configs.titRegular);
        passwordTxt.setTypeface(Configs.titRegular);

        // Get Title
        TextView titleTxt = findViewById(R.id.loginTitleTxt);
        titleTxt.setTypeface(Configs.titSemibold);
        titleTxt.setText("Log in to " + getString(R.string.app_name));


        // MARK: - LOGIN BUTTON ------------------------------------------------------------------
        Button loginButt = findViewById(R.id.loginButt);
        loginButt.setTypeface(Configs.titSemibold);
        loginButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Configs.showPD("Please wait...", Login.this);
                userName = usernameTxt.getText().toString();
                pass = passwordTxt.getText().toString();
                if (userName.equals("") && pass.equals("")) {
                    Toast.makeText(Login.this, "Please Enter details", Toast.LENGTH_SHORT).show();
                } else {
                    login();
                }
            }
        });


        // MARK: - SIGN UP BUTTON ------------------------------------------------------------------
        Button signupButt = findViewById(R.id.signUpButt);
        signupButt.setTypeface(Configs.titSemibold);
        signupButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(Login.this, SignUp.class));
            }
        });


        // MARK: - FORGOT PASSWORD BUTTON ------------------------------------------------------------------
        Button fpButt = findViewById(R.id.forgotPassButt);
        fpButt.setTypeface(Configs.titSemibold);
        fpButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Login.this);
                alert.setTitle(R.string.app_name);
                alert.setMessage("Type the valid email address you've used to register on this app");

                // Add an EditTxt
                final EditText editTxt = new EditText(Login.this);
                editTxt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                alert.setView(editTxt)
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                // Reset password
                                ParseUser.requestPasswordResetInBackground(editTxt.getText().toString(), new RequestPasswordResetCallback() {
                                    public void done(ParseException error) {
                                        if (error == null) {
                                            Configs.simpleAlert("We've sent you an email to reset your password!", Login.this);
                                        } else {
                                            Configs.simpleAlert(error.getMessage(), Login.this);
                                        }
                                    }
                                });

                            }
                        });
                alert.show();

            }
        });


        // MARK: - DISMISS BUTTON ----------------------------------------------
        Button dismButt = findViewById(R.id.lDismissButt);
        dismButt.setTypeface(Configs.titSemibold);
        dismButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }// end onCreate()

    private void login() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, LOGIN_URL+"userName"+"="+userName+"&"+"passWord"+"="+pass+"&"+"companyId"+"="+c_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_LONG).show();
                //  hideProgress(getActivity());
                Log.d("success","successs");

                try {
                    Configs.hidePD();
                    JSONObject rs = new JSONObject(response);
                    String userId = rs.optString("UserId");
                    String userName = rs.optString("UserName");
                    String pass = rs.optString("PassWord");
                    String fullName = rs.optString("FullName");
                    String about = rs.optString("About");
                    String profile = rs.optString("ProfilePic");
                    String url =IMAGE_URL;
                    String profile_img = url+profile;


                    JSONObject object = new JSONObject(rs.getString("Company"));
                    String email = object.optString("EmailAddress");



                    if (!userName.equals("null")) {
                        editor.putString("userid", userId);
                        editor.putString("username", userName);
                        editor.putString("password", pass);
                        editor.putString("fullname",fullName);
                        editor.putString("EmailAddress",email);
                        editor.putString("About",about);
                        editor.putString("ProfilePic",profile_img);
                        editor.commit();
                        // Go to Home screen
                        //startActivity(new Intent(Login.this, Home.class));
                        Intent intent = new Intent(Login.this, Home.class);
                        startActivity(intent);


                        // error
                    } else {
                        Configs.simpleAlert(error_login, Login.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Configs.hidePD();
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject jsonObject = new JSONObject(responseBody);
                    Log.d("success","failure");
                    Log.d(jsonObject+"jsonres","jsonresponse");
                    Log.d(error+"","");
                } catch (Exception e) {
                    //Handle a malformed json response
                    Toast.makeText(Login.this, error_toast+e, Toast.LENGTH_LONG).show();
                }

            }
        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("userName", userName);
//                params.put("passWord", pass);
//                params.put("companyId",c_id);
//                return params;
//            }


//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//
//
//                return params;
//            }
        };

        // showProgress(getActivity(), "Loading....", "Please wait!");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}// @end