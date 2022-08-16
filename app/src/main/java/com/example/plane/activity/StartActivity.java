package com.example.plane.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.plane.base.BaseActivity;
import com.example.plane.utils.IntentKey;
import com.example.plane.view.StartView;
import com.example.test.R;

public class StartActivity extends BaseActivity {

    private static boolean save = true;
    private static StartView startView;
    private String username;
    private long time;
    private final int standardWidthMulti = 12;
    private final int standardHeightMulti = 12;
    private final int standardViewHeightMulti = 20;
    private final int buttonViewTopMulti = 30;

    public static void setStart(StartView startView) {
        StartActivity.startView = startView;
    }

    public static void setAllData(StartView startView) {
        setStart(startView);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_main);
        if (username == null) {
            username = (String) getIntent().getExtras().get(IntentKey.getInstance().username);
        }
        LinearLayout parentView = findViewById(R.id.startRelativeLayout);
        LinearLayout.LayoutParams paramsParent = new LinearLayout.LayoutParams(this.width , this.height);
        parentView.setLayoutParams(paramsParent);
        parentView.setPadding(this.width / standardWidthMulti, this.height/standardHeightMulti,
                this.width / standardWidthMulti, ZERO);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                this.height/standardViewHeightMulti);
        buttonParams.setMargins(ZERO, this.height/ buttonViewTopMulti, ZERO, ZERO);
        ImageView gameIcView = findViewById(R.id.iv_game_ic);
        LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams((this.width / gameOverImageWidthMulti), (this.height / gameOverImageHeightMulti));
        gameIcView.setLayoutParams(imageViewParams);
        TextView buttonRestart = findViewById(R.id.buttonRestart);
        buttonRestart.setLayoutParams(buttonParams);
        buttonRestart.setOnClickListener(v -> {
//            开始游戏
            MainActivity.setMStatus(this.startGame);
            save = true;
            finish();
        });
        TextView buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setLayoutParams(buttonParams);
        buttonSave.setOnClickListener(view -> {
            String text;
            if (save) {
                save = false;
                startView.save();
                text = this.getString(R.string.save_successfully);
            } else {
                text = this.getString(R.string.repeat_is_operation);
            }
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        });
        TextView buttonSelect = findViewById(R.id.buttonSelect);
        buttonSelect.setLayoutParams(buttonParams);
        buttonSelect.setOnClickListener(view -> {
            startActivity(new Intent(this, RankingActivity.class));
        });
        TextView buttonQuit = findViewById(R.id.buttonQuit);
        buttonQuit.setLayoutParams(buttonParams);
        buttonQuit.setOnClickListener(v -> {
            MainActivity.setMStatus(this.quitGame);
            finish();
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) { //返回键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == this.systemOverSleepTime) {
            long t = System.currentTimeMillis();//获取系统时间
            if (t - time <= 500) {
                finishAffinity();
                startView.deleteAllList();
                System.exit(this.systemOverSleepTime);
            } else {
                time = t;
                Toast.makeText(getApplicationContext(), this.getString(R.string.quit_game), Toast.LENGTH_SHORT).show();
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
