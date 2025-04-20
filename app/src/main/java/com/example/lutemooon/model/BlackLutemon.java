package com.example.lutemooon.model;

public class BlackLutemon extends Lutemon {
    public BlackLutemon(String name) {
        super(name, "Black", 0, 0, 0);
    }

    public void setAttributes(int attack, int defense, int maxHealth) {
        this.attack = attack;
        this.defense = defense;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }
} 