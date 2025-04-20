package com.example.lutemooon;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lutemooon.model.Lutemon;
import com.example.lutemooon.storage.Storage;

public class TrainingActivity extends AppCompatActivity {
    private TextView tvName;
    private TextView tvColor;
    private TextView tvStats;
    private TextView tvExperience;
    private ImageView ivLutemon;
    private Button btnTrain;
    private Button btnReturnHome;
    
    private Storage storage;
    private Lutemon trainingLutemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        storage = Storage.getInstance();
        trainingLutemon = storage.getTrainingLutemon();
        
        if (trainingLutemon == null) {
            Toast.makeText(this, "The Training Ground is empty", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupViews();
        updateLutemonInfo();
        setupButtons();
    }

    private void setupViews() {
        tvName = findViewById(R.id.tvName);
        tvColor = findViewById(R.id.tvColor);
        tvStats = findViewById(R.id.tvStats);
        tvExperience = findViewById(R.id.tvExperience);
        ivLutemon = findViewById(R.id.ivLutemon);
        btnTrain = findViewById(R.id.btnTrain);
        btnReturnHome = findViewById(R.id.btnReturnHome);
    }

    private void updateLutemonInfo() {
        if (trainingLutemon == null) {
            return;
        }

        tvName.setText(trainingLutemon.getName());
        tvColor.setText("Color: " + trainingLutemon.getColor());
        tvStats.setText(String.format("ATK: %d DEF: %d HP: %d/%d",
                trainingLutemon.getAttack(), trainingLutemon.getDefense(),
                trainingLutemon.getCurrentHealth(), trainingLutemon.getMaxHealth()));
        tvExperience.setText("EXP: " + trainingLutemon.getExperience());
        
        switch (trainingLutemon.getColor()) {
            case "White":
                ivLutemon.setImageResource(R.drawable.lutemon_white);
                break;
            case "Green":
                ivLutemon.setImageResource(R.drawable.lutemon_green);
                break;
            case "Pink":
                ivLutemon.setImageResource(R.drawable.lutemon_pink);
                break;
            case "Orange":
                ivLutemon.setImageResource(R.drawable.lutemon_orange);
                break;
            case "Black":
                ivLutemon.setImageResource(R.drawable.lutemon_black);
                break;
        }
    }

    private void setupButtons() {
        btnTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainingLutemon.addExperience(1);
                updateLutemonInfo();
                Toast.makeText(TrainingActivity.this, "Training complete! Gained 1 EXP", Toast.LENGTH_SHORT).show();
            }
        });

        btnReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storage.removeFromTraining();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (trainingLutemon != null) {
            updateLutemonInfo();
        }
    }
} 