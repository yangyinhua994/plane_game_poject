package com.example.plane.view;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.plane.activity.MainActivity;
import com.example.plane.activity.StartActivity;
import com.example.plane.dto.Plane;
import com.example.plane.sqlite.MySQLite;
import com.example.plane.utils.IntentKey;
import com.example.test.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("all")
public class StartView extends TextView {
    //    倍率
    private final int refreshRate = getResources().getInteger(R.integer.refreshRate);
    //    敌方飞机移动距离
    private final int enemyPlaneMoveDistance = getResources().getInteger(R.integer.enemyPlaneMoveDistance);
    //    移动敌方飞机的刷新速率
    private int enemyPlaneMoveGenerateSpeed = getResources().getInteger(R.integer.enemyPlaneMoveGenerateSpeed) * refreshRate;
    //    子弹的刷新速率
    private final int bulletGenerateSpeed = getResources().getInteger(R.integer.bulletGenerateSpeed) * refreshRate;
    //    敌方飞机的刷新速率
    private final int enemyPlaneGenerateSpeed = getResources().getInteger(R.integer.enemyPlaneGenerateSpeed) * refreshRate;
    //    界面的刷新速率
    private final int magnification = getResources().getInteger(R.integer.magnification);
    //    总分
    private int index = getResources().getInteger(R.integer.index);
    //    计时器的时间
    private Integer millisecond = getResources().getInteger(R.integer.millisecond);
    //    无敌时间
    private final int invincibleTime = getResources().getInteger(R.integer.invincibleTime);
    //    判断爆炸的刷新速率
    private final int blastGenerateSpeed = getResources().getInteger(R.integer.blastGenerateSpeed);
    private final int textSize = 22;
    private final int paddingLeft = 10;
    private final int paddingTop = 10;
    private final int paddingRight = 0;
    private final int paddingBottm = 0;
    private final int phoneMixheightPixels = 900;
    private final int phoneMixheightPixelsSpeed = 2;
    private final int planInitLive = 3;
    private final int enemyPlaneYellow = 1;
    private final int enemyPlaneYellowLive = 4;
    private final int enemyPlaneGreen = 2;
    private final int enemyPlaneGreenLive = 5;
    private final int enemyPlaneRed = 3;
    private final int enemyPlaneRedLive = 6;
    private final int enemyPlaneBlue = 4;
    private final int enemyPlaneBlueLive = 7;
    private final int moveSpeed = 2;
    private final int enemyPlaneMixRadom = 1;
    private final int enemyPlaneMaxRadom = 4;
    private float myPlaneInitX;
    private float myPlaneInitY;
    private int myPlaneInitBitmapType = 0;
    private final int timerTime = 123;


    public void setInvincible(boolean invincible) {
        isInvincible = invincible;
    }

    //    子弹的高度
    private int bulletHeight;
    //    无敌状态
    private boolean isInvincible = false;
    //    线程运行状态
    private boolean threadRunState = true;

    private final Paint paint = new Paint();
    private CopyOnWriteArrayList<Plane> bulletList, enemyList, blastList;
    private final DisplayMetrics dm = getResources().getDisplayMetrics();
    private Plane my_plane;
    private Bitmap bulletBitmap;
    private MediaPlayer mediaPlayerBlast, mediaPlayerBulletFiring, mediaPlayerBulletImpact;
    private final Context context;
    private MainActivity mainActivity;
    private final String TAG = "Start";
    private String username;
    private int width, height;

    public void setThreadRunState(boolean threadRunState) {
        this.threadRunState = threadRunState;
    }

    public boolean isThreadRunState() {
        return threadRunState;
    }
    public StartView(Context context) {
        super(context);
        this.context = context;
    }

    public StartView(Context context, MainActivity mainActivity) {
        super(context);
        this.context = context;
        this.mainActivity = mainActivity;
        bulletList = new CopyOnWriteArrayList<>();
        enemyList = new CopyOnWriteArrayList<>();
        blastList = new CopyOnWriteArrayList<>();
        initMyPlane();
        init();
    }

