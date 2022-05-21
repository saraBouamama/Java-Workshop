package fr.epita.assistant.game.food;

public class RawSteak extends Food implements Cookable{

    public RawSteak(){
        this.calories = 15;
        this.name = "Raw Steak";
    }

    @Override
    public Food cook() {
        return new CookedSteak();
    }
}
