package hr.algebra.aisi_project.model;

public enum Ingredient {
    // Ingrédients sucrés
    FLOUR("Flour"),
    SUGAR("Sugar"),
    EGGS("Eggs"),
    MILK("Milk"),
    BUTTER("Butter"),
    VANILLA_EXTRACT("Vanilla Extract"),
    CHOCOLATE_CHIPS("Chocolate Chips"),

    // Ingrédients salés
    SALT("Salt"),
    PEPPER("Pepper"),
    OLIVE_OIL("Olive Oil"),
    GARLIC("Garlic"),
    ONION("Onion"),
    CHEESE("Cheese"),
    TOMATOES("Tomatoes"),
    BASIL("Basil"),

    // Ingrédients légumes
    CARROT("Carrot"),
    BROCCOLI("Broccoli"),
    SPINACH("Spinach"),
    ZUCCHINI("Zucchini"),

    // Ingrédients viandes
    CHICKEN("Chicken"),
    BEEF("Beef"),
    PORK("Pork"),
    LAMB("Lamb"),

    // Ingrédients basiques
    PASTA("Pasta"),
    COUSCOUS("Couscous"),
    RICE("Rice"),
    T("T"),
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