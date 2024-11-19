package com.studio.order;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

public class MyApplication extends Application {
    private boolean isAppRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();

        // Register the activity lifecycle callbacks
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (!isAppRunning) {
                    isAppRunning = true;
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (isAppRunning && isAppClosed()) {
                    stopLocationUploadService();
                    isAppRunning = false;
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    private void stopLocationUploadService() {
        Intent serviceIntent = new Intent(this, LocationUploadService.class);
        stopService(serviceIntent);
    }

    private boolean isAppClosed() {
        // Implement your logic here to determine if the app is closed
        // You can use different approaches like tracking the number of activities or detecting app termination events
        return true;  // Replace this with your implementation
    }
}
