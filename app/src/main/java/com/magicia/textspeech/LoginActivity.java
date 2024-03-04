package com.magicia.textspeech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TotpSecret;
import com.magicia.textspeech.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth  = FirebaseAuth.getInstance();
        binding.submit.setOnClickListener(v->{
            String email = String.valueOf(binding.email.getText());
            String password = String.valueOf(binding.password.getText());
            signInWithEmailAndPassword(email,password);
        });
    }

    private void signInWithEmailAndPassword(String email,String password){
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this,task -> {
                    if (task.isSuccessful()){
                        FirebaseUser firebaseUser = task.getResult().getUser();
                        updateUI(firebaseUser);
                    }else {
                        Toast.makeText(getApplicationContext(),"Try again", Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void updateUI(FirebaseUser firebaseUser ){
        startActivity(new Intent(this,MainActivity.class));
    }
}