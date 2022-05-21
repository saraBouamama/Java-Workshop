package fr.epita.assistant.game.food;

public class CookedSteak extends Food implements Cookable{
    public CookedSteak(){
        this.calories = 20;
        this.name ="Cooked Steak";
    }

    @Override
    public Food cook() {
        return new BurnedSteak();
    }
}
