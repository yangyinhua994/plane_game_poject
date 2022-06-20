package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class ranking_activity extends AppCompatActivity {

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking3);
        MySQLite sqLite = new MySQLite(this, "user");
        SQLiteDatabase readableDatabase = sqLite.getReadableDatabase();
        String sql = "select username, number, create_time from user order by number DESC limit 0,10";
        Cursor cursor = readableDatabase.rawQuery(sql, null);
        int x = 1;
        while (cursor.moveToNext()){
            String number = cursor.getString(cursor.getColumnIndex("number"));
            String create_time = cursor.getString(cursor.getColumnIndex("create_time"));
            String username = cursor.getString(cursor.getColumnIndex("username"));
            x++;
        }
        readableDatabase.close();
        cursor.close();

    }
}