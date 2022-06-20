package ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.test.MainActivity;
import com.example.test.MySQLite;
import com.example.test.R;

public class RegisterActivity extends AppCompatActivity {

    private final static String TAG = "RegisterActivity";
    private final String defaultName = "未登录用户";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "======================onCreate======================");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "======================onStart======================");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "======================onStop======================");
        super.onStop();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "======================onResume======================");
        super.onResume();

    }

    @Override
    protected void onRestart() {
        Log.i(TAG, "======================onRestart======================");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "======================onPause======================");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "======================onDestroy======================");
        super.onDestroy();
    }

    @SuppressLint("Recycle")
    public void onClick(View view) {
        EditText nameText = findViewById(R.id.usernameText);
        String username = nameText.getText().toString();
        if (username.length() >= 9){
            Toast.makeText(this, "用户名过长，请重新输入", Toast.LENGTH_SHORT).show();
        }else {
            if (username.equals("") || username.equals("请输入你的名字")){
                MySQLite mySQLite = new MySQLite(this, "user");
                SQLiteDatabase readableDatabase = mySQLite.getReadableDatabase();
                Cursor cursor = readableDatabase.rawQuery("select count(*) from user where username like '"+defaultName+"%'", null);
                int count = 0;
                if (cursor.moveToNext()){
                    count = cursor.getInt(0) + 1;
                }
                username = defaultName + (count + 1);
            }
            Bundle bundle = new Bundle();
            Intent intent = new Intent(this, MainActivity.class);
            bundle.putString("username", username);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    public void closeOnClick(View view) {

    }
}