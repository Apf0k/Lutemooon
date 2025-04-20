package com.example.lutemooon.model;

public class OrangeLutemon extends Lutemon {
    public OrangeLutemon(String name) {
        super(name, "Orange", 0, 0, 0);
    }

    public void setAttributes(int attack, int defense, int maxHealth) {
        this.attack = attack;
        this.defense = defense;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }
} 