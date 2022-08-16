package com.example.plane.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plane.base.BaseActivity;
import com.example.plane.sqlite.MySQLite;
import com.example.plane.view.StartView;
import com.example.test.R;

public class ContinueActivity extends BaseActivity {

    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue);
        TextView continueTextView = findViewById(R.id.continueTextView);
        LinearLayout.LayoutParams continueTextViewParamsParent =
                new LinearLayout.LayoutParams((this.width - leftAddRight),
                        (this.height / standardHeightMulti));
        continueTextViewParamsParent.setMargins(this.width / this.standardWidthMulti, ZERO,
                this.width / this.standardWidthMulti, ZERO);
        continueTextView.setLayoutParams(continueTextViewParamsParent);
        Button ct = findViewById(R.id.Continue);
        Button restart = findViewById(R.id.restart);
        Button quit = findViewById(R.id.quit);
        ct.setOnClickListener(v -> {
            MainActivity.setMStatus(initData);
            finish();
        });
        restart.setOnClickListener(v -> {
            MainActivity.setMStatus(this.errorExitStartGame);
            finish();
        });
        quit.setOnClickListener(v -> {
            MainActivity.setMStatus(quitGame);
            finish();
        });

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) { //返回键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == this.systemOverSleepTime){
            long t=System.currentTimeMillis();//获取系统时间
            if(t-time<=500){
                finishAffinity();
                System.exit(this.systemOverSleepTime);
            }else{
                time=t;
                Toast.makeText(getApplicationContext(),this.getString(R.string.quit_game),Toast.LENGTH_SHORT).show();
            }

            return true;
        }
        return false;
    }
    
}