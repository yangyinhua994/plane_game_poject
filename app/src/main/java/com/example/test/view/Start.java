package com.example.test.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.activity.MainActivity;
import com.example.test.activity.StartActivity;
import com.example.test.dto.Plane;
import com.example.test.sqlite.MySQLite;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressLint({"AppCompatCustomView", "ViewConstructor"})
public class Start extends TextView {

    private boolean threadRunState = true;
    private final int multiple = 10;
    private final Paint paint = new Paint();
    private CopyOnWriteArrayList<Plane> bulletList;
    private CopyOnWriteArrayList<Plane> enemyList;
    private CopyOnWriteArrayList<Plane> blastList;
    DisplayMetrics dm = getResources().getDisplayMetrics();
    private Plane my_plane;
    private Bitmap bulletBitmap;
    private int index = 0;
    private MediaPlayer mediaPlayerBlast;
    private MediaPlayer mediaPlayerBulletFiring;
    private MediaPlayer mediaPlayerBulletImpact;
    private final Context context;
    MainActivity mainActivity;
    private final String TAG = Start.class.getName();
    private int invincibleTime = 0;
    private String username;
    private Integer millisecond = 0;

    public Start(Context context, MainActivity mainActivity) {
        super(context);
        this.context = context;
        this.mainActivity = mainActivity;
        bulletList = new CopyOnWriteArrayList<>();
        enemyList = new CopyOnWriteArrayList<>();
        blastList = new CopyOnWriteArrayList<>();
        my_plane = setMyPlane();
        init();
    }

    @SuppressLint("Range")
    public Start(Context context, MainActivity mainActivity, boolean b) {
        super(context);
        this.context = context;
        this.mainActivity = mainActivity;
        if (b){
            Cursor cursor = getCursor(context, "list");
            if (cursor.moveToNext()){
                Gson gson = new Gson();
                Type type = new TypeToken<CopyOnWriteArrayList<Plane>>() {
                }.getType();
                bulletList = gson.fromJson(cursor.getString(cursor.getColumnIndex("bulletJson")), type);
                enemyList = gson.fromJson(cursor.getString(cursor.getColumnIndex("enemyJson")), type);
                blastList = gson.fromJson(cursor.getString(cursor.getColumnIndex("blastJson")), type);
                my_plane = gson.fromJson(cursor.getString(cursor.getColumnIndex("myJson")), Plane.class);
                deleteAllList();
                init();
            }
            cursor.close();
        }
    }

