package com.example.plane.activity;

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
import com.example.plane.view.Start;

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
        if (getMStatus() != -2){
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
                    if (start != null){
                        start.restartInit();
                        reStart(start);
                    }
                }else if (getMStatus() == 2){
                    buttonRestart();
                }else if (getMStatus() == 5){
                    if (start != null){
                        start.deleteAllList();
                    }
                    finishAffinity();
                    System.exit(0);
                }
            }
        }
        if (start != null){
            start.setThreadRunState(true);
        }
        setMStatus(-2);
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "==================onPause==================");
        super.onPause();
        if (start != null && start.isThreadRunState()){
            start.saveAllList();
            start.setThreadRunState(false);
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
        start.setMainActivity(this);
        start.setThreadRunState(true);
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
        EditText editText = findViewById(R.id.usernameText);
        username = editText.getText().toString();
        if (username.equals("")){
            username = this.getString(R.string.unknown_user);
        }
        start = new Start(this, this);
        reStart(start);
    }

    public void closeOnClick(View view) {
        onClick(view);
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

    public void buttonRestart(){
        start.initMyPlane();
        start.setThreadRunState(true);
        start.setInvincible(true);
        start.invincibleTime();
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