package com.domain.mystream.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.domain.mystream.R;

import static com.domain.mystream.Constants.Configs.editor;
import static com.domain.mystream.Constants.Configs.myPref;
import static com.domain.mystream.Constants.Configs.sharedPreferences;

public class SplashScreen extends AppCompatActivity {

    public static final String loginOrRegId="loginOrRegId";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        sharedPreferences = getSharedPreferences(myPref, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        final String user_id = sharedPreferences.getString("userid",null);

        int splashInterval = 2000;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (user_id!=null){
                    Intent i = new Intent(SplashScreen.this, Home.class);
                    startActivity(i);
                    this.finish();
                }
       else
           {
                    Intent i = new Intent(SplashScreen.this, Login.class);
                    startActivity(i);
                    this.finish();
                }
            }

            private void finish() {
            }}, splashInterval);

    } // end onCreate()



} //@end
