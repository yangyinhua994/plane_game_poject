package com.example.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.sql.Date;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressLint("AppCompatCustomView")
public class Start extends TextView {

    private boolean threadRunState = true;
    private final int multiple = 10;
    private final Paint paint = new Paint();
    private CopyOnWriteArrayList<Plane> bulletList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Plane> enemyList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Plane> blastList = new CopyOnWriteArrayList<>();
    DisplayMetrics dm = getResources().getDisplayMetrics();
    private Plane my_plane;
    private Bitmap bulletBitmap;
    private int index = 0;
    private MediaPlayer mediaPlayerBack;
    private MediaPlayer mediaPlayerBlast;
    private MediaPlayer mediaPlayerBulletFiring;
    private MediaPlayer mediaPlayerBulletImpact;
    private Context context;
    MainActivity mainActivity;
    private final String TAG = Start.class.getName();
    private int invincibleTime = 0;
    private String username;
    private Chronometer ch;

    public Start(Context context) {
        this(context, null);
//        ch.setBase(SystemClock.elapsedRealtime());
        StartActivity.setAllData(this);
    }

    private void init() {
        my_plane = setMyPlane();
        bulletBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bullet);
        setBackgroundColor(Color.parseColor("#000000"));
        setTextColor(Color.parseColor("#ffffff"));
        setPadding(10,10,0,0);
        setTextSize(22);
        setOnTouchListener(this::onTouch);
        startThread();
    }
    
    public Start(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public Start(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mediaPlayerBack = MediaPlayer.create(context, R.raw.back);
        mediaPlayerBlast = MediaPlayer.create(context, R.raw.blast);
        mediaPlayerBulletFiring = MediaPlayer.create(context, R.raw.bullet_firing);
        mediaPlayerBulletImpact = MediaPlayer.create(context, R.raw.bullet_impact);
        mediaPlayerBack.setLooping(true);
        mediaPlayerBack.start();
        init();
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
//                边缘相撞
//                flag = inRange(plane, plane1);
//                边缘和在中心相撞
//                flag = inRange(plane, plane1.getX() + plane1.getBitmap().getWidth() / 2f, plane1.getY() - plane1.getBitmap().getHeight() / 2f);
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
               canvas.drawBitmap(plane.getBitmap(), plane.getX() + my_plane.getBitmap().getWidth() / 2f - 70, plane.getY() + my_plane.getBitmap().getHeight(), paint);
           }
           for (Plane plane : blastList) {
               canvas.drawBitmap(plane.getBitmap(), plane.getX(), plane.getY(), paint);
           }
           for (Plane plane : enemyList) {
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
           this.setText("已击毁敌机数量：" + index);
           canvas.drawBitmap(my_plane.getBitmap(), my_plane.getX(), my_plane.getY(), paint);

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
        return new Plane(dm.widthPixels / 2f, dm.heightPixels - 300, BitmapFactory.decodeResource(getResources(), R.drawable.my_plan));
    }

    public void startThread() {
        new Thread(new refresh()).start();
        new Thread(new MyBullet()).start();
        new Thread(new EnemyPlane()).start();
        new Thread(new Blast()).start();
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

    class End implements Runnable{

        @Override
        public void run() {
            try {
                Thread.sleep(300 * multiple);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
                Plane plane = new Plane(my_plane.getX() + my_plane.getBitmap().getWidth() / 2f - bulletBitmap.getWidth() / 2f, my_plane.getY() - my_plane.getBitmap().getHeight(), bulletBitmap, 5);
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
                enemyList.add(new Plane(new Random().nextInt(dm.widthPixels - bitmap.getWidth()), -bitmap.getHeight(), bitmap, live));
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

//    class EnemyBullet implements Runnable {
//
//        @Override
//        public void run() {
//            while (threadRunState) {
//                sleep(30L * multiple);
//                int i1 = new Random().nextInt(880) + 100;
//                Plane plane = new Plane(i1, 2000, BitmapFactory.decodeResource(getResources(), R.drawable.bullet), 5);
//                bulletList.add(plane);
//                for (int i = 0; i < bulletList.size(); i++) {
//                    Plane plane1 = bulletList.get(i);
//                    plane1.setY(plane1.getY() + 100);
//                    if (plane1.getY() >= 2000) {
//                        try {
//                            bulletList.remove(i);
//                        }catch (Exception e){
//                            Log.e(TAG, e.toString());
//                        }
//                    }
//                }
//            }
//        }
//    }

    public void revive() {
        threadRunState = true;
    }

    public void setThis(MainActivity mainActivity) {
        if (mainActivity != null){
            this.mainActivity = mainActivity;
        }
    }

    public static boolean inRange(float let_top_x, float let_top_y, float right_top_x, float right_top_y,
                                  float let_bottom_x, float let_bottom_y, float right_bottom_x, float right_bottom_y,
                                  float mixX, float maxX, float mixY, float maxY) {
        if (let_top_x >= mixX && let_top_x <= maxX && let_top_y >= mixY && let_top_y <= maxY)
            return true;
        if (right_top_x >= mixX && right_top_x <= maxX && right_top_y >= mixY && right_top_y <= maxY)
            return true;
        if (let_bottom_x >= mixX && let_bottom_x <= maxX && let_bottom_y >= mixY && let_bottom_y <= maxY)
            return true;
        return right_bottom_x >= mixX && right_bottom_x <= maxX && right_bottom_y >= mixY && right_bottom_y <= maxY;
    }


    public static boolean inRange(Plane plane, Plane plane1) {
        return inRange(plane.getX(), plane.getY(), plane.getX() + plane.getBitmap().getWidth(), plane.getY(), plane.getX() + plane.getBitmap().getHeight(), plane.getY() + plane.getBitmap().getHeight(),
                plane.getX() + plane.getBitmap().getWidth(), plane.getY() + plane.getBitmap().getHeight(), plane1.getX(), plane1.getX() + plane1.getBitmap().getWidth(), plane1.getY(), plane1.getY() + plane1.getBitmap().getHeight());
    }

//    public void test(){
//        //                774   2073   752  2073
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plan1);
//        boolean flag = inRange(new Plane(300, 1000, BitmapFactory.decodeResource(getResources(), R.drawable.bullet)), 340, 340 + bitmap.getWidth(), 1000);
//        int x= 0;
//    }

    public static boolean inRange(Plane plane, float minX, float maxX, float y) {
        if (y >= plane.getY() && y <= plane.getY() + plane.getBitmap().getHeight()){
            if (plane.getX() >= minX && plane.getX() <= maxX) return true;
            return plane.getX() + plane.getBitmap().getWidth() >= minX && plane.getX() + plane.getBitmap().getWidth() <= maxX;
        }else {
            return false;
        }
    }

    public static boolean inRange(Plane plane, float x, float y) {
        return (x >= plane.getX() && x<= plane.getX() + plane.getBitmap().getWidth() && y >= plane.getY() && y <= plane.getY() + plane.getBitmap().getHeight());
      }

}
