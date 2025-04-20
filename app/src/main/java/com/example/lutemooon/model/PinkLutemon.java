package com.example.lutemooon.model;

public class PinkLutemon extends Lutemon {
    public PinkLutemon(String name) {
        super(name, "Pink", 0, 0, 0);
    }

    public void setAttributes(int attack, int defense, int maxHealth) {
        this.attack = attack;
        this.defense = defense;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }
} 