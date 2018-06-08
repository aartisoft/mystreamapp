package com.domain.mystream.Activity;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

public class MyStream extends Application {

 @Override
protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(base);
   }
}