package com.studio.order.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.studio.order.R;
import com.studio.order.utils.VolleyManager;

import java.util.Objects;

public class MainActivityProductsDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_products_details);

       String productId = getIntent().getExtras().getString("PRO_ID");

        TextView tvBarName = findViewById(R.id.TvBarName);
        tvBarName.setText(getIntent().getExtras().getString("PRO_NAME"));


        TextInputEditText etName = findViewById(R.id.etName);
        etName.setText(getIntent().getExtras().getString("PRO_NAME"));

        TextInputEditText etPrice = findViewById(R.id.etPrice);
        etPrice.setText(getIntent().getExtras().getString("PRO_PRICE"));

        TextInputEditText etQuantity = findViewById(R.id.etQuantity);
        etQuantity.setText(getIntent().getExtras().getString("PRO_QUANTITY"));



        findViewById(R.id.BtnBack).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.BtnDelete).setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmation de suppression");
            builder.setMessage("Êtes-vous sûr de vouloir supprimer?");
            builder.setPositiveButton("Supprimer", (dialog, which) -> {

                VolleyManager.deleteProduct(MainActivityProductsDetails.this,productId);
            });
            builder.setNegativeButton("Annuler", (dialog, which) -> {
                // User clicked cancel, do nothing
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });


        findViewById(R.id.BtnUpdate).setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmation de mise à jour");
            builder.setMessage("Êtes-vous sûr de vouloir mettre à jour?");
            builder.setPositiveButton("Oui", (dialog, which) -> {

                String nameText = Objects.requireNonNull(etName.getText()).toString();
                String priceText = Objects.requireNonNull(etPrice.getText()).toString();
                String quantityText = Objects.requireNonNull(etQuantity.getText()).toString();

                VolleyManager.updateProduct(MainActivityProductsDetails.this,productId,nameText,priceText,quantityText);
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                // User clicked cancel, do nothing
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

    }
}