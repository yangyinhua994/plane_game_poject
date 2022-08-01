package com.example.plane.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MySQLite extends SQLiteOpenHelper {

    public MySQLite(@Nullable Context context, @Nullable String tableName) {
        super(context, tableName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user (_id integer primary key autoincrement, username varchar(30), number Interger(20), create_time Date)");
        db.execSQL("create table list (bulletJson varchar(255), enemyJson varchar(255)," +
                " blastJson varchar(255), myJson varchar(255)," + " username varchar(30), " +
                "millisecond varchar(50), fraction varchar(30))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