    public StartView(Context context, MainActivity mainActivity, boolean b) {
        super(context);
        this.context = context;
        this.mainActivity = mainActivity;
        if (b) {
            Cursor cursor = getCursor(context, "list");
            if (cursor.moveToNext()) {
                Gson gson = new Gson();
                Type type = new TypeToken<CopyOnWriteArrayList<Plane>>() {
                }.getType();
                bulletList = gson.fromJson(cursor.getString(cursor.getColumnIndex("bulletJson")), type);
                enemyList = gson.fromJson(cursor.getString(cursor.getColumnIndex("enemyJson")), type);
                blastList = gson.fromJson(cursor.getString(cursor.getColumnIndex("blastJson")), type);
                my_plane = gson.fromJson(cursor.getString(cursor.getColumnIndex("myJson")), Plane.class);
                username = cursor.getString(cursor.getColumnIndex("username"));
                index = Integer.parseInt(cursor.getString(cursor.getColumnIndex("fraction")));
                millisecond = Integer.valueOf(cursor.getString(cursor.getColumnIndex("millisecond")));
                deleteAllList();
                init();
            }
            cursor.close();
        }
    }

    public void restartInit() {
        initMyPlane();
        bulletList = new CopyOnWriteArrayList<>();
        enemyList = new CopyOnWriteArrayList<>();
        blastList = new CopyOnWriteArrayList<>();
        index = getResources().getInteger(R.integer.index);
        millisecond = getResources().getInteger(R.integer.index);
        index = getResources().getInteger(R.integer.index);
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
        setBackgroundResource(R.drawable.black);
        setTextColor(Color.parseColor("#ffffff"));
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottm);
        setTextSize(textSize);
        setOnTouchListener(this::onTouch);
        startThread();
        bulletHeight = bulletBitmap.getHeight();
        if (dm.heightPixels <= phoneMixheightPixels) {
            enemyPlaneMoveGenerateSpeed = enemyPlaneMoveGenerateSpeed * phoneMixheightPixelsSpeed;
        }
        WindowManager windowManager = mainActivity.getWindow().getWindowManager();
        Point point = new Point();
        windowManager.getDefaultDisplay().getRealSize(point);
        //屏幕实际宽度（像素个数）
        width = point.x;
        //屏幕实际高度（像素个数）
        height = point.y;
        myPlaneInitX = dm.widthPixels / 2f;
        myPlaneInitY = dm.heightPixels - 300;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (threadRunState) {
            for (int i = 0; i < bulletList.size(); i++) {
                Plane plane = bulletList.get(i);
                mediaPlayerBulletFiring.start();
                for (int i1 = 0; i1 < enemyList.size(); i1++) {
                    Plane plane1 = enemyList.get(i1);
                    boolean flag;
                    flag = inRange(plane, plane1.getX(),
                            plane1.getX() + plane1.getBitmap().getWidth(),
                            plane1.getY() - plane1.getBitmap().getHeight() / 2f);
                    if (flag) {
                        mediaPlayerBulletImpact.start();
                        try {
                            bulletList.remove(i);
                            enemyList.remove(i1);
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                        if (plane1.getLive() == 1) {
                            plane1.setBitmap(BitmapFactory.decodeResource(getResources(),
                                    R.drawable.blast4));
                            mediaPlayerBlast.start();
                            blastList.add(plane1);
                            index++;
                        } else {
                            plane1.setLive(plane1.getLive() - 1);
                            enemyList.add(i1, plane1);
                        }
                    }
                }
                canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bullet),
                        plane.getX(),
                        plane.getY() + my_plane.getBitmap().getHeight(), paint);
            }
            for (Plane plane : blastList) {
                canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blast4), plane.getX(), plane.getY(), paint);
            }
            for (Plane plane : enemyList) {
                if (plane.getBitmapType() == enemyPlaneYellow) {
                    plane.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.plan1));
                } else if (plane.getBitmapType() == enemyPlaneGreen) {
                    plane.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.plan2));
                } else if (plane.getBitmapType() == enemyPlaneRed) {
                    plane.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.plan3));
                } else {
                    plane.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.plan4));
                }
                canvas.drawBitmap(plane.getBitmap(), plane.getX(), plane.getY(), paint);
                if (!isInvincible) {
                    if (inRange(my_plane, plane.getX(), plane.getX() + plane.getBitmap().getWidth(),
                            plane.getY() + plane.getBitmap().getHeight())) {
                        threadRunState = false;
                        Intent intent = new Intent(mainActivity, StartActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(IntentKey.getInstance().username, username);
                        bundle.putInt(IntentKey.getInstance().index, index);
                        intent.putExtras(bundle);
                        mainActivity.startActivity(intent);
                        my_plane.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blast4));
                        blastList.add(my_plane);
                        sleep(20L * refreshRate);
                    }
                }
            }
            String timer = DateUtils.formatElapsedTime(millisecond / 1000)
                    + ":" + (millisecond % 100);

            this.setText(mainActivity.getString(R.string.destroy_enemy_plane) + "：" + index +
                    "\n"
                    + timer);

            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.my_plan),
                    my_plane.getX(), my_plane.getY(), paint);
        }
    }

    public static void sleep(long millis) {
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
        int max_x = width - min_x;
        int min_y = my_plane.getBitmap().getHeight() / 2;
        int max_y = height - min_y;
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
        if (username == null || username == ""){
            username = mainActivity.getString(R.string.unknown_user);
        }
        String sql = "insert into user (username, number, create_time) values ( " + "'"
                + username + "'," + index + "," + "'" + new Date(System.currentTimeMillis()) + "')";
        writableDatabase.execSQL(sql);
    }

    public void initMyPlane(){
        my_plane = new Plane(myPlaneInitX,
                myPlaneInitY,
                BitmapFactory.decodeResource(getResources(),
                        R.drawable.my_plan),
                myPlaneInitBitmapType);
    }

    public void startThread() {
        new Thread(new refresh()).start();
        new Thread(new MyBullet()).start();
        new Thread(new EnemyPlane()).start();
        new Thread(new Blast()).start();
        runTimer();
        threadRunState = true;
        moveEnemyPlane();
    }

    public void setUsername(String username) {
        if (this.username == null){
            this.username = username;
        }
    }

    public void invincibleTime(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(invincibleTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isInvincible = false;
            }
        }.start();
    }

    public void runTimer() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                while (true){
                    StartView.sleep(timerTime);
                    if (threadRunState){
                        millisecond += timerTime;
                    }
                }
            }
        }.start();
     }

    public void moveEnemyPlane(){
        new Thread(() -> {
            while (true){
                if (threadRunState){
                    for (int i1 = 0; i1 < enemyList.size(); i1++) {
                        Plane plane = enemyList.get(i1);
                        if (plane.getY() >= height) {
                            try {
                                enemyList.remove(i1);
                            }catch (Exception e){
                                Log.e(TAG, e.toString());
                            }
                        } else {
                            plane.setY(plane.getY() + enemyPlaneMoveDistance + plane.getMoveSpeed());
                        }
                    }
                    for (int i1 = 0; i1 < blastList.size(); i1++) {
                        Plane plane = blastList.get(i1);
                        plane.setY(plane.getY());
                    }
                    sleep(enemyPlaneMoveGenerateSpeed);
                }
            }
        }).start();
    }

    class Blast implements Runnable {
        @Override
        public void run() {
            while (true) {
                if (threadRunState){
                    sleep(blastGenerateSpeed * refreshRate);
                    blastList = new CopyOnWriteArrayList<>();
                }
            }
        }
    }

    class refresh implements Runnable {

        @Override
        public void run() {
            while (true) {
                if (threadRunState){
                    sleep(magnification);
                    postInvalidate();
                }
            }
        }
    }

    class MyBullet implements Runnable {

        @Override
        public void run() {
            while (true) {
                if (threadRunState){
                    sleep(bulletGenerateSpeed * refreshRate);
                    float bulletX;
                    int widthPixels = dm.widthPixels;
//                    if (widthPixels >= 500 && widthPixels  < 550){
//                        bulletX = my_plane.getX() + (my_plane.getBitmap().getWidth() / 2f) - (bulletBitmap.getWidth() / 2f) + 35;
//                    } else if (widthPixels >= 700 && widthPixels < 750){
//                        bulletX = my_plane.getX() + (my_plane.getBitmap().getWidth() / 2f) - (bulletBitmap.getWidth() / 2f) +25;
//                    }
//                    else if (width >= 450 && width < 500){
//                        bulletX = my_plane.getX() + (my_plane.getBitmap().getWidth() / 2f) - (bulletBitmap.getWidth() / 2f) + 35;
//                    }else {
//                        bulletX = my_plane.getX() + (my_plane.getBitmap().getWidth() / 2f) - (bulletBitmap.getWidth() / 2f) - 10;
//                    }
                    bulletList.add(new Plane(my_plane.getX() + (my_plane.getBitmap().getWidth() / 2 - bulletBitmap.getWidth()) / 2, my_plane.getY() - my_plane.getBitmap().getHeight() - (new Random().nextInt(30) + 10), bulletBitmap, 5));
                   for (int i = 0; i < bulletList.size(); i++) {
                        Plane plane1 = bulletList.get(i);
//                        小于让子弹有飞出屏幕的效果
                        if (plane1.getY() < bulletHeight) {
                            try {
                                bulletList.remove(i);
                            } catch (Exception ignored) {
                            }
                        } else {
                            plane1.setY(plane1.getY() - bulletBitmap.getHeight());
                        }
                    }
                }

            }
        }
    }

    class EnemyPlane implements Runnable {

        @Override
        public void run() {
            while (true) {
                if (threadRunState) {
                    sleep(enemyPlaneGenerateSpeed * refreshRate);
                    int i = new Random().nextInt(enemyPlaneMaxRadom) + enemyPlaneMixRadom;
                    Bitmap bitmap;
                    int live = planInitLive;
                    if (i == enemyPlaneYellow) {
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plan1);
                    } else if (i == enemyPlaneGreen) {
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plan2);
                        live = enemyPlaneGreenLive;
                    } else if (i == enemyPlaneRed) {
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plan3);
                        live = enemyPlaneRedLive;
                    } else {
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plan4);
                        live = enemyPlaneBlueLive;
                    }
                    enemyList.add(new Plane(new Random().nextInt(width - bitmap.getWidth()),
                            -bitmap.getHeight(), bitmap, live, i, moveSpeed * i));
                }

            }
        }
    }

    public static Cursor getCursor(Context context, String databaseName) {
        MySQLite sqLite = new MySQLite(context, databaseName);
        SQLiteDatabase readableDatabase = sqLite.getReadableDatabase();
        return readableDatabase.rawQuery("select * from " + databaseName, null);

    }

    public static boolean checkState(Context context) {
        Cursor cursor = StartView.getCursor(context, "list");
        return cursor.moveToNext();
    }

    public void saveAllList() {
        if (threadRunState) {
            Gson gson = new Gson();
            String bulletJson = gson.toJson(bulletList, new TypeToken<List<Plane>>() {
            }.getType());
            String enemyJson = gson.toJson(enemyList, new TypeToken<List<Plane>>() {
            }.getType());
            String blastJson = gson.toJson(blastList, new TypeToken<List<Plane>>() {
            }.getType());
            String myJson = gson.toJson(my_plane);
            MySQLite sqLite = new MySQLite(mainActivity, "list");
            SQLiteDatabase writableDatabase = sqLite.getWritableDatabase();
            String sql = "insert into list (bulletJson, enemyJson, blastJson, myJson, username, " +
                    "millisecond, fraction) " +
                    "values ('" + bulletJson + "','" + enemyJson + "','" + blastJson + "'" +
                    ",'" + myJson + "','" + username + "','" + millisecond + "','" + index + "')";
            writableDatabase.execSQL(sql);
            writableDatabase.close();
            sqLite.close();
            Log.i(TAG, "数据保存成功");
        }
    }

    public void deleteAllList() {
        MySQLite sqLite = new MySQLite(mainActivity, "list");
        SQLiteDatabase writableDatabase = sqLite.getWritableDatabase();
        String sql = "delete from list";
        writableDatabase.execSQL(sql);
        writableDatabase.close();
        sqLite.close();
    }



    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public static boolean inRange(Plane plane, float minX, float maxX, float y) {
        if (y >= plane.getY() && y <= plane.getY() + plane.getBitmap().getHeight()) {
            if (plane.getX() >= minX && plane.getX() <= maxX) return true;
            return plane.getX() + plane.getBitmap().getWidth() >= minX && plane.getX()
                    + plane.getBitmap().getWidth() <= maxX;
        } else {
            return false;
        }
    }

//
//    private Handler handler = new Handler(){
//        @NonNull
//        @Override
//        public String getMessageName(@NonNull Message message) {
//            int flag = message.arg1;
//            switch (flag){
//                case 0x001:
//                    while (true) {
//                        if (threadRunState) {
//                            for (int i1 = 0; i1 < enemyList.size(); i1++) {
//                                Plane plane = enemyList.get(i1);
//                                if (plane.getY() >= dm.heightPixels) {
//                                    try {
//                                        enemyList.remove(i1);
//                                    } catch (Exception e) {
//                                        Log.e(TAG, e.toString());
//                                    }
//                                } else {
//                                    plane.setY(plane.getY() + enemyPlaneMoveDistance + plane.getMoveSpeed());
//                                }
//                            }
//                            for (int i1 = 0; i1 < blastList.size(); i1++) {
//                                Plane plane = blastList.get(i1);
//                                plane.setY(plane.getY());
//                            }
//                            sleep(enemyPlaneMoveGenerateSpeed);
//                        }
//                    }
//                default:
//                    break;
//            }
//            return super.getMessageName(message);
//        }
//    };

}
