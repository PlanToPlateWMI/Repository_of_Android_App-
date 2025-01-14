/*
 * Copyright 2023 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.plantoplate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import pl.plantoplate.utils.ApplicationState;
import pl.plantoplate.ui.login.LoginActivity;
import pl.plantoplate.ui.main.ActivityMain;
import pl.plantoplate.ui.registration.EmailConfirmActivity;
import pl.plantoplate.ui.registration.GroupSelectActivity;
import pl.plantoplate.ui.registration.RegisterActivity;
import timber.log.Timber;

/**
 * This activity is responsible for displaying the splash screen and initializing the application.
 * It is also responsible for redirecting the user to the appropriate activity.
 */
@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private static final int SPLASH_TIME_OUT = 1000;

    /**
     * This method is responsible for displaying the splash screen and initializing the application.
     * It is also responsible for redirecting the user to the appropriate activity.
     *
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefs = getSharedPreferences("prefs", 0);
        setAppTheme(prefs.getString("theme", "light"));
        getFcmToken();

        View decorView = getWindow().getDecorView();
        decorView.postDelayed(() -> {
            initApplication();
            finish();
        }, SPLASH_TIME_OUT);
    }

    /**
     * This method is responsible for setting the application theme.
     *
     * @param theme application theme
     */
    private void setAppTheme(String theme) {
        Timber.e("Theme: %s", theme);
        if(theme.equals("dark")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    /**
     * This method is responsible for initializing the application.
     * It is also responsible for redirecting the user to the appropriate activity.
     */
    private void initApplication() {
        String applicationState = prefs.getString("applicationState", ApplicationState.LOGIN.toString());
        Class<? extends AppCompatActivity> activityClass = getActivityClass(applicationState);
        startNewActivity(activityClass);
    }

    private Class<? extends AppCompatActivity> getActivityClass(String applicationState) {
        switch (ApplicationState.valueOf(applicationState)) {
            case REGISTER:
                return RegisterActivity.class;
            case CONFIRM_MAIL:
                return EmailConfirmActivity.class;
            case GROUP_CHOOSE:
                return GroupSelectActivity.class;
            case MAIN_ACTIVITY:
                return ActivityMain.class;
            default:
                return LoginActivity.class;
        }
    }


    /**
     * This method is responsible for getting the FCM token.
     */
    public void getFcmToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Timber.e("Fetching FCM registration token failed");
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();
                    prefs.edit().putString("fcmToken", token).apply();

                    // Log and toast
                    Timber.e("FCM token: %s", token);
                });
    }

    /**
     * This method is responsible for starting a new activity.
     *
     * @param activityClass activity class
     */
    private void startNewActivity(Class<? extends AppCompatActivity> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}