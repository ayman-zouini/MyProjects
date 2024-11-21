package com.studio.order.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.studio.order.R;
import com.studio.order.utils.VolleyManager;

import java.util.Objects;



public class MainActivityLogin extends AppCompatActivity {

    private TextInputLayout emailEditText;
    private TextInputLayout passwordEditText;
    private ProgressDialog myProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);


        myProgress = new ProgressDialog(this);
        myProgress.setTitle(getString(R.string.msg_progress_title_login));
        myProgress.setMessage(getString(R.string.msg_progress_message_login));
        myProgress.setCancelable(false);
        myProgress.setIndeterminate(true);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button btnLogin = findViewById(R.id.BtnLogin);

        btnLogin.setOnClickListener(v -> {
            String email = String.valueOf(Objects.requireNonNull(emailEditText.getEditText()).getText());
            String password =String.valueOf(Objects.requireNonNull(passwordEditText.getEditText()).getText());

            if (email.length() == 0) {
                emailEditText.setError(getString(R.string.msg_error_mail));
                return;
            }
            if (password.length() < 6) {
                passwordEditText.setError(getString(R.string.msg_error_pass));
                return;
            }

            // Perform the login action here
            VolleyManager.login(MainActivityLogin.this, emailEditText, passwordEditText, myProgress);

            myProgress.show();

        });



    }


}