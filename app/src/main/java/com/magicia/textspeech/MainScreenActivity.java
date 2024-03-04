package com.magicia.textspeech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.magicia.textspeech.databinding.ActivityMainScreenBinding;

public class MainScreenActivity extends AppCompatActivity {

    ActivityMainScreenBinding binding;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(this,MainActivity.class));
        }



        binding.login.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
        binding.register.setOnClickListener(v->{
            startActivity(new Intent(this, RegisterActivity.class));
        });

    }
}