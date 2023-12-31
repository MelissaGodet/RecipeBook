package com.example.recipefront.model;

public enum Difficulty {

    EASY("Easy"),
    MEDIUM("Medium"),
    DIFFICULT("Difficult");

    private String displayName;

    Difficulty(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

}
