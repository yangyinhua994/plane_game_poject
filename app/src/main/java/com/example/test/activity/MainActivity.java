package com.example.test.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test.R;
import com.example.test.view.Start;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private long time;
    private Start start;
    public static int mStatus = -1;
    private String username;
    private boolean state = true;
    private final String TAG = this.getClass().getName();


    public static void setMStatus(int i) {
        mStatus = i;
    }

    public static int getMStatus() {
        return mStatus;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"ClickableViewAccessibility", "InflateParams", "CutPasteId", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = Start.checkState(this);
    }

    @SuppressLint({"Range", "SetTextI18n","ResourceType"})
    @Override
    protected void onRestart() {
        Log.e(TAG, "==================onRestart==================");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Log.e(TAG, "==================onStart==================");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.e(TAG, "==================onResume==================");
        super.onResume();
        if (state){
            startActivity(new Intent(this, ContinueActivity.class));
            state = false;
        }else {
            if (getMStatus() == -1){
                setContentView(R.layout.activity_main);
            }else if (getMStatus() == 0){
                start = new Start(this, this, true);
                reStart(start);
            }else if(getMStatus() == 1){
                start = new Start(this, this);
                reStart(start);
            }else if (getMStatus() == 2){
                buttonRestart();
            }else if (getMStatus() == 5){
                exit();
            }
        }
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "==================onPause==================");
        super.onPause();
        if (start != null){
            start.saveAllList();
        }

    }

    @Override
    protected void onStop() {
        Log.e(TAG, "==================onStop==================");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "==================onDestroy==================");
        super.onDestroy();
    }

    @SuppressLint("InflateParams")
    public void reStart(Start start){
        start.setThis(this);
        start.revive();
        start.setUsername(username);
        setContentView(start);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputMethodManager.isActive()){
                inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            }
            onClick(findViewById(R.id.usernameText));
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @SuppressLint("Recycle")
    public void onClick(View view) {
        EditText nameText = findViewById(R.id.usernameText);
        if (nameText.getText() == null | Objects.equals(nameText.getText(), R.string.username_is_long)){
            username = this.getString(R.string.unknown_user);
        }else {
            username = nameText.getText().toString();
        }
        if (username.length() >= 9){
            Toast.makeText(this, this.getString(R.string.username_is_long), Toast.LENGTH_SHORT).show();
        }else {
            start = new Start(this, this);
            reStart(start);
        }

    }

    public void closeOnClick(View view) {
        onClick(view);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) { //返回键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            long t=System.currentTimeMillis();//获取系统时间
            if(t-time<=500){
                exit(); //如果500毫秒内按下两次返回键则退出游戏
            }else{
                time=t;
                Toast.makeText(getApplicationContext(),this.getString(R.string.quit_game),Toast.LENGTH_SHORT).show();
            }

            return true;
        }
        return false;

    }
    public void exit(){
        MainActivity.this.finish();
        new Thread(() -> {
            try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
            System.exit(0);
        }).start();
    }

    public void buttonRestart(){
        start.startThread(1);
        setContentView(start);
    }


    public static int dp2px(Context context, float dp){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dp(Context context, float px){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px/scale + 035f);
    }

}