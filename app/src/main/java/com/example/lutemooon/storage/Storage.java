package com.example.lutemooon.storage;

import com.example.lutemooon.model.Lutemon;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static Storage instance;
    private final List<Lutemon> lutemons;
    private Lutemon trainingLutemon;
    private final List<Lutemon> battleLutemons;

    private Storage() {
        lutemons = new ArrayList<>();
        battleLutemons = new ArrayList<>();
    }

    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    public void addLutemon(Lutemon lutemon) {
        lutemons.add(lutemon);
    }

    public List<Lutemon> getLutemons() {
        return lutemons;
    }

    public boolean moveToTraining(Lutemon lutemon) {
        if (trainingLutemon != null) {
            return false;
        }
        trainingLutemon = lutemon;
        return true;
    }

    public Lutemon getTrainingLutemon() {
        return trainingLutemon;
    }

    public void removeFromTraining() {
        trainingLutemon = null;
    }

    public boolean moveToBattle(Lutemon lutemon) {
        if (battleLutemons.size() >= 2) {
            return false;
        }
        battleLutemons.add(lutemon);
        return true;
    }

    public List<Lutemon> getBattleLutemons() {
        return battleLutemons;
    }

    public void clearBattle() {
        for (Lutemon lutemon : battleLutemons) {
            lutemon.heal();
        }
        battleLutemons.clear();
    }

    public void removeFromBattle(Lutemon lutemon) {
        battleLutemons.remove(lutemon);
    }
} 