    private void init() {
        StartActivity.setAllData(this);
        MediaPlayer mediaPlayerBack = MediaPlayer.create(context, R.raw.back);
        mediaPlayerBlast = MediaPlayer.create(context, R.raw.blast);
        mediaPlayerBulletFiring = MediaPlayer.create(context, R.raw.bullet_firing);
        mediaPlayerBulletImpact = MediaPlayer.create(context, R.raw.bullet_impact);
        mediaPlayerBack.setLooping(true);
        mediaPlayerBack.start();
        bulletBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bullet);
        setBackgroundColor(Color.parseColor("#000000"));
        setTextColor(Color.parseColor("#ffffff"));
        setPadding(10,10,0,0);
        setTextSize(22);
        setOnTouchListener(this::onTouch);
        startThread();
    }

    @SuppressLint({"DrawAllocation", "SetTextI18n"})
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (threadRunState){
            for (int i = 0; i < bulletList.size(); i++) {
                Plane plane = bulletList.get(i);
                mediaPlayerBulletFiring.start();
                for (int i1 = 0; i1 < enemyList.size(); i1++) {
                    Plane plane1 = enemyList.get(i1);
                    boolean flag;
                    flag = inRange(plane, plane1.getX(), plane1.getX() + plane1.getBitmap().getWidth(), plane1.getY() - plane1.getBitmap().getHeight() / 2f);
                    if (flag) {
                        mediaPlayerBulletImpact.start();
                        try {
                            bulletList.remove(i);
                            enemyList.remove(i1);
                        }catch (Exception e){
                            Log.e(TAG, e.toString());
                        }
                        if (plane1.getLive() == 1){
                            plane1.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blast4));
                            mediaPlayerBlast.start();
                            blastList.add(plane1);
                            index++;
                        }else {
                            plane1.setLive(plane1.getLive() - 1);
                            enemyList.add(i1, plane1);
                        }
                    }
                }
                canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bullet),
                        plane.getX() + my_plane.getBitmap().getWidth() / 2f - 70,
                        plane.getY() + my_plane.getBitmap().getHeight(), paint);
            }
            for (Plane plane : blastList) {
                canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blast4), plane.getX(), plane.getY(), paint);
            }
            for (Plane plane : enemyList) {
                if (plane.getBitmapType() == 1){
                    plane.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.plan1));
                }else if (plane.getBitmapType() == 2){
                    plane.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.plan2));
                }else if (plane.getBitmapType() == 3){
                    plane.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.plan3));
                }else{
                    plane.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.plan4));
                }
                canvas.drawBitmap(plane.getBitmap(), plane.getX(), plane.getY(), paint);
                if (invincibleTime == 0){
                    if (inRange(my_plane, plane.getX(), plane.getX() + plane.getBitmap().getWidth(), plane.getY() + plane.getBitmap().getHeight())) {
                        threadRunState = false;
                        Intent intent = new Intent(mainActivity, StartActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username);
                        intent.putExtras(bundle);
                        mainActivity.startActivity(intent);
                        try {
                            Thread.sleep(20L * multiple);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        my_plane.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blast4));
                        blastList.add(my_plane);
                    }
                }
            }
            this.setText(mainActivity.getString(R.string.destroy_enemy_plane) + "：" + index);
            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.my_plan), my_plane.getX(), my_plane.getY(), paint);
        }
    }

    public static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean onTouch(View v, MotionEvent event) {
        int eventX = (int) event.getX();
        int eventY = (int) event.getY();
        int x = (int) (eventX - my_plane.getX());
        int y = (int) (eventY - my_plane.getY());
        int min_x = my_plane.getBitmap().getWidth() / 2;
        int max_x = dm.widthPixels - min_x;
        int min_y = my_plane.getBitmap().getHeight() / 2;
        int max_y = dm.heightPixels - min_y;
        if (x >= 0 && y >= 0) {
            if (eventX >= min_x && eventX <= max_x) {
                my_plane.setX(eventX - min_x);
            }
            if (eventY >= min_y && eventY <= max_y) {
                my_plane.setY(eventY - min_y);
            }
        }
        return true;
    }

    public void save() {
        MySQLite mySQLite = new MySQLite(context, "user");
        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
        String sql = "insert into user (username, number, create_time) values ( "+"'"+username+"',"+index+","+"'"+new Date(System.currentTimeMillis())+"')";
        writableDatabase.execSQL(sql);
    }

    public Plane setMyPlane(){
        return new Plane(dm.widthPixels / 2f, dm.heightPixels - 300, BitmapFactory.decodeResource(getResources(), R.drawable.my_plan), 0);
    }

    public void startThread() {
        new Thread(new refresh()).start();
        new Thread(new MyBullet()).start();
        new Thread(new EnemyPlane()).start();
        new Thread(new Blast()).start();
        runTimer();
        threadRunState = true;
    }

    public void startThread(int invincibleTime) {
        threadRunState = true;
        if (bulletList.size() != 0 || enemyList.size() != 0 || blastList.size() != 0){
            this.invincibleTime = invincibleTime;
        }
        my_plane = setMyPlane();
        new Thread(new InvincibleTime()).start();
        new Thread(new refresh()).start();
        new Thread(new MyBullet()).start();
        new Thread(new EnemyPlane()).start();
        new Thread(new Blast()).start();
        runTimer();
    }

    public void setUsername(String username) {
        if (this.username == null){
            this.username = username;
        }
    }

    class InvincibleTime implements Runnable{

         @Override
         public void run() {
             try {
                 Thread.sleep(300 * multiple);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
             invincibleTime = 0;
         }
     }

     public void runTimer(){
        int time = 130;
        new Thread(){
            @Override
            public void run() {
                super.run();
                while (threadRunState){
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    millisecond += time;
                }
            }
        }.start();
     }

    class Blast implements Runnable {
        public Blast() {
            new Thread(this).start();
        }
        @Override
        public void run() {
            while (threadRunState) {
                sleep(100L * multiple);
                blastList = new CopyOnWriteArrayList<>();
            }
        }
    }

    class refresh implements Runnable {

        @Override
        public void run() {
            while (threadRunState) {
                sleep(1);
                postInvalidate();
            }
        }
    }

    class MyBullet implements Runnable {

        @Override
        public void run() {
            while (threadRunState) {
                sleep(5L * multiple);
                Plane plane = new Plane(my_plane.getX() + my_plane.getBitmap().getWidth() / 2f - bulletBitmap.getWidth() / 2f,
                        my_plane.getY() - my_plane.getBitmap().getHeight(),
                        bulletBitmap,
                        5);
                bulletList.add(plane);
                for (int i = 0; i < bulletList.size(); i++) {
                    Plane plane1 = bulletList.get(i);
//                        小于让子弹有飞出屏幕的效果
                    if (plane1.getY() < -500) {
                        bulletList.remove(i);
                    } else {
                        plane1.setY(plane1.getY() - bulletBitmap.getHeight());
                    }
                }
            }
        }
    }

    class EnemyPlane implements Runnable {

        @Override
        public void run() {
            while (threadRunState) {
                sleep(30L * multiple);
                int i = new Random().nextInt(4) + 1;
                Bitmap bitmap;
                int live = 3;
                if (i == 1) {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plan1);
                } else if (i == 2) {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plan2);
                    live = 4;
                } else if (i == 3) {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plan3);
                    live = 5;
                } else {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plan4);
                    live = 6;
                }
                enemyList.add(new Plane(new Random().nextInt(dm.widthPixels - bitmap.getWidth()),
                        -bitmap.getHeight(), bitmap, live, i));
                for (int i1 = 0; i1 < enemyList.size(); i1++) {
                    Plane plane = enemyList.get(i1);
                    if (plane.getY() >= dm.heightPixels) {
                        try {
                            enemyList.remove(i1);
                        }catch (Exception e){
                            Log.e(TAG, e.toString());
                        }
                    } else {
                        plane.setY(plane.getY() + 50);
                    }
                }
                for (int i1 = 0; i1 < blastList.size(); i1++) {
                    Plane plane = blastList.get(i1);
                    plane.setY(plane.getY() + 50);
                }
            }
        }
    }

    public static Cursor getCursor(Context context, String databaseName){
        MySQLite sqLite = new MySQLite(context, databaseName);
        SQLiteDatabase readableDatabase = sqLite.getReadableDatabase();
        return readableDatabase.rawQuery("select * from " + databaseName, null);

    }

    public static boolean checkState(Context context){
        Cursor cursor = Start.getCursor(context, "list");
        return cursor.moveToNext();
    }

    public void saveAllList(){
        if (threadRunState){
            Gson gson = new Gson();
            String bulletJson = gson.toJson(bulletList, new TypeToken<List<Plane>>() {}.getType());
            String enemyJson = gson.toJson(enemyList, new TypeToken<List<Plane>>() {}.getType());
            String blastJson = gson.toJson(blastList, new TypeToken<List<Plane>>() {}.getType());
            String myJson = gson.toJson(my_plane);
            MySQLite sqLite = new MySQLite(mainActivity, "list");
            SQLiteDatabase writableDatabase = sqLite.getWritableDatabase();
            String sql = "insert into list (bulletJson, enemyJson, blastJson, myJson, username, " +
                    "millisecond, fraction) " +
                    "values ('"+bulletJson+"','"+enemyJson+"','"+blastJson+"'" +
                    ",'"+myJson+"','"+username+"','"+millisecond+"','"+index+"')";
            writableDatabase.execSQL(sql);
            writableDatabase.close();
            sqLite.close();
            Log.i(TAG, "数据保存成功");
        }
    }

    public void deleteAllList(){
        MySQLite sqLite = new MySQLite(mainActivity, "list");
        SQLiteDatabase writableDatabase = sqLite.getWritableDatabase();
        String sql = "delete from list";
        writableDatabase.execSQL(sql);
        writableDatabase.close();
        sqLite.close();
        Log.i(TAG, "数据删除成功");
    }

    public void revive() {
        threadRunState = true;
    }

    public void setThis(MainActivity mainActivity) {
        if (mainActivity != null){
            this.mainActivity = mainActivity;
        }
    }

    public static boolean inRange(Plane plane, float minX, float maxX, float y) {
        if (y >= plane.getY() && y <= plane.getY() + plane.getBitmap().getHeight()){
            if (plane.getX() >= minX && plane.getX() <= maxX) return true;
            return plane.getX() + plane.getBitmap().getWidth() >= minX && plane.getX() + plane.getBitmap().getWidth() <= maxX;
        }else {
            return false;
        }
    }

}
