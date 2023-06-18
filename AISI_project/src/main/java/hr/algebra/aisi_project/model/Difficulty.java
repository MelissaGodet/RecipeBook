package hr.algebra.aisi_project.model;

public enum Difficulty {

    T("T"),
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
