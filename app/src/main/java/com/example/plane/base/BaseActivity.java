package com.example.plane.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

public class BaseActivity extends Activity {

    public int width, height;
    public final int initState = 1;
    public final static int initGameState = 2;
    public final int initData = 3;
    public final int startGame = 4;
    public final int quitGame = 5;
    public final int errorExitStartGame = 6;
    public final int standardWidthMulti = 16;
    public final int leftAddRight = 60;
    public final int ZERO = 0;
    public final int standardHeightMulti = 3;
    public final int gameOverImageHeightMulti= 5;
    public final int gameOverImageWidthMulti= 2;
    public final int systemOverSleepTime= 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        WindowManager windowManager = getWindow().getWindowManager();
        Point point = new Point();
        windowManager.getDefaultDisplay().getRealSize(point);
        //屏幕实际宽度（像素个数）
        width = point.x;//480
        //屏幕实际高度（像素个数）
        height = point.y;//854

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
