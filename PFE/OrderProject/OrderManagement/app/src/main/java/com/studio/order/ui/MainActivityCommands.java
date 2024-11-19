package com.studio.order.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.studio.order.LocationUploadService;
import com.studio.order.R;
import com.studio.order.utils.HideButtonOnScrollListener;
import com.studio.order.utils.VolleyManager;

import java.util.Objects;

public class MainActivityCommands extends AppCompatActivity {

    static int userId;
    ProgressDialog myProgress;
    RecyclerView recyclerView;
    TextView noDataTextView;
    ProgressBar progressBar;
    MainActivityCommands activityCommands;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_commands);

        activityCommands = this;


        myProgress = new ProgressDialog(this); // Initialize the ProgressDialog
        myProgress.setTitle("Ajout en cours...");
        myProgress.setMessage("Veuillez patienter...");
        myProgress.setCancelable(false);
        myProgress.setIndeterminate(true);


        SharedPreferences sharedPrefs = getSharedPreferences("session", Context.MODE_PRIVATE);

        userId = sharedPrefs.getInt("userId", 0);

        FloatingActionButton floatingButton = findViewById(R.id.floatingButton);
        recyclerView = findViewById(R.id.RecyclerView);
        noDataTextView = findViewById(R.id.noDataTextView);
        progressBar = findViewById(R.id.ProgressBar);

        recyclerView.addOnScrollListener(new HideButtonOnScrollListener() {
            @Override
            public void onHide() {
                // Hide your button here
                floatingButton.setVisibility(View.GONE);
            }

            @Override
            public void onShow() {
                // Show your button here
                floatingButton.setVisibility(View.VISIBLE);
            }
        });

        floatingButton.setOnClickListener(v -> {
            @SuppressLint("InflateParams")
            View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_add_command, null);

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivityCommands.this);
            bottomSheetDialog.setContentView(bottomSheetView);

            TextInputLayout nameEditText = bottomSheetView.findViewById(R.id.nameEditText);
            TextInputLayout contentEditText = bottomSheetView.findViewById(R.id.contentEditText);
            Button btnAdd = bottomSheetView.findViewById(R.id.BtnAdd);
            btnAdd.setOnClickListener(v1 -> {

                String nameCommand = String.valueOf(Objects.requireNonNull(nameEditText.getEditText()).getText());
                String contentCommand = String.valueOf(Objects.requireNonNull(contentEditText.getEditText()).getText());

                if (nameCommand.length() == 0) {
                    nameEditText.setError("Insérer le nom du modèle");
                    return;
                }
                if (contentCommand.length() == 0) {
                    contentEditText.setError("Insérer le nom du modèle");
                    return;
                }


                // Perform the login action here
                VolleyManager.addCommand(this, String.valueOf(userId), nameCommand, contentCommand, myProgress, recyclerView, noDataTextView, progressBar);
                bottomSheetDialog.dismiss();
            });


            bottomSheetDialog.show();
        });

        findViewById(R.id.BtnLogout).setOnClickListener(v -> logOut());


    }


    private void logOut() {

        // Professor is already logged in, show AlertDialog for logout confirmation
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation de déconnexion");
        builder.setMessage("Êtes-vous sûr de vouloir vous déconnecter?");
        builder.setPositiveButton("Se déconnecter", (dialog, which) -> {
            // Clear the shared preferences and navigate to the login activity
            SharedPreferences sharedPrefs = getSharedPreferences("session", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(this, MainActivityLogin.class);
            startActivity(intent);
            finish();
        });
        builder.setNegativeButton("Annuler", (dialog, which) -> {
            // User clicked cancel, do nothing
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        VolleyManager.getCommands(this, String.valueOf(userId), recyclerView, noDataTextView, progressBar);


        // Check GPS and location permission
        checkGpsAndPermission();
    }


    private void checkGpsAndPermission() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean hasLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        if (!isGpsEnabled) {
            showGpsDisabledAlertDialog();
        } else if (!hasLocationPermission) {
            requestLocationPermission();
        } else {
            startLocationUploadService();
        }
    }

    private void showGpsDisabledAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("GPS désactivé");
        builder.setMessage("Veuillez activer le GPS pour continuer.");
        builder.setPositiveButton("Activer le GPS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void startLocationUploadService() {
        Intent serviceIntent = new Intent(this, LocationUploadService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }


    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Display a rationale for requesting the permission
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Autorisation de localisation")
                    .setMessage("Cette application nécessite une autorisation de localisation pour collecter et télécharger des données de localisation.")
                    .setPositiveButton("OK", (dialog, which) -> {
                        // Request the permission
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
                    })
                    .setNegativeButton("Annuler", (dialog, which) -> {
                        // Permission denied, handle accordingly (e.g., show a message to the user)
                        finish();
                    })
                    .show();
        } else {
            // Request the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent serviceIntent = new Intent(this, LocationUploadService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent);
                } else {
                    startService(serviceIntent);
                }
            } else {
                // Permission denied, send the user to the app's settings screen
                Intent settingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                settingsIntent.setData(uri);
                startActivity(settingsIntent);
                finish();
            }
        }
    }


}