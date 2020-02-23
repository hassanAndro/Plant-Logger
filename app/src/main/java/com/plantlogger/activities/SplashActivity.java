package com.plantlogger.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import com.plantlogger.utils.PreferencesKeeper;
import com.plantlogger.utils.Utils;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SplashActivity extends AppCompatActivity {

    PreferencesKeeper preferencesKeeper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesKeeper = new PreferencesKeeper(this);
        Log.e("CALLED","preferencesKeeper.getLoginStatus() :"+preferencesKeeper.getLoginStatus());
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (preferencesKeeper.getLoginStatus()) {
                    navigateActivity(HomeAreasActivity.class);
                } else {
                    navigateActivity(LoginActivity.class);
                }
            }
        }, 1000);
    }

    public void navigateActivity(Class<?> cls) {
        Intent intent = new Intent(SplashActivity.this, cls);
        startActivity(intent);
        finish();
    }
}
