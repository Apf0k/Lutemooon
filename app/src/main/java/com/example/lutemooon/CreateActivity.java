package com.example.lutemooon;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lutemooon.model.*;
import com.example.lutemooon.storage.Storage;
import java.util.Random;

public class CreateActivity extends AppCompatActivity {
    private TextView tvLutemonInfo;
    private EditText etName;
    private Button btnCreate;
    private ImageView ivLutemonPreview;
    
    private String selectedColor;
    private int attack;
    private int defense;
    private int maxHealth;
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        tvLutemonInfo = findViewById(R.id.tvLutemonInfo);
        etName = findViewById(R.id.etName);
        btnCreate = findViewById(R.id.btnCreate);
        ivLutemonPreview = findViewById(R.id.ivLutemonPreview);

        generateRandomLutemon();
        setupCreateButton();
    }

    private void generateRandomLutemon() {
        // Randomly select color
        String[] colors = {"白色", "绿色", "粉色", "橘色", "黑色"};
        selectedColor = colors[random.nextInt(colors.length)];

        // Generate random stats
        attack = random.nextInt(13); // 0-12
        defense = random.nextInt(9); // 0-8
        maxHealth = random.nextInt(11) + 15; // 15-25

        // Update UI
        String info = String.format("颜色: %s\n攻击力: %d\n防御力: %d\n生命值: %d",
                selectedColor, attack, defense, maxHealth);
        tvLutemonInfo.setText(info);

        // Update Lutemon image based on color
        int imageResource;
        switch (selectedColor) {
            case "白色":
                imageResource = R.drawable.lutemon_white;
                break;
            case "绿色":
                imageResource = R.drawable.lutemon_green;
                break;
            case "粉色":
                imageResource = R.drawable.lutemon_pink;
                break;
            case "橘色":
                imageResource = R.drawable.lutemon_orange;
                break;
            case "黑色":
                imageResource = R.drawable.lutemon_black;
                break;
            default:
                imageResource = R.mipmap.ic_launcher;
                break;
        }
        ivLutemonPreview.setImageResource(imageResource);
    }

    private void setupCreateButton() {
        btnCreate.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "请输入宠物名称", Toast.LENGTH_SHORT).show();
                return;
            }

            Lutemon newLutemon = createLutemon(name);
            Storage.getInstance().addLutemon(newLutemon);
            
            Toast.makeText(this, "创建成功！", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private Lutemon createLutemon(String name) {
        Lutemon lutemon;
        switch (selectedColor) {
            case "白色":
                lutemon = new WhiteLutemon(name);
                ((WhiteLutemon) lutemon).setAttributes(attack, defense, maxHealth);
                break;
            case "绿色":
                lutemon = new GreenLutemon(name);
                ((GreenLutemon) lutemon).setAttributes(attack, defense, maxHealth);
                break;
            case "粉色":
                lutemon = new PinkLutemon(name);
                ((PinkLutemon) lutemon).setAttributes(attack, defense, maxHealth);
                break;
            case "橘色":
                lutemon = new OrangeLutemon(name);
                ((OrangeLutemon) lutemon).setAttributes(attack, defense, maxHealth);
                break;
            case "黑色":
                lutemon = new BlackLutemon(name);
                ((BlackLutemon) lutemon).setAttributes(attack, defense, maxHealth);
                break;
            default:
                throw new IllegalStateException("未知的颜色: " + selectedColor);
        }
        return lutemon;
    }
} 