package com.walkiriaapps.firebaseusermanagement.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.walkiriaapps.firebaseusermanagement.R;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mAuth = FirebaseAuth.getInstance();

        userName = findViewById(R.id.user_name);
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
            }
        });
        userName.setText("WELCOME "+mAuth.getCurrentUser().getDisplayName());
    }
}
