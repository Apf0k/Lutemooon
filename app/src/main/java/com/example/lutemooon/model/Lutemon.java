package com.example.lutemooon.model;

public class Lutemon {
    protected String name;
    protected String color;
    protected int attack;
    protected int defense;
    protected int maxHealth;
    protected int currentHealth;
    protected int experience;
    protected int id;
    private static int idCounter = 0;

    public Lutemon(String name, String color, int attack, int defense, int maxHealth) {
        this.name = name;
        this.color = color;
        this.attack = attack;
        this.defense = defense;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.experience = 0;
        this.id = idCounter++;
    }

    // Getters
    public String getName() { return name; }
    public String getColor() { return color; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getMaxHealth() { return maxHealth; }
    public int getCurrentHealth() { return currentHealth; }
    public int getExperience() { return experience; }
    public int getId() { return id; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setCurrentHealth(int health) { this.currentHealth = health; }
    public void addExperience(int exp) { this.experience += exp; }

    // Battle methods
    public void takeDamage(int damage) {
        currentHealth = Math.max(0, currentHealth - damage);
    }

    public void heal() {
        currentHealth = maxHealth;
    }

    public boolean isAlive() {
        return currentHealth > 0;
    }

    public int calculateAttackDamage(boolean isStrike) {
        if (isStrike) {
            return attack + 5;
        }
        return attack;
    }

    public String getAttackResult(int damage, int targetDefense) {
        if (damage > targetDefense + 3) {
            return "Critical hit!";
        } else if (damage >= targetDefense) {
            return "Attack successful!";
        } else {
            return "Attack deflected!-^-";
        }
    }

    public int calculateDamageTaken(int damage, int targetDefense) {
        if (damage > targetDefense + 3) {
            return damage + 3;
        } else if (damage >= targetDefense) {
            return damage;
        } else {
            return 1;
        }
    }
} 