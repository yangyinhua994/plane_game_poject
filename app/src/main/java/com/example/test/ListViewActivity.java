package com.example.test;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity {

    private static int index = 0;
    List<User> list = new ArrayList<>();
    private ListView listView;

    public static int getIndex() {
        return index;
    }

    public static void setIndex(int index) {
        ListViewActivity.index = index;
    }

    @SuppressLint({"Range", "Recycle"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.lv_list);
        MySQLite sqLite = new MySQLite(this, "user");
        SQLiteDatabase readableDatabase = sqLite.getReadableDatabase();
        String sql = "select username, number, create_time from user order by number DESC limit 0,10";
        Cursor cursor = readableDatabase.rawQuery(sql, null);
        int x = 1;
        while (cursor.moveToNext()){
            String number = cursor.getString(cursor.getColumnIndex("number"));
            String create_time = cursor.getString(cursor.getColumnIndex("create_time"));
            String username = cursor.getString(cursor.getColumnIndex("username"));
            list.add(new User(x, username,  number, create_time));
            x++;
        }
        readableDatabase.close();
        cursor.close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint({"ResourceType", "InflateParams"})
    @Override
    protected void onStart() {
        super.onStart();
        if (getIndex() == 1){
            listView.setAdapter(new ArrayAdapter<>(this, R.layout.item_main, list));
//            LinearLayout list_view_linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.activity_ranking2, null);
//            LinearLayout linearLayout = list_view_linearLayout.findViewById(R.id.linearLayout);
//            TextView textView1 = list_view_linearLayout.findViewById(R.id.list_view_textview1);
//            TextView textView2 = list_view_linearLayout.findViewById(R.id.list_view_textview2);
//            TextView textView3 = list_view_linearLayout.findViewById(R.id.list_view_textview3);
//            list_view_linearLayout.removeAllViews();
//            for (int i = 0; i < list.size(); i++) {
//                linearLayout.removeAllViews();
//                User user = list.get(i);
//                textView1.setText(String.valueOf(user.getRanking()));
//                linearLayout.addView(textView1);
//                textView2.setText(user.getNumber());
//                linearLayout.addView(textView2);
//                textView3.setText(user.getUsername());
//                linearLayout.addView(textView3);
//
//            }
//            list_view_linearLayout.addView(linearLayout);
//            setContentView(list_view_linearLayout);
        }
    }
}