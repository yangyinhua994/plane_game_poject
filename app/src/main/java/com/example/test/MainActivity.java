package com.example.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private long time;
    private Start start;
    private boolean save = true;
    public static int mStatus = 0;
    private int index = 0;
    private String username;

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
        username = (String) getIntent().getExtras().get("username");
        start = new Start(this);
        start.setUsername(username);
        reStart(start);
    }

    @SuppressLint({"Range", "SetTextI18n","ResourceType"})
    @Override
    protected void onRestart() {
        super.onRestart();
        if(getMStatus() == 0 || getMStatus() == 1 ){
            reStart(new Start(this));
        }else if (getMStatus() == 2){
            buttonRestart();
        }else if(getMStatus() == 3 ){
            if (save){
                start.save();
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "请勿重复操作", Toast.LENGTH_SHORT).show();
            }
            save = false;
        }else {
            exit();
        }
    }

    @Override
    protected void onStart() {
        Log.e("===", "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.e("===", "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.e("===", "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.e("===", "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e("===", "onDestroy");
        super.onDestroy();
    }

    @SuppressLint("InflateParams")
    public void reStart(Start start){
        start.setThis(this);
        start.revive();
        setContentView(start);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) { //返回键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            long t=System.currentTimeMillis();//获取系统时间
            if(t-time<=500){
                exit(); //如果500毫秒内按下两次返回键则退出游戏
            }else{
                time=t;
                Toast.makeText(getApplicationContext(),"再按一次退出游戏",Toast.LENGTH_SHORT).show();
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
        save = true;
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