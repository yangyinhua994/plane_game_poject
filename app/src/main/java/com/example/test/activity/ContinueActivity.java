package com.example.test.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.test.R;

public class ContinueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue);
        Button ct = findViewById(R.id.Continue);
        Button restart = findViewById(R.id.restart);
        Button quit = findViewById(R.id.quit);
        ct.setOnClickListener(v -> {
            MainActivity.setMStatus(0);
            finish();
        });
        restart.setOnClickListener(v -> {
            MainActivity.setMStatus(1);
            finish();
        });
        quit.setOnClickListener(v -> {
            MainActivity.setMStatus(5);
            finish();
        });

    }
}