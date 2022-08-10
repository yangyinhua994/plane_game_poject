package com.example.plane.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.plane.base.BaseActivity;
import com.example.test.R;

public class ContinueActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue);
        TextView continueTextView = findViewById(R.id.continueTextView);
        LinearLayout.LayoutParams continueTextViewParamsParent = new LinearLayout.LayoutParams((this.width - leftAddRight), (this.height / standardHeightMulti));
        continueTextViewParamsParent.setMargins(this.width / this.standardWidthMulti, ZERO, this.width / this.standardWidthMulti, ZERO);
        continueTextView.setLayoutParams(continueTextViewParamsParent);
        Button ct = findViewById(R.id.Continue);
        Button restart = findViewById(R.id.restart);
        Button quit = findViewById(R.id.quit);
        ct.setOnClickListener(v -> {
            MainActivity.setMStatus(initData);
            finish();
        });
        restart.setOnClickListener(v -> {
            MainActivity.setMStatus(this.errorExitStartGame);
            finish();
        });
        quit.setOnClickListener(v -> {
            MainActivity.setMStatus(quitGame);
            finish();
        });

    }
}