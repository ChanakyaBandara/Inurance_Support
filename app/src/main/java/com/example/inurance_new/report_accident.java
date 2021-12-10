package com.example.inurance_new;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class report_accident extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_accident);
    }

    public void goTo_camera(View view) {
        Intent intent = new Intent(report_accident.this, camera_act.class);
        startActivity(intent);
    }

    public void goTo_map(View view) {
        Intent intent = new Intent(report_accident.this, Map.class);
        startActivity(intent);
    }
}
