package com.example.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.test.R;
import com.example.test.view.Start;

public class StartActivity extends Activity {

    private static boolean save = true;
    private static Start start;
    private String username;

    public static void setStart(Start start) {
        StartActivity.start = start;
    }

    public static void setAllData(Start start) {
       setStart(start);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_main);
        if (username == null){
            username = (String) getIntent().getExtras().get("username");
        }
        Button buttonRestart = findViewById(R.id.buttonRestart);
        buttonRestart.setOnClickListener(v -> {
            MainActivity.setMStatus(1);
            finish();
        });
        Button buttonRevive = findViewById(R.id.buttonRevive);
        buttonRevive.setOnClickListener(view -> {
            MainActivity.setMStatus(2);
            save = true;
            finish();
        });
        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(view -> {
            String text;
            if (save){
                save = false;
                start.save();
                text = this.getString(R.string.save_successfully);
            }else {
                text = this.getString(R.string.repeat_is_operation);
            }
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        });
        Button buttonSelect = findViewById(R.id.buttonSelect);
        buttonSelect.setOnClickListener(view -> {
            startActivity(new Intent(this, RankingActivity.class));
        });
        Button buttonQuit = findViewById(R.id.buttonQuit);
        buttonQuit.setOnClickListener(v -> {
            MainActivity.setMStatus(5);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
