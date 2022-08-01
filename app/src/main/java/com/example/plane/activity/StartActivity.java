package com.example.plane.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.test.R;
import com.example.plane.view.Start;

public class StartActivity extends Activity {

    private static boolean save = true;
    private static Start start;
    private String username;
    private long time;

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

    public boolean onKeyDown(int keyCode, KeyEvent event) { //返回键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            long t=System.currentTimeMillis();//获取系统时间
            if(t-time<=500){
                finishAffinity();
                System.exit(0);
            }else{
                time=t;
                Toast.makeText(getApplicationContext(),this.getString(R.string.quit_game),Toast.LENGTH_SHORT).show();
            }

            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
