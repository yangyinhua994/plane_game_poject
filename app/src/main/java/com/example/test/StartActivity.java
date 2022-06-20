package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class StartActivity extends Activity {

    private static boolean save = true;
    private static Start start;
    private String username;

    public static Start getStart() {
        return start;
    }

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
            if (save){
                save = false;
                start.save();
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "请勿重复操作", Toast.LENGTH_SHORT).show();
            }
        });
        Button buttonSelect = findViewById(R.id.buttonSelect);
        buttonSelect.setOnClickListener(view -> {
            startActivity(new Intent(this, ListViewActivity.class));
            ListViewActivity.setIndex(1);
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
