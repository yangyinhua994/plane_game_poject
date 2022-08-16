package com.example.plane.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.plane.activity.RankingActivity;
import com.example.plane.sqlite.MySQLite;
import com.example.plane.utils.IntentKey;
import com.example.test.R;

import java.util.ArrayList;

@SuppressLint("all")
public class SimpleWidgetProvider extends AppWidgetProvider {
    private static final String CLICK_ACTION = "com.example.widget.CLICK";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        if (action.equals(CLICK_ACTION)){
            RankingActivity.isAppWidget = true;
            ComponentName cmp=new ComponentName("com.example.test", "com.example.plane.activity.RankingActivity");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            context.startActivity(intent);
        }
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = updateData(context);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            appWidgetManager.updateAppWidget(new ComponentName(context, SimpleWidgetProvider.class), remoteViews);
        }
    }

    public RemoteViews updateData(Context context){
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
        Intent intentClick = new Intent();
        //这个必须要设置，不然点击效果会无效
        intentClick.setClass(context, SimpleWidgetProvider.class);
        intentClick.setAction(CLICK_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentClick, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.line1, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.line2, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.line3, pendingIntent);
        ArrayList<String> list = new ArrayList<>();
        MySQLite mySQLite = new MySQLite(context, "user");
        SQLiteDatabase readableDatabase = mySQLite.getReadableDatabase();
        String sql = "select username, number, create_time from user order by " +
                "number DESC limit 0,3";
        Cursor cursor = readableDatabase.rawQuery(sql, null);
        for (int x = 0; x <= 2; x++){
            if (cursor.moveToNext()){
                list.add(cursor.getString(cursor.getColumnIndex("number")));
            }else {
                list.add("");
            }
        }
        cursor.close();
        readableDatabase.close();
        mySQLite.close();
        remoteViews.setTextViewText(R.id.textview1, list.get(0));
        remoteViews.setTextViewText(R.id.textview2, list.get(1));
        remoteViews.setTextViewText(R.id.textview3, list.get(2));
        return remoteViews;
    }

}