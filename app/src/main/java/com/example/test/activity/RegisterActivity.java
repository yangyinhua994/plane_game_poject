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


public class RegisterActivity extends AppCompatActivity {

    private long time;
    private final static String TAG = "RegisterActivity";
    private String defaultName;
    private String usernameIsLong;
    private String name;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "======================onCreate======================");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        defaultName = this.getString(R.string.unknown_user);
        usernameIsLong = this.getString(R.string.username_is_long);
        name = this.getString(R.string.username);
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "======================onStart======================");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "======================onStop======================");
        super.onStop();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "======================onResume======================");
        super.onResume();

    }

    @Override
    protected void onRestart() {
        Log.i(TAG, "======================onRestart======================");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "======================onPause======================");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "======================onDestroy======================");
      super.onDestroy();
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
        String username = nameText.getText().toString();
        if (username.length() >= 9){
            Toast.makeText(this, usernameIsLong, Toast.LENGTH_SHORT).show();
        }else {
            if (username.equals("") || username.equals(name)){
                username = defaultName;
            }
            Bundle bundle = new Bundle();
            Intent intent = new Intent(this, MainActivity.class);
            bundle.putString("username", username);
            intent.putExtras(bundle);
            startActivity(intent);
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
        this.finish();
        new Thread(() -> {
            try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
            System.exit(0);
        }).start();
    }

}