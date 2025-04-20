package com.example.lutemooon;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lutemooon.adapter.LutemonAdapter;
import com.example.lutemooon.model.Lutemon;
import com.example.lutemooon.storage.Storage;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView rvLutemons;
    private LutemonAdapter adapter;
    private Lutemon selectedLutemon = null;
    private Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        storage = Storage.getInstance();
        setupRecyclerView();
        setupButtons();
    }

    private void setupRecyclerView() {
        rvLutemons = findViewById(R.id.rvLutemons);
        rvLutemons.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new LutemonAdapter(storage.getLutemons(), (position, isSelected) -> {
            Lutemon lutemon = storage.getLutemons().get(position);
            if (isSelected) {
                selectedLutemon = lutemon;
            } else {
                selectedLutemon = null;
            }
        });
        
        rvLutemons.setAdapter(adapter);
    }

    private void setupButtons() {
        Button btnMoveToTraining = findViewById(R.id.btnMoveToTraining);
        Button btnMoveToBattle = findViewById(R.id.btnMoveToBattle);

        btnMoveToTraining.setOnClickListener(v -> {
            if (selectedLutemon == null) {
                Toast.makeText(this, "Please select a Lutemon", Toast.LENGTH_SHORT).show();
                return;
            }

            if (storage.moveToTraining(selectedLutemon)) {
                selectedLutemon = null;
                adapter.clearSelection();
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Moved to Training Ground", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Training Ground already has a Lutemon", Toast.LENGTH_SHORT).show();
            }
        });

        btnMoveToBattle.setOnClickListener(v -> {
            if (selectedLutemon == null) {
                Toast.makeText(this, "Please select a Lutemon", Toast.LENGTH_SHORT).show();
                return;
            }

            if (storage.moveToBattle(selectedLutemon)) {
                selectedLutemon = null;
                adapter.clearSelection();
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Moved to Battle Arena", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Battle Arena is full", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedLutemon = null;
        adapter.clearSelection();
        adapter.notifyDataSetChanged();
    }
} 