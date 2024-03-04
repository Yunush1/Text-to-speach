package com.magicia.textspeech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.magicia.textspeech.databinding.ActivityRegisterBinding;

import java.util.Locale;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding  binding;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();

        binding.submit.setOnClickListener(v->{
            String email = Objects.requireNonNull(binding.email.getText()).toString().trim();
            String password = Objects.requireNonNull(binding.password.getText()).toString().trim();

            createUserWithEmailAndPassword(email,password);
        });
    }

    private void createUserWithEmailAndPassword(String email,String password){
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this,task -> {
                    if (task.isSuccessful()){
                        FirebaseUser firebaseUser = task.getResult().getUser();
                        updateUI(firebaseUser);
                    }else {
                        Toast.makeText(getApplicationContext(),"Try again sometime",Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void updateUI(FirebaseUser firebaseUser ){
        Log.e("Err",firebaseUser.toString());
        startActivity(new Intent(this,MainActivity.class));
    }
}