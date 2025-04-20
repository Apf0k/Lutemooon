package com.example.lutemooon.model;

public class GreenLutemon extends Lutemon {
    public GreenLutemon(String name) {
        super(name, "Green", 0, 0, 0);
    }

    public void setAttributes(int attack, int defense, int maxHealth) {
        this.attack = attack;
        this.defense = defense;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }
} 