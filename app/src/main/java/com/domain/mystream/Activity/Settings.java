package com.domain.mystream.Activity;

/* ================================

    - MyStream -

    created by cubycode @2018
    All Rights reserved

===================================*/

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.domain.mystream.Constants.Configs;
import com.domain.mystream.R;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

public class Settings extends AppCompatActivity {

    /* Views */
    ListView settingsListView;



    /* Variables */
    String[] settingsArray = {
            "Edit Profile",          // 0
            // 1
            "Terms of Use",          // 2
            "Version",               // 3
            "Like on Facebook",      // 4
            "Follow on Twitter",     // 5
            "Rate on the Play Store",// 6
            "Logout",                // 7
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Hide ActionBar
//        getSupportActionBar().hide();

        // Change StatusBar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));


        // Init views
        settingsListView = findViewById(R.id.settListView);

        configureSettingsListView();



        // MARK: - BACK BUTTON ------------------------------------
        Button backButt = findViewById(R.id.settBackButt);
        backButt.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) { finish(); }});


    }// end onCreate()






    // MARK: - CONFIGURE SETTINGS LISTVIEW ------------------------------------------------------
    void configureSettingsListView() {
        final List<String>settArr = Arrays.asList(settingsArray);

         // CUSTOM LIST ADAPTER
          class ListAdapter extends BaseAdapter {
              private Context context;
              public ListAdapter(Context context) {
                  super();
                  this.context = context;
              }

              // CONFIGURE CELL
              @SuppressLint("SetTextI18n")
              @Override
              public View getView(int position, View cell, ViewGroup parent) {
                  if (cell == null) {
                      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                      assert inflater != null;
                      cell = inflater.inflate(R.layout.cell_settings, null);
                  }

                  // Init views
                  TextView cellTxt = cell.findViewById(R.id.csettCellTxt);
                  cellTxt.setTypeface(Configs.titSemibold);

                  // Set cell text
                  cellTxt.setText(settArr.get(position));

                  // Get app version
                  if (position == 3){
                      try {
                          PackageInfo pInfo = Settings.this.getPackageManager().getPackageInfo(getPackageName(), 0);
                          String version = pInfo.versionName;
                          cellTxt.setText("Version " + version);
                      } catch (PackageManager.NameNotFoundException e) { e.printStackTrace(); }
                  }


                  // Red the Logout text
                  if (position == 7) { cellTxt.setTextColor(Color.RED); }


              return cell;
              }

              @Override public int getCount() { return settArr.size(); }
              @Override public Object getItem(int position) { return settArr.get(position); }
              @Override public long getItemId(int position) { return position; }
          }


          // Init ListView and set its adapter
          settingsListView.setAdapter(new ListAdapter(Settings.this));
          settingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                  switch (position){


                      // EDIT PROFILE ------------------------------------------
                      case 0:
                          startActivity(new Intent(Settings.this, EditProfile.class));
                          break;



                   /*   // ACTIVITY ------------------------------------------------
                      case 1:
                          startActivity(new Intent(Settings.this, ActivityScreen.class));
                          break;

*/

                      // TERMS OF USE ------------------------------------------------
                      case 1:
                          startActivity(new Intent(Settings.this, TermsOfUse.class));
                          break;


                      // LIKE ON FACEBOOK ----------------------------------------
                      case 2:
                          startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Configs.FACEBOOK_URL)));
                          break;



                      // FOLLOW ON TWITTER ----------------------------------------
                      case 3:
                          startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Configs.TWITTER_URL)));
                          break;



                      // RATE ON THE APP STORE ----------------------------------------
                      case 4:
                          try {
                              PackageInfo pInfo = Settings.this.getPackageManager().getPackageInfo(getPackageName(), 0);
                              String pName = pInfo.packageName;
                              startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pName)));
                              Log.i("log-", "PACKAGE NAME: " + pName);
                          } catch (PackageManager.NameNotFoundException e) { e.printStackTrace(); }
                          break;



                      // LOGOUT ---------------------------------------------------------
                      case 5:
                          AlertDialog.Builder alert = new AlertDialog.Builder(Settings.this);
                          alert.setMessage("Are you sure you want to logout?")
                              .setTitle(R.string.app_name)
                              .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialogInterface, int i) {
                                      Configs.showPD("Logging out...", Settings.this);

                                      ParseUser.logOutInBackground(new LogOutCallback() {
                                          @Override
                                          public void done(ParseException e) {
                                              Configs.hidePD();
                                              // Open Intro activity
                                              startActivity(new Intent(Settings.this, Intro.class));
                                      }});
                                  }})
                              .setNegativeButton("Cancel", null)
                              .setIcon(R.drawable.logo);
                          alert.create().show();
                      break;


                      default:break; }

          }});

    }


}// @end
