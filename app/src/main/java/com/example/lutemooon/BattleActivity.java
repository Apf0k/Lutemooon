package com.example.lutemooon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lutemooon.model.Lutemon;
import com.example.lutemooon.storage.Storage;
import java.util.List;

public class BattleActivity extends AppCompatActivity {
    private Lutemon lutemon1;
    private Lutemon lutemon2;
    
    private ImageView ivLutemon1;
    private TextView tvLutemon1Name;
    private TextView tvLutemon1Color;
    private ProgressBar pbLutemon1Health;
    private TextView tvLutemon1Health;
    
    private ImageView ivLutemon2;
    private TextView tvLutemon2Name;
    private TextView tvLutemon2Color;
    private ProgressBar pbLutemon2Health;
    private TextView tvLutemon2Health;
    
    private TextView tvBattleLog;
    private Button btnAttack;
    private Button btnStrike;
    private Button btnEndBattle;
    private ImageView ivAttackIcon;
    private Storage storage;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        storage = Storage.getInstance();
        List<Lutemon> battleLutemons = storage.getBattleLutemons();
        
        if (battleLutemons.size() != 2) {
            Toast.makeText(this, "You need two Lutemons to enter the arena.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        lutemon1 = battleLutemons.get(0);
        lutemon2 = battleLutemons.get(1);
        
        initializeViews();
        updateLutemonInfo();
        setupButtonListeners();
    }

    private void initializeViews() {
        // Lutemon 1 UI
        ivLutemon1 = findViewById(R.id.ivLutemon1);
        tvLutemon1Name = findViewById(R.id.tvLutemon1Name);
        tvLutemon1Color = findViewById(R.id.tvLutemon1Color);
        pbLutemon1Health = findViewById(R.id.pbLutemon1Health);
        tvLutemon1Health = findViewById(R.id.tvLutemon1Health);
        
        // Lutemon 2 UI
        ivLutemon2 = findViewById(R.id.ivLutemon2);
        tvLutemon2Name = findViewById(R.id.tvLutemon2Name);
        tvLutemon2Color = findViewById(R.id.tvLutemon2Color);
        pbLutemon2Health = findViewById(R.id.pbLutemon2Health);
        tvLutemon2Health = findViewById(R.id.tvLutemon2Health);
        
        // Log
        tvBattleLog = findViewById(R.id.tvBattleLog);
        btnAttack = findViewById(R.id.btnAttack);
        btnStrike = findViewById(R.id.btnStrike);
        btnEndBattle = findViewById(R.id.btnEndBattle);
        ivAttackIcon = findViewById(R.id.ivAttackIcon);
        ivAttackIcon.setVisibility(View.INVISIBLE);
    }

    private void updateLutemonInfo() {
        // Lutemon 1
        tvLutemon1Name.setText(lutemon1.getName());
        tvLutemon1Color.setText(lutemon1.getColor());
        pbLutemon1Health.setMax(lutemon1.getMaxHealth());
        pbLutemon1Health.setProgress(lutemon1.getCurrentHealth());
        tvLutemon1Health.setText(lutemon1.getCurrentHealth() + "/" + lutemon1.getMaxHealth());

        switch (lutemon1.getColor()) {
            case "White":
                ivLutemon1.setImageResource(R.drawable.lutemon_white);
                break;
            case "Green":
                ivLutemon1.setImageResource(R.drawable.lutemon_green);
                break;
            case "Pink":
                ivLutemon1.setImageResource(R.drawable.lutemon_pink);
                break;
            case "Orange":
                ivLutemon1.setImageResource(R.drawable.lutemon_orange);
                break;
            case "Black":
                ivLutemon1.setImageResource(R.drawable.lutemon_black);
                break;
        }
        
        // Lutemon 2
        tvLutemon2Name.setText(lutemon2.getName());
        tvLutemon2Color.setText(lutemon2.getColor());
        pbLutemon2Health.setMax(lutemon2.getMaxHealth());
        pbLutemon2Health.setProgress(lutemon2.getCurrentHealth());
        tvLutemon2Health.setText(lutemon2.getCurrentHealth() + "/" + lutemon2.getMaxHealth());
        
        switch (lutemon2.getColor()) {
            case "White":
                ivLutemon2.setImageResource(R.drawable.lutemon_white);
                break;
            case "Green":
                ivLutemon2.setImageResource(R.drawable.lutemon_green);
                break;
            case "Pink":
                ivLutemon2.setImageResource(R.drawable.lutemon_pink);
                break;
            case "Orange":
                ivLutemon2.setImageResource(R.drawable.lutemon_orange);
                break;
            case "Black":
                ivLutemon2.setImageResource(R.drawable.lutemon_black);
                break;
        }
    }

    private void setupButtonListeners() {
        btnAttack.setOnClickListener(v -> performNormalAttack());
        btnStrike.setOnClickListener(v -> performHeavyAttack());
        btnEndBattle.setOnClickListener(v -> {
            addBattleLog("-!Battle over!-");
            addBattleLog("-The battle was called off-");
            storage.clearBattle();
            finish();
        });
    }

    private void playAttackAnimation(boolean isPlayer1Attacking, boolean isHeavyAttack, Runnable onAnimationEnd) {
        // Get the start and end positions.
        int[] startLocation = new int[2];
        int[] endLocation = new int[2];
        
        if (isPlayer1Attacking) {
            ivLutemon1.getLocationInWindow(startLocation);
            ivLutemon2.getLocationInWindow(endLocation);
        } else {
            ivLutemon2.getLocationInWindow(startLocation);
            ivLutemon1.getLocationInWindow(endLocation);
        }

        // Set the initial position of the attack icon.
        ivAttackIcon.setX(startLocation[0] + ivLutemon1.getWidth() / 2f - ivAttackIcon.getWidth() / 2f);
        ivAttackIcon.setY(startLocation[1] + ivLutemon1.getHeight() / 2f - ivAttackIcon.getHeight() / 2f);
        ivAttackIcon.setVisibility(View.VISIBLE);
        
        // anim of Strike icon
        if (isHeavyAttack) {
            ObjectAnimator rotation = ObjectAnimator.ofFloat(ivAttackIcon, "rotation", 0f, 360f);
            rotation.setDuration(600);
            rotation.start();
        }

        // anim X
        ObjectAnimator animX = ObjectAnimator.ofFloat(ivAttackIcon, "x", 
            startLocation[0] + ivLutemon1.getWidth() / 2f - ivAttackIcon.getWidth() / 2f,
            endLocation[0] + ivLutemon2.getWidth() / 2f - ivAttackIcon.getWidth() / 2f);
        
        // anim Y
        ObjectAnimator animY = ObjectAnimator.ofFloat(ivAttackIcon, "y",
            startLocation[1] + ivLutemon1.getHeight() / 2f - ivAttackIcon.getHeight() / 2f,
            endLocation[1] + ivLutemon2.getHeight() / 2f - ivAttackIcon.getHeight() / 2f);

        // anim properties
        animX.setDuration(600);
        animY.setDuration(600);
        animX.setInterpolator(new AccelerateDecelerateInterpolator());
        animY.setInterpolator(new AccelerateDecelerateInterpolator());

        // anim end listener
        animX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ivAttackIcon.setVisibility(View.INVISIBLE);
                if (onAnimationEnd != null) {
                    onAnimationEnd.run();
                }
            }
        });

        // anim start
        animX.start();
        animY.start();
    }

    private void performNormalAttack() {
        if (isBattleOver()) {
            disableAttackButtons();
            return;
        }

        btnAttack.setEnabled(false);
        btnStrike.setEnabled(false);
        
        playAttackAnimation(true, false, () -> {
            if (isBattleOver()) {
                disableAttackButtons();
                return;
            }

            int damage = lutemon1.calculateAttackDamage(false);
            String result = lutemon1.getAttackResult(damage, lutemon2.getDefense());
            int actualDamage = lutemon2.calculateDamageTaken(damage, lutemon2.getDefense());
            
            lutemon2.takeDamage(actualDamage);
            addBattleLog(lutemon1.getName() + " used a normal attack!");
            addBattleLog(result);
            addBattleLog("It dealt " + actualDamage + " damage!");
            updateLutemonInfo();
            
            if (isBattleOver()) {
                disableAttackButtons();
                return;
            }
            
            handler.postDelayed(() -> {
                if (!isBattleOver()) {
                    performOpponentAttack();
                }
            }, 500);
        });
    }

    private void performHeavyAttack() {
        if (isBattleOver()) {
            disableAttackButtons();
            return;
        }

        btnAttack.setEnabled(false);
        btnStrike.setEnabled(false);
        
        playAttackAnimation(true, true, () -> {
            if (isBattleOver()) {
                disableAttackButtons();
                return;
            }

            int damage = lutemon1.calculateAttackDamage(true);
            String result = lutemon1.getAttackResult(damage, lutemon2.getDefense());
            int actualDamage = lutemon2.calculateDamageTaken(damage, lutemon2.getDefense());
            
            lutemon1.takeDamage(5); // Strike causes self-damage
            lutemon2.takeDamage(actualDamage);
            addBattleLog(lutemon1.getName() + " used Heavy Strike!");
            addBattleLog(result);
            addBattleLog("It dealt " + actualDamage + " damage!");
            addBattleLog(lutemon1.getName() + " lost 5 HP as the cost!");
            updateLutemonInfo();
            
            if (isBattleOver()) {
                disableAttackButtons();
                return;
            }
            
            handler.postDelayed(() -> {
                if (!isBattleOver()) {
                    performOpponentAttack();
                }
            }, 500);
        });
    }

    private void performOpponentAttack() {
        if (isBattleOver()) {
            disableAttackButtons();
            return;
        }

        boolean useStrike = Math.random() < 0.5;
        
        playAttackAnimation(false, useStrike, () -> {
            if (isBattleOver()) {
                disableAttackButtons();
                return;
            }

            int damage = lutemon2.calculateAttackDamage(useStrike);
            String result = lutemon2.getAttackResult(damage, lutemon1.getDefense());
            int actualDamage = lutemon1.calculateDamageTaken(damage, lutemon1.getDefense());
            
            if (useStrike) {
                lutemon2.takeDamage(5);
            }
            lutemon1.takeDamage(actualDamage);
            
            addBattleLog(lutemon2.getName() + (useStrike ? " used Heavy Strike!" : " used a normal attack!"));
            addBattleLog(result);
            addBattleLog("It dealt " + actualDamage + " damage!");
            if (useStrike) {
                addBattleLog(lutemon2.getName() + " lost 5 HP as the cost!");
            }
            updateLutemonInfo();
            
            if (isBattleOver()) {
                disableAttackButtons();
            } else {
                btnAttack.setEnabled(true);
                btnStrike.setEnabled(true);
            }
        });
    }

    private boolean isBattleOver() {
        boolean isOver = false;
        
        if (!lutemon1.isAlive()) {
            addBattleLog("Battle over! " + lutemon2.getName() + " wins!");
            lutemon2.addExperience(1);
            storage.clearBattle();
            isOver = true;
        } else if (!lutemon2.isAlive()) {
            addBattleLog("Battle over! " + lutemon1.getName() + " wins!");
            lutemon1.addExperience(1);
            storage.clearBattle();
            isOver = true;
        }
        
        if (isOver) {
            disableAttackButtons();
            handler.removeCallbacksAndMessages(null); // Remove any pending attacks
        }
        
        return isOver;
    }

    private void disableAttackButtons() {
        btnAttack.setEnabled(false);
        btnStrike.setEnabled(false);
    }

    private void addBattleLog(String message) {
        String currentLog = tvBattleLog.getText().toString();
        tvBattleLog.setText(currentLog + "\n" + message);
    }
} 