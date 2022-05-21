package fr.epita.assistant.game.food;

public class Food {
    protected int calories;
    protected String name;

    public int getCalories() {
        return calories;
    }
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "F";
    }
}
