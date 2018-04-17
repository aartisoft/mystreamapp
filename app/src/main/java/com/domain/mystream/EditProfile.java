package com.domain.mystream;

/*================================

    - MyStream -

    created by cubycode @2018
    All Rights reserved

===================================*/

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class EditProfile extends AppCompatActivity {

    /* Views */
    ImageView avatarImg, coverImg;
    EditText usernameTxt, fullnameTxt, emailTxt, aboutMeTxt;
    Button updateProfileButt;


    /* Variables */
    boolean isAvatar;
    MarshMallowPermission mmp = new MarshMallowPermission(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Hide ActionBar
        getSupportActionBar().hide();

        // Change StatusBar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));


        // Request Storage permission
        if(!mmp.checkPermissionForReadExternalStorage()) {
            mmp.requestPermissionForReadExternalStorage();
        }


        // Init views
        avatarImg = findViewById(R.id.epAvatarImg);
        coverImg = findViewById(R.id.epCoverImg);
        usernameTxt= findViewById(R.id.epUsernameTxt);
        usernameTxt.setTypeface(Configs.titRegular);
        fullnameTxt = findViewById(R.id.epFullnameTxt);
        fullnameTxt.setTypeface(Configs.titRegular);
        emailTxt = findViewById(R.id.epEmailTxt);
        emailTxt.setTypeface(Configs.titRegular);
        aboutMeTxt = findViewById(R.id.epAboutMeTxt);
        aboutMeTxt.setTypeface(Configs.titRegular);
        updateProfileButt = findViewById(R.id.epUpdateProfileButt);
        updateProfileButt.setTypeface(Configs.titSemibold);


        // Call query
        getUserDetails();


        // MARK: - CANCEL BUTTON ------------------------------------
        Button cancButt = findViewById(R.id.epCancelButt);
        cancButt.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) { finish(); }});


    }// end onCreate()






    // MARK: - GET USER'S DETAILS --------------------------------------------------------
    void getUserDetails() {
        ParseUser currUser = ParseUser.getCurrentUser();

        // Get images
        Configs.getParseImage(avatarImg, currUser, Configs.USER_AVATAR);
        Configs.getParseImage(coverImg, currUser, Configs.USER_COVER_IMAGE);

        // Get details
        usernameTxt.setText(currUser.getString(Configs.USER_USERNAME));
        fullnameTxt.setText(currUser.getString(Configs.USER_FULLNAME));
        if (currUser.getString(Configs.USER_EMAIL) != null){ emailTxt.setText(currUser.getString(Configs.USER_EMAIL)); }
        if (currUser.getString(Configs.USER_ABOUT_ME) != null){ aboutMeTxt.setText(currUser.getString(Configs.USER_ABOUT_ME)); }



        // MARK: - CHANGE AVATAR ------------------------------------
        avatarImg.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              isAvatar = true;
              openAlertForCameraGallery();
        }});



        // MARK: - CHANGE COVER IMAGE ---------------------------------------------
        coverImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAvatar = false;
                openAlertForCameraGallery();
        }});




        // MARK: - UPDATE PROFILE BUTTON --------------------------------------------------
        updateProfileButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser currUser = ParseUser.getCurrentUser();

                if (usernameTxt.getText().toString().matches("") ||
                        emailTxt.getText().toString().matches("") ||
                        fullnameTxt.getText().toString().matches("") ){
                    Configs.simpleAlert("You must type a Username, a Full Name and an email address (needed only for password recovery)", EditProfile.this);

                } else {
                    Configs.showPD("Please wait...", EditProfile.this);
                    dismissKeyboard();

                    // Save data
                    currUser.put(Configs.USER_USERNAME, usernameTxt.getText().toString());
                    currUser.put(Configs.USER_EMAIL, emailTxt.getText().toString());
                    currUser.put(Configs.USER_FULLNAME, fullnameTxt.getText().toString());
                    currUser.put(Configs.USER_ABOUT_ME, aboutMeTxt.getText().toString());

                    // Save Avatar image
                    if (avatarImg.getDrawable() != null){
                        Configs.saveParseImage(avatarImg, currUser, Configs.USER_AVATAR);
                    }

                    // Save Cover image
                    if (coverImg.getDrawable() != null) {
                        Configs.saveParseImage(coverImg, currUser, Configs.USER_COVER_IMAGE);
                    }


                    // Saving block
                    currUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Configs.hidePD();
                                Configs.simpleAlert("Your profile has been updated!", EditProfile.this);

                        // error
                        } else {
                                Configs.hidePD();
                                Configs.simpleAlert(e.getMessage(), EditProfile.this);
                    }}});
                }

         }});
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
                                } else { openCamera(); }
                                break;

                            // OPEN GALLERY
                            case 1:
                                if (!mmp.checkPermissionForReadExternalStorage()) {
                                    mmp.requestPermissionForReadExternalStorage();
                                } else { openGallery(); }
                                break;
                        }}})
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
        Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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

        if (resultCode == Activity.RESULT_OK) {
            Bitmap bm = null;

            // Image from Camera
            if (requestCode == CAMERA) {

                try {
                    File f = file;
                    ExifInterface exif = new ExifInterface(f.getPath());
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                    int angle = 0;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90) { angle = 90; }
                    else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) { angle = 180; }
                    else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) { angle = 270; }
                    Log.i("log-", "ORIENTATION: " + orientation);

                    Matrix mat = new Matrix();
                    mat.postRotate(angle);

                    Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f), null, null);
                    bm = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
                }
                catch (IOException | OutOfMemoryError e) { Log.i("log-", e.getMessage()); }


                // Image from Gallery
            } else if (requestCode == GALLERY) {
                try { bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException e) { e.printStackTrace(); }
            }


            // Set image
            Bitmap scaledBm = Configs.scaleBitmapToMaxSize(600, bm);
            if (isAvatar) {
                avatarImg.setImageBitmap(scaledBm);
            } else { coverImg.setImageBitmap(scaledBm); }
        }
    }
    //---------------------------------------------------------------------------------------------





    // MARK: - DISMISS KEYBOARD
    void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(usernameTxt.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(fullnameTxt.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(emailTxt.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(aboutMeTxt.getWindowToken(), 0);
    }

}// @end
