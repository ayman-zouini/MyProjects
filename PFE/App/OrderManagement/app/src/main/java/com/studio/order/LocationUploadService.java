package com.studio.order;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LocationUploadService extends Service {

    private static final String TAG = LocationUploadService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "LocationUploadChannel";
    private static final long INTERVAL = 30 * 1000; // 30 seconds
    private int userId;
    private Handler handler;
    private Runnable uploadRunnable;
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPrefs = getSharedPreferences("session", Context.MODE_PRIVATE);

        userId = sharedPrefs.getInt("userId", 0);

        // Create the notification channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Location Upload Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Initialize the handler and runnable
        handler = new Handler();
        uploadRunnable = new Runnable() {
            @Override
            public void run() {
                uploadLocationData();
                handler.postDelayed(this, INTERVAL);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");

        // Build and display the foreground notification
        Notification notification = buildNotification();
        startForeground(NOTIFICATION_ID, notification);
        handler.postDelayed(uploadRunnable, 0);


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed");

        handler.removeCallbacks(uploadRunnable);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void uploadLocationData() {
        // Create a location manager instance
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Check location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            // Create a location listener
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // Handle the updated location here
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    addLocalization(latitude, longitude);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    // Handle location provider status changes if needed
                }

                @Override
                public void onProviderEnabled(String provider) {
                    // Handle location provider enabled if needed
                }

                @Override
                public void onProviderDisabled(String provider) {
                    // Handle location provider disabled if needed
                }
            };

            // Request a single location update
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
        }

        handler.removeCallbacks(uploadRunnable);

    }

    private void addLocalization(double latitude, double longitude) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://ordermanagment.000webhostapp.com/order/AddLocalisation.php",
                response -> {
                    // Handle the response here
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("user_id", String.valueOf(userId));
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }



    private Notification buildNotification() {
        // Create the intent for stopping the service
        Intent stopIntent = new Intent(this, LocationUploadService.class);
        stopIntent.setAction("stop_service");
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Location Upload Service")
                .setContentText("Service is running")
                .setSmallIcon(R.drawable.ic_logo)
                .addAction(R.drawable.ic_cancel, "Stop", stopPendingIntent);

        // Create the notification channel (if targeting API level 26 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Location Service", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        return builder.build();
    }

}