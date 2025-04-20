package com.example.lutemooon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnHome = findViewById(R.id.btnHome);
        Button btnCreate = findViewById(R.id.btnCreate);
        Button btnTraining = findViewById(R.id.btnTraining);
        Button btnBattle = findViewById(R.id.btnBattle);

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        btnCreate.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateActivity.class);
            startActivity(intent);
        });

        btnTraining.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TrainingActivity.class);
            startActivity(intent);
        });

        btnBattle.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BattleActivity.class);
            startActivity(intent);
        });
    }
}