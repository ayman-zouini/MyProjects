package com.studio.order.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.studio.order.R;
import com.studio.order.adapters.AdapterCommands;
import com.studio.order.utils.HideButtonOnScrollListener;
import com.studio.order.utils.VolleyManager;

import java.util.Objects;

public class MainActivityCommandsDetails extends AppCompatActivity {

    TextView noDataTextView, tvBarName;
    String commandId, commandName, commandNameNew;
    ProgressDialog myProgress;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    AdapterCommands adapterCommands;
    MainActivityCommandsDetails activityCommandsDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_commands_details);

        activityCommandsDetails = this;


        myProgress = new ProgressDialog(this); // Initialize the ProgressDialog
        myProgress.setTitle("Ajout en cours...");
        myProgress.setMessage("Veuillez patienter...");
        myProgress.setCancelable(false);
        myProgress.setIndeterminate(true);

        commandId = getIntent().getExtras().getString("CMD_ID");
        commandName = getIntent().getExtras().getString("CMD_NAME");


        tvBarName = findViewById(R.id.TvBarName);
        tvBarName.setText(commandName);

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
            View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_add_product, null);

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(bottomSheetView);

            TextInputLayout nameEditText = bottomSheetView.findViewById(R.id.nameEditText);
            TextInputLayout priceEditText = bottomSheetView.findViewById(R.id.priceEditText);
            TextInputLayout quantityEditText = bottomSheetView.findViewById(R.id.quantityEditText);

            Button btnAdd = bottomSheetView.findViewById(R.id.BtnAdd);
            btnAdd.setOnClickListener(v1 -> {

                String nameProduct = String.valueOf(Objects.requireNonNull(nameEditText.getEditText()).getText());
                String priceProduct= String.valueOf(Objects.requireNonNull(priceEditText.getEditText()).getText());
                String quantityProduct= String.valueOf(Objects.requireNonNull(quantityEditText.getEditText()).getText());

                if (nameProduct.length() == 0) {
                    nameEditText.setError("Insérer le nom du modèle");
                    return;
                }
                if (priceProduct.length() == 0) {
                    priceEditText.setError("Insérer le nom du modèle");
                    return;
                }
                if (quantityProduct.length() == 0) {
                    quantityEditText.setError("Insérer le nom du modèle");
                    return;
                }


                // Perform the login action here
                VolleyManager.addProduct(this, commandId, nameProduct, priceProduct,quantityProduct, myProgress, recyclerView,noDataTextView,progressBar);
                bottomSheetDialog.dismiss();
            });


            bottomSheetDialog.show();
        });

        findViewById(R.id.BtnBack).setOnClickListener(v -> onBackPressed());


    }

    @Override
    protected void onResume() {
        super.onResume();
        VolleyManager.getProducts(this,commandId,recyclerView,noDataTextView,progressBar);
    }

}