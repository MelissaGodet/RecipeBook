package com.example.recipefront;

public enum Ingredient {
    FLOUR("Flour"),
    SUGAR("Sugar"),
    EGGS("Eggs"),
    MILK("Milk"),
    BUTTER("Butter"),
    VANILLA_EXTRACT("Vanilla Extract"),
    CHOCOLATE_CHIPS("Chocolate Chips"),

    SALT("Salt"),
    PEPPER("Pepper"),
    OLIVE_OIL("Olive Oil"),
    GARLIC("Garlic"),
    ONION("Onion"),
    CHEESE("Cheese"),
    TOMATOES("Tomatoes"),
    BASIL("Basil"),

    CARROT("Carrot"),
    BROCCOLI("Broccoli"),
    SPINACH("Spinach"),
    ZUCCHINI("Zucchini"),

    CHICKEN("Chicken"),
    BEEF("Beef"),
    PORK("Pork"),
    LAMB("Lamb"),

    PASTA("Pasta"),
    COUSCOUS("Couscous"),
    RICE("Rice"),

    BALSAMIC_VINEGAR("Balsamic vinegar");

    private String displayName;

    Ingredient(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}