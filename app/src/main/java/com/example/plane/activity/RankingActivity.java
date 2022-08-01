package com.example.plane.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.example.plane.sqlite.MySQLite;
import com.example.test.R;
import com.example.plane.adapter.RankingAdapter;
import com.example.plane.dto.User;

import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    private final static String TAG = RankingActivity.class.getName();
    private List<User> userList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "======================onCreate======================");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        recyclerView = findViewById(R.id.recyclerView);
    }


    @Override
    protected void onStart() {
        Log.e(TAG, "======================onStart======================");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "======================onStop======================");
        super.onStop();
    }

    @SuppressLint({"Range", "ResourceType","InflateParams"})
    @Override
    protected void onResume() {
        Log.i(TAG, "======================onResume======================");
        super.onResume();
        MySQLite sqLite = new MySQLite(this, "user");
        SQLiteDatabase readableDatabase = sqLite.getReadableDatabase();
        String sql = "select username, number, create_time from user order by number DESC limit 0,10";
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RankingAdapter rankingAdapter = new RankingAdapter(this, userList);
        recyclerView.setAdapter(rankingAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onRestart() {
        Log.e(TAG, "======================onRestart======================");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "======================onPause======================");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "======================onDestroy======================");
        super.onDestroy();
    }


}