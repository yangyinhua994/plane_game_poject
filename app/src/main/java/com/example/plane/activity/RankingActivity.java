package com.example.plane.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.plane.base.BaseActivity;
import com.example.plane.sqlite.MySQLite;
import com.example.plane.utils.IntentKey;
import com.example.test.R;
import com.example.plane.adapter.RankingAdapter;
import com.example.plane.dto.User;
import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends BaseActivity {

    private final static String TAG = RankingActivity.class.getName();
    private final List<User> userList = new ArrayList<>();
    private RecyclerView recyclerView;
    private boolean isOne;
    public static boolean isAppWidget = false;

    private static boolean DEBUG = false;


    public static void setDEBUG(boolean b) {
        DEBUG = b;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DEBUG){
            Log.e(TAG, "======================onCreate======================");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        recyclerView = findViewById(R.id.recyclerView);
        isOne = true;
    }


    @Override
    protected void onStart() {
        if (DEBUG){
            Log.e(TAG, "======================onStart======================");
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (DEBUG){
            Log.e(TAG, "======================onStop======================");
        }
        super.onStop();
    }

    @SuppressLint({"Range", "ResourceType","InflateParams"})
    @Override
    protected void onResume() {
        if (DEBUG){
            Log.i(TAG, "======================onResume======================");
        }
        super.onResume();
        if (isOne){
            MySQLite sqLite = new MySQLite(this, "user");
            SQLiteDatabase readableDatabase = sqLite.getReadableDatabase();
            String sql = "select username, number, create_time from user order by " +
                    "number DESC limit 0,10";
            Cursor cursor = readableDatabase.rawQuery(sql, null);
            int x = 1;
            while (cursor.moveToNext()){
                String number = cursor.getString(cursor.getColumnIndex("number"));
                String create_time = cursor.getString(cursor.getColumnIndex("create_time"));
                String username = cursor.getString(cursor.getColumnIndex("username"));
                userList.add(new User(x, username, number, create_time));
                x++;
            }
            readableDatabase.close();
            cursor.close();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false);
            RankingAdapter rankingAdapter = new RankingAdapter(this, userList);
            recyclerView.setAdapter(rankingAdapter);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        isOne = false;
    }

    @Override
    protected void onRestart() {
        if (DEBUG){
            Log.e(TAG, "======================onRestart======================");
        }
        super.onRestart();
    }

    @Override
    protected void onPause() {
        if (DEBUG){
            Log.e(TAG, "======================onPause======================");
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (DEBUG){
            Log.e(TAG, "======================onDestroy======================");
        }
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) { //返回键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == this.systemOverSleepTime){
            if (isAppWidget){
                startActivity(new Intent(this, MainActivity.class));
                isAppWidget = false;
                MainActivity.setMStatus(BaseActivity.isAppWidget);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}