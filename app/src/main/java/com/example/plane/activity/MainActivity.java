package com.example.plane.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.plane.base.BaseActivity;
import com.example.plane.sqlite.MySQLite;
import com.example.plane.utils.IntentKey;
import com.example.test.R;
import com.example.plane.view.StartView;

@SuppressLint("all")
public class MainActivity extends BaseActivity {

    private long time;
    private StartView startView;
    public static int mStatus = initGameState;
    private String username;
    private boolean state = true;
    private final String TAG = this.getClass().getName();
    private boolean DEBUG = true;

    public static void setMStatus(int i) {
        mStatus = i;
    }

    public static int getMStatus() {
        return mStatus;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        state = StartView.checkState(this);

    }

    @Override
    protected void onRestart() {
        if (DEBUG){
            Log.e(TAG, "==================onRestart==================");
        }
        super.onRestart();
    }

    @Override
    protected void onStart() {
        if (DEBUG){
            Log.e(TAG, "==================onStart==================");
        }
        super.onStart();
    }

    @Override
    protected void onResume() {
        if (DEBUG){
            Log.e(TAG, "==================onResume==================");
        }
        super.onResume();
        if (getMStatus() != initState){
            if (state){
                startActivity(new Intent(this, ContinueActivity.class));
                state = false;
            }else {
                if (getMStatus() == initGameState || getMStatus() == isAppWidget){
                    TextView mainNullTextView = findViewById(R.id.mainNullTextView);
                    LinearLayout.LayoutParams mainNullTextViewParamsParent =
                            new LinearLayout.LayoutParams((this.width - leftAddRight),
                                    (this.height / this.standardHeightMulti));
                    mainNullTextViewParamsParent.setMargins(this.width / this.standardWidthMulti,
                            ZERO, this.width / this.standardWidthMulti, ZERO);
                    mainNullTextView.setLayoutParams(mainNullTextViewParamsParent);
                }else if (getMStatus() == this.initData){
                    startView = new StartView(this.getBaseContext(), this, true);
                    reStart(startView);
                }else if(getMStatus() == startGame){
                    startView = new StartView(this.getBaseContext(), this);
                    startView.restartInit();
                    reStart(startView);
                } else if (getMStatus() == quitGame){
                    startView = new StartView(this.getBaseContext(), this);
                    startView.deleteAllList();
                    finishAffinity();
                    System.exit(this.systemOverSleepTime);
                }else if (getMStatus() == this.errorExitStartGame){
//                    异常关闭的重新开始
                    startView = new StartView(this.getBaseContext(), this);
                    setContentView(startView);
                }
            }
        }
        if (startView != null){
            startView.setThreadRunState(true);
        }
        setMStatus(initState);
    }

    @Override
    protected void onPause() {
        if (DEBUG){
            Log.e(TAG, "==================onPause==================");
        }
        super.onPause();
        if (startView != null && startView.isThreadRunState()){
            startView.saveAllList();
            startView.setThreadRunState(false);
        }
    }

    @Override
    protected void onStop() {
        if (DEBUG){
            Log.e(TAG, "==================onStop==================");
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (DEBUG){
            Log.e(TAG, "==================onDestroy==================");
        }
        super.onDestroy();
    }

    public void reStart(StartView startView){
        startView.setMainActivity(this);
        startView.setThreadRunState(true);
        startView.setUsername(username);
        setContentView(startView);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputMethodManager.isActive()){
                inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), ZERO);
            }
            onClick(findViewById(R.id.usernameText));
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public void onClick(View view) {
        EditText editText = findViewById(R.id.usernameText);
        username = editText.getText().toString();
        if (username.equals("")){
            username = this.getString(R.string.unknown_user);
        }
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.getInstance().username, username);
        Intent intent = new Intent(this, StartActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void closeOnClick(View view) {
        onClick(view);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) { //返回键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == this.systemOverSleepTime){
            long t=System.currentTimeMillis();//获取系统时间
            if(t-time<=500){
                finishAffinity();
                if (startView != null){
                    startView.deleteAllList();
                }
                System.exit(this.systemOverSleepTime);
            }else{
                time=t;
                Toast.makeText(getApplicationContext(),this.getString(R.string.quit_game),Toast.LENGTH_SHORT).show();
            }

            return true;
        }
        return false;
    }

    public void buttonRestart(){
        startView.initMyPlane();
        startView.setThreadRunState(true);
        startView.setInvincible(true);
        startView.invincibleTime();
        setContentView(startView);
    }

}