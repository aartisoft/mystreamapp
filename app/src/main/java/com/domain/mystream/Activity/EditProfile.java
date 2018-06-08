package com.domain.mystream.Activity;

/*================================

    - MyStream -

    created by cubycode @2018
    All Rights reserved

===================================*/

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.domain.mystream.Constants.Configs;
import com.domain.mystream.Constants.MarshMallowPermission;
import com.domain.mystream.Constants.VolleyMultipartRequest;
import com.domain.mystream.R;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


import static com.domain.mystream.Activity.StreamDetails.hideProgress;
import static com.domain.mystream.Activity.StreamDetails.showProgress;
import static com.domain.mystream.Constants.Configs.editor;
import static com.domain.mystream.Constants.Configs.error_toast;
import static com.domain.mystream.Constants.Configs.myPref;
import static com.domain.mystream.Constants.Configs.sharedPreferences;
import static com.domain.mystream.Constants.Configs.user_id;
import static com.domain.mystream.Constants.MyStreamApis.BASE_URL;
import static com.domain.mystream.Constants.MyStreamApis.UPDATE_PROFILE;
import static com.domain.mystream.Constants.MyStreamApis.UPLOAD_PROFILE_PIC;

public class EditProfile extends AppCompatActivity {

    /* Views */
    ImageView avatarImg, coverImg;
    EditText usernameTxtEdit, fullnameTxtEdit, emailTxt, aboutMeTxt;
    Button updateProfileButt;
    String uasername, fullName,username,fullname,email,about;
    JSONObject jsonObject;
    Bitmap bitmap;

