package com.magicia.textspeech;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.magicia.textspeech.databinding.ActivityMainBinding;

import java.io.File;
import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    TextToSpeech textToSpeech;
    private FirebaseAuth auth;

    private boolean toggle = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ShapeableImageView profile = findViewById(R.id.profile);
        auth = FirebaseAuth.getInstance();
        Log.e("Err", "User \n" + auth.getCurrentUser());

        textToSpeech = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.US);
            }
        });


        binding.speaker.setOnClickListener(v -> {
            String text = binding.textField.getText().toString().trim();
            if (text.isEmpty()) {
                binding.textField.setError("Text field should not be empty");
                return;
            }
            Voice voiceobj = new Voice("it-it-x-kda#male_2-local",
                    Locale.getDefault(), 1, 1, false, null);

            textToSpeech.setVoice(voiceobj);
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null,null);
            textToSpeech.speak(binding.textField.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        });

        binding.mic.setOnClickListener(v -> {
            checkPermission();
            listen();
        });

        binding.save.setOnClickListener(v -> {
            String fileName = binding.filename.getText().toString().trim();
            String text = binding.textField.getText().toString().trim();
            if (fileName.isEmpty()) {
                binding.filename.setError("File name should not be empty");
                return;
            }
            if (text.isEmpty()) {
                binding.textField.setError("File name should not be empty");
                return;
            }
            speakText(binding.textField.getText().toString().trim(), fileName);


        });
    }

    private void getGoogle() {
        String query = binding.filename.getText().toString();
        Uri uri = Uri.parse("https://www.google.com/search?q=" + query);
        Intent gSearchIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(gSearchIntent);

    }

    private void speakText(String text, String audioFileName) {


        String state = Environment.getExternalStorageState();
        boolean mExternalStorageWriteable = false;
        boolean mExternalStorageAvailable = false;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;

        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }

        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


        File dir = new File(directory + "/textToSpeech");
        dir.mkdirs();
        File file = new File(dir, audioFileName + ".mp3");
        int test = textToSpeech.synthesizeToFile((CharSequence) text, null, file,
                "tts");
        Toast.makeText(getApplicationContext(),dir.getName(),Toast.LENGTH_LONG).show();

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO}, 100);
            return;
        }

    }

    private void listen() {
        Intent intent
                = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

        try {
            startActivityForResult(intent, 100);
        } catch (Exception e) {
            Toast
                    .makeText(MainActivity.this, " " + e.getMessage(),
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                binding.textField.setText(
                        Objects.requireNonNull(result).get(0));
            }
        }
    }

}