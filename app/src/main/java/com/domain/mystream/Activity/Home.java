package com.domain.mystream.Activity;

/* ================================
    - MyStream -
    created by cubycode @2018
    All Rights reserved
===================================*/
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.domain.mystream.Fragment.Account;
import com.domain.mystream.Fragment.Following;
import com.domain.mystream.Fragment.MainStreamFragment;
import com.domain.mystream.Fragment.Messages;
import com.domain.mystream.Fragment.StreamFragment;
import com.domain.mystream.R;
import java.lang.reflect.Field;
import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
CircleImageView drawer_open;
ImageView messsagess,home_icon;
ImageView actionbar;
    // ON CREATE() --------------------------------------------------------------
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer_open=findViewById(R.id.drawer_open);
        messsagess=findViewById(R.id.messsagess);
        home_icon=findViewById(R.id.home_icon);
        drawer_open.setOnClickListener(this);
        actionbar=findViewById(R.id.actionbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide ActionBar
//        getSupportActionBar().hide();
        // Change StatusBar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
   FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, MainStreamFragment.newInstance());
        transaction.commit();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        selectedFragment = MainStreamFragment.newInstance();
                        break;

                    case R.id.navigation_following:
                        selectedFragment = Following.newInstance();
                        break;
                    case R.id.navigation_account:
                        selectedFragment = Account.newInstance();
                        break;
                    case R.id.navigation_message:
                        selectedFragment = Messages.newInstance();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
                return true;
            }

        });
        messsagess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager=getSupportFragmentManager();
                FragmentTransaction transaction1=fragmentManager.beginTransaction();
                transaction1.replace(R.id.home_frame,new Messages()).addToBackStack(null).commit();
            }
        });
        home_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(Home.this,Home.class);
               startActivity(intent);
            }
        });
        }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.drawerhome) {
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction transaction=fragmentManager.beginTransaction();
            transaction.replace(R.id.home_frame,new StreamFragment()).addToBackStack(null).commit();
        } else if (id == R.id.drawerleer) {

        } else if (id == R.id.drawerbibi) {

        } else if (id == R.id.drawerchat) {
        Intent intent=new Intent(Home.this,NewChat.class);
        startActivity(intent);
        } else if (id == R.id.drawerfeed) {

        } else if (id == R.id.drawerdash) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // add your action here that you want
            return true;
        }




        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.drawer_open){
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.openDrawer(GravityCompat.START);
        }
    }

    static class BottomNavigationViewHelper {
        @SuppressLint("RestrictedApi")
        private static void disableShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("BNVHelper", "Unable to get shift mode field", e);
            } catch (IllegalAccessException e) {
                Log.e("BNVHelper", "Unable to change value of shift mode", e);
            }
        }
    }
}//@end