    boolean isAvatar;
    MarshMallowPermission mmp = new MarshMallowPermission(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));


        // Request Storage permission
        if (!mmp.checkPermissionForReadExternalStorage()) {
            mmp.requestPermissionForReadExternalStorage();
        }

         sharedPreferences = getSharedPreferences(myPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        uasername = sharedPreferences.getString("username", null);
        fullName = sharedPreferences.getString("fullname", null);
        email =sharedPreferences.getString("EmailAddress",null);
        about =sharedPreferences.getString("About",null);
        // Init views


        avatarImg = findViewById(R.id.epAvatarImg);
        coverImg = findViewById(R.id.epCoverImg);

        usernameTxtEdit = findViewById(R.id.epUsernameTxt);
        usernameTxtEdit.setTypeface(Configs.titRegular);
        usernameTxtEdit.setText(uasername);

        fullnameTxtEdit = findViewById(R.id.epFullnameTxt);
        fullnameTxtEdit.setTypeface(Configs.titRegular);
        fullnameTxtEdit.setText(fullName);

        emailTxt = findViewById(R.id.epEmailTxt);
        emailTxt.setTypeface(Configs.titRegular);
        emailTxt.setText(email);
        aboutMeTxt = findViewById(R.id.epAboutMeTxt);
        aboutMeTxt.setTypeface(Configs.titRegular);
        aboutMeTxt.setText(about);

        updateProfileButt = findViewById(R.id.epUpdateProfileButt);
        updateProfileButt.setTypeface(Configs.titSemibold);

        String pp = sharedPreferences.getString("ProfilePic",null);
        Glide.with(EditProfile.this)
                .load(pp)
                .into(avatarImg);

        // Call query
        getUserDetails();


        // MARK: - CANCEL BUTTON ------------------------------------
        Button cancButt = findViewById(R.id.epCancelButt);
        cancButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }// end onCreate()


    // MARK: - GET USER'S DETAILS --------------------------------------------------------
    void getUserDetails() {
        ParseUser currUser = ParseUser.getCurrentUser();

        // Get images
        Configs.getParseImage(avatarImg, currUser, Configs.USER_AVATAR);
        Configs.getParseImage(coverImg, currUser, Configs.USER_COVER_IMAGE);


        // MARK: - CHANGE AVATAR ------------------------------------
        avatarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAvatar = true;
                openAlertForCameraGallery();
            }
        });


        // MARK: - CHANGE COVER IMAGE ---------------------------------------------
        coverImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAvatar = false;
                openAlertForCameraGallery();
            }
        });


        // MARK: - UPDATE PROFILE BUTTON --------------------------------------------------
        updateProfileButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = usernameTxtEdit.getText().toString();
                String fullname = fullnameTxtEdit.getText().toString();
                String about = aboutMeTxt.getText().toString();
                String email = emailTxt.getText().toString();


                if (usernameTxtEdit.getText().toString().matches("") ||
                        fullnameTxtEdit.getText().toString().matches("")) {
                    Configs.simpleAlert("You must type a Username, a Full Name)", EditProfile.this);

                } else {


                    jsonObject = new JSONObject();
                    try {

                        jsonObject.put("UserName", username);
                        jsonObject.put("Email", email);
                        jsonObject.put("About", about);
                        jsonObject.put("FullName",fullname);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    updateProfile(jsonObject);


                }

            }
        });
    }


    // MARK: - OPEN ALERT FOR CAMERA/GALLERY ----------------------------------------
    void openAlertForCameraGallery() {
        dismissKeyboard();
        AlertDialog.Builder alert = new AlertDialog.Builder(EditProfile.this);
        alert.setTitle("SELECT SOURCE")
                .setIcon(R.drawable.logo)
                .setItems(new CharSequence[]{
                        "Take a picture",
                        "Pick from Gallery"
                }, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {

                            // OPEN CAMERA
                            case 0:
                                if (!mmp.checkPermissionForCamera()) {
                                    mmp.requestPermissionForCamera();
                                } else {
                                    openCamera();
                                }
                                break;

                            // OPEN GALLERY
                            case 1:
                                if (!mmp.checkPermissionForReadExternalStorage()) {
                                    mmp.requestPermissionForReadExternalStorage();
                                } else {
                                    openGallery();
                                }
                                break;
                        }
                    }
                })
                .setNegativeButton("Cancel", null);
        alert.create().show();
    }


    // IMAGE HANDLING METHODS ------------------------------------------------------------------------
    int CAMERA = 0;
    int GALLERY = 1;
    Uri imageURI;
    File file;


    // OPEN CAMERA
    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStorageDirectory(), "image.jpg");
        imageURI = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
        startActivityForResult(intent, CAMERA);
    }


    // OPEN GALLERY
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), GALLERY);
    }


    // IMAGE PICKED DELEGATE -----------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(EditProfile.this.getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                avatarImg.setImageBitmap(bitmap);
                uploadProfilePic();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //---------------------------------------------------------------------------------------------




    // MARK: - DISMISS KEYBOARD
    void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(usernameTxtEdit.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(fullnameTxtEdit.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(emailTxt.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(aboutMeTxt.getWindowToken(), 0);
    }

    private void updateProfile(final JSONObject jsonObject) {
        final String requestBody = jsonObject.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_LONG).show();
                hideProgress(EditProfile.this);

                Toast.makeText(EditProfile.this, "profile updated", Toast.LENGTH_SHORT).show();


                try {
                    usernameTxtEdit.setText(jsonObject.getString("UserName"));
                    editor.putString("fullname",jsonObject.getString("FullName"));
                    editor.putString("About",jsonObject.getString("About"));
                    editor.commit();
                    fullnameTxtEdit.setText(jsonObject.getString("FullName"));
                    emailTxt.setText(jsonObject.getString("Email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                aboutMeTxt.setText(about);
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
                Toast.makeText(EditProfile.this, error_toast, Toast.LENGTH_LONG).show();
            }
        })  {
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
        showProgress(EditProfile.this, "Loading....", "Please wait!");
        RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);
        requestQueue.add(stringRequest);

    }

    private void uploadProfilePic() {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, UPLOAD_PROFILE_PIC+"userId=" + user_id,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        //  iv_imageview.setImageBitmap(bitmap);

                        Log.e("VolleyOnResponse200", response.toString());

                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            String path = obj.getString("Path");
                            String msg = obj.getString("Message");
                            //  String post = streamTxt.getText().toString();
                            String url = "https://app_api_json.veamex.com" + path;
                            Glide.with(EditProfile.this)
                                    .load(url)
                                    .into(avatarImg);
                            editor.putString("ProfilePic",url);
                            editor.apply();
                            finish();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyonErrorResponse200", "Error: " + error.getMessage());
                        // Toasty.error(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(EditProfile.this).add(volleyMultipartRequest);

    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}// @end
