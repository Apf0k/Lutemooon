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
            Toast.makeText(this, "战斗场需要两个精灵", Toast.LENGTH_SHORT).show();
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
        // 第一个精灵UI组件
        ivLutemon1 = findViewById(R.id.ivLutemon1);
        tvLutemon1Name = findViewById(R.id.tvLutemon1Name);
        tvLutemon1Color = findViewById(R.id.tvLutemon1Color);
        pbLutemon1Health = findViewById(R.id.pbLutemon1Health);
        tvLutemon1Health = findViewById(R.id.tvLutemon1Health);
        
        // 第二个精灵UI组件
        ivLutemon2 = findViewById(R.id.ivLutemon2);
        tvLutemon2Name = findViewById(R.id.tvLutemon2Name);
        tvLutemon2Color = findViewById(R.id.tvLutemon2Color);
        pbLutemon2Health = findViewById(R.id.pbLutemon2Health);
        tvLutemon2Health = findViewById(R.id.tvLutemon2Health);
        
        // 战斗日志和按钮
        tvBattleLog = findViewById(R.id.tvBattleLog);
        btnAttack = findViewById(R.id.btnAttack);
        btnStrike = findViewById(R.id.btnStrike);
        btnEndBattle = findViewById(R.id.btnEndBattle);
        ivAttackIcon = findViewById(R.id.ivAttackIcon);
        ivAttackIcon.setVisibility(View.INVISIBLE);
    }

    private void updateLutemonInfo() {
        // 更新第一个精灵信息
        tvLutemon1Name.setText(lutemon1.getName());
        tvLutemon1Color.setText(lutemon1.getColor());
        pbLutemon1Health.setMax(lutemon1.getMaxHealth());
        pbLutemon1Health.setProgress(lutemon1.getCurrentHealth());
        tvLutemon1Health.setText(lutemon1.getCurrentHealth() + "/" + lutemon1.getMaxHealth());
        
        // 根据颜色设置图片
        switch (lutemon1.getColor()) {
            case "白色":
                ivLutemon1.setImageResource(R.drawable.lutemon_white);
                break;
            case "绿色":
                ivLutemon1.setImageResource(R.drawable.lutemon_green);
                break;
            case "粉色":
                ivLutemon1.setImageResource(R.drawable.lutemon_pink);
                break;
            case "橘色":
                ivLutemon1.setImageResource(R.drawable.lutemon_orange);
                break;
            case "黑色":
                ivLutemon1.setImageResource(R.drawable.lutemon_black);
                break;
        }
        
        // 更新第二个精灵信息
        tvLutemon2Name.setText(lutemon2.getName());
        tvLutemon2Color.setText(lutemon2.getColor());
        pbLutemon2Health.setMax(lutemon2.getMaxHealth());
        pbLutemon2Health.setProgress(lutemon2.getCurrentHealth());
        tvLutemon2Health.setText(lutemon2.getCurrentHealth() + "/" + lutemon2.getMaxHealth());
        
        // 根据颜色设置图片
        switch (lutemon2.getColor()) {
            case "白色":
                ivLutemon2.setImageResource(R.drawable.lutemon_white);
                break;
            case "绿色":
                ivLutemon2.setImageResource(R.drawable.lutemon_green);
                break;
            case "粉色":
                ivLutemon2.setImageResource(R.drawable.lutemon_pink);
                break;
            case "橘色":
                ivLutemon2.setImageResource(R.drawable.lutemon_orange);
                break;
            case "黑色":
                ivLutemon2.setImageResource(R.drawable.lutemon_black);
                break;
        }
    }

    private void setupButtonListeners() {
        btnAttack.setOnClickListener(v -> performNormalAttack());
        btnStrike.setOnClickListener(v -> performHeavyAttack());
        btnEndBattle.setOnClickListener(v -> {
            addBattleLog("-战斗被停止-");
            addBattleLog("-没有人胜利-");
            storage.clearBattle();
            finish();
        });
    }

    private void playAttackAnimation(boolean isPlayer1Attacking, boolean isHeavyAttack, Runnable onAnimationEnd) {
        // 获取起点和终点的位置
        int[] startLocation = new int[2];
        int[] endLocation = new int[2];
        
        if (isPlayer1Attacking) {
            ivLutemon1.getLocationInWindow(startLocation);
            ivLutemon2.getLocationInWindow(endLocation);
        } else {
            ivLutemon2.getLocationInWindow(startLocation);
            ivLutemon1.getLocationInWindow(endLocation);
        }

        // 设置攻击图标的初始位置
        ivAttackIcon.setX(startLocation[0] + ivLutemon1.getWidth() / 2f - ivAttackIcon.getWidth() / 2f);
        ivAttackIcon.setY(startLocation[1] + ivLutemon1.getHeight() / 2f - ivAttackIcon.getHeight() / 2f);
        ivAttackIcon.setVisibility(View.VISIBLE);
        
        // 如果是重击，让图标旋转
        if (isHeavyAttack) {
            ObjectAnimator rotation = ObjectAnimator.ofFloat(ivAttackIcon, "rotation", 0f, 360f);
            rotation.setDuration(600);
            rotation.start();
        }

        // 创建X轴动画
        ObjectAnimator animX = ObjectAnimator.ofFloat(ivAttackIcon, "x", 
            startLocation[0] + ivLutemon1.getWidth() / 2f - ivAttackIcon.getWidth() / 2f,
            endLocation[0] + ivLutemon2.getWidth() / 2f - ivAttackIcon.getWidth() / 2f);
        
        // 创建Y轴动画
        ObjectAnimator animY = ObjectAnimator.ofFloat(ivAttackIcon, "y",
            startLocation[1] + ivLutemon1.getHeight() / 2f - ivAttackIcon.getHeight() / 2f,
            endLocation[1] + ivLutemon2.getHeight() / 2f - ivAttackIcon.getHeight() / 2f);

        // 设置动画属性
        animX.setDuration(600);
        animY.setDuration(600);
        animX.setInterpolator(new AccelerateDecelerateInterpolator());
        animY.setInterpolator(new AccelerateDecelerateInterpolator());

        // 添加动画结束监听器
        animX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ivAttackIcon.setVisibility(View.INVISIBLE);
                if (onAnimationEnd != null) {
                    onAnimationEnd.run();
                }
            }
        });

        // 开始动画
        animX.start();
        animY.start();
    }

    private void performNormalAttack() {
        if (isBattleOver()) {
            return;
        }

        // 禁用按钮，防止动画播放时重复点击
        btnAttack.setEnabled(false);
        btnStrike.setEnabled(false);
        
        // 播放玩家攻击动画
        playAttackAnimation(true, false, () -> {
            // 计算伤害
            int damage = lutemon1.calculateAttackDamage(false);
            String result = lutemon1.getAttackResult(damage, lutemon2.getDefense());
            int actualDamage = lutemon2.calculateDamageTaken(damage, lutemon2.getDefense());
            
            lutemon2.takeDamage(actualDamage);
            addBattleLog(lutemon1.getName() + " 使用了普通攻击！");
            addBattleLog(result);
            addBattleLog("造成了 " + actualDamage + " 点伤害！");
            updateLutemonInfo();
            
            if (!isBattleOver()) {
                // 延迟一下再播放对手的攻击动画
                handler.postDelayed(() -> {
                    performOpponentAttack();
                }, 500);
            } else {
                btnAttack.setEnabled(true);
                btnStrike.setEnabled(true);
            }
        });
    }

    private void performHeavyAttack() {
        if (isBattleOver()) {
            return;
        }

        // 禁用按钮，防止动画播放时重复点击
        btnAttack.setEnabled(false);
        btnStrike.setEnabled(false);
        
        // 播放玩家重击动画
        playAttackAnimation(true, true, () -> {
            // 计算伤害
            int damage = lutemon1.calculateAttackDamage(true);
            String result = lutemon1.getAttackResult(damage, lutemon2.getDefense());
            int actualDamage = lutemon2.calculateDamageTaken(damage, lutemon2.getDefense());
            
            lutemon1.takeDamage(5); // 重击自身扣血
            lutemon2.takeDamage(actualDamage);
            addBattleLog(lutemon1.getName() + " 使用了重击！");
            addBattleLog(result);
            addBattleLog("造成了 " + actualDamage + " 点伤害！");
            addBattleLog(lutemon1.getName() + " 受到了5点反伤！");
            updateLutemonInfo();
            
            if (!isBattleOver()) {
                // 延迟一下再播放对手的攻击动画
                handler.postDelayed(() -> {
                    performOpponentAttack();
                }, 500);
            } else {
                btnAttack.setEnabled(true);
                btnStrike.setEnabled(true);
            }
        });
    }

    private void performOpponentAttack() {
        // 随机选择攻击方式
        boolean useStrike = Math.random() < 0.5;
        
        // 播放对手攻击动画
        playAttackAnimation(false, useStrike, () -> {
            int damage = lutemon2.calculateAttackDamage(useStrike);
            String result = lutemon2.getAttackResult(damage, lutemon1.getDefense());
            int actualDamage = lutemon1.calculateDamageTaken(damage, lutemon1.getDefense());
            
            if (useStrike) {
                lutemon2.takeDamage(5); // 重击自身扣血
            }
            lutemon1.takeDamage(actualDamage);
            
            addBattleLog(lutemon2.getName() + (useStrike ? " 使用了重击！" : " 使用了普通攻击！"));
            addBattleLog(result);
            addBattleLog("造成了 " + actualDamage + " 点伤害！");
            if (useStrike) {
                addBattleLog(lutemon2.getName() + " 受到了5点反伤！");
            }
            updateLutemonInfo();
            
            // 重新启用按钮
            btnAttack.setEnabled(true);
            btnStrike.setEnabled(true);
        });
    }

    private boolean isBattleOver() {
        if (!lutemon1.isAlive()) {
            addBattleLog("战斗结束！" + lutemon2.getName() + " 获胜！");
            lutemon2.addExperience(1);
            disableAttackButtons();
            storage.clearBattle();
            return true;
        }
        
        if (!lutemon2.isAlive()) {
            addBattleLog("战斗结束！" + lutemon1.getName() + " 获胜！");
            lutemon1.addExperience(1);
            disableAttackButtons();
            storage.clearBattle();
            return true;
        }
        
        return false;
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