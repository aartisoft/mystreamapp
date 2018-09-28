package com.domain.mystream.Activity;

/* ================================

    - MyStream -

    created by cubycode @2018
    All Rights reserved

===================================*/

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.domain.mystream.Constants.Configs;
import com.domain.mystream.R;


public class ShowVideo extends AppCompatActivity {

    /* Views */
    VideoView mVideoView;


    /* Variables */
    String videoURL;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_video);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide ActionBar
        getSupportActionBar().hide();
        // Get videoURL
        Bundle extras = getIntent().getExtras();
        videoURL = extras.getString("videoURL");
        Log.i("log-", "VIDEO URL: " + videoURL);


        // Init videoView and play video
        mVideoView = findViewById(R.id.videoView);
        Configs.showPD("Please wait...", ShowVideo.this);

        try {
            MediaController mediacontroller = new MediaController(ShowVideo.this);
            mediacontroller.setAnchorView(mVideoView);

            Uri videoUri = Uri.parse(videoURL);
            mVideoView.setMediaController(mediacontroller);
            mVideoView.setVideoURI(videoUri);

        } catch (Exception err) { err.printStackTrace(); }

        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                Configs.hidePD();
                mVideoView.start();
        }});



    }// end onCreate()





}// @end
