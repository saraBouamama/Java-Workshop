package fr.epita.assistant.game.characters;

import fr.epita.assistant.game.food.Food;
import fr.epita.assistant.game.utils.Coord;
import fr.epita.assistant.game.utils.Direction;

import java.util.ArrayList;
import java.util.Locale;

public final class Player extends Character implements Attacker {
    private int damage;
    private Coord coord;
    private ArrayList<Food> inventory = new ArrayList<Food>();

    public Player(Coord coord) {
        this.coord = coord;
        this.health = 100;
        this.damage = 10;
        this.armour = 15;
        this.isAlive = true;
        this.name = "Player";
    }



    @Override
    public String toString() {
        return "P";
    }
    //DAMAGE
    public int getDamage() {
        return damage;
    }
    @Override
    void takeDamage(int damage) {
        if (damage > 0) {
            health -= (damage - armour * 0.5);
            if (health < 0) {
                health = 0;
            }
            armour--;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("You take ").append(damage).append(" damage\n");
            if (health <= 0) {
                this.isAlive = false;
                stringBuilder.append("You are dead\n");
            }
            System.out.print(stringBuilder.toString());
        }
    }
    public void setDamage(int damage) {
        if (damage < 0) {
            return;
        }
        this.damage = damage;
    }

    public Coord getCoord() {
        return coord;
    }

    public void heal(int hp) {
        health += hp;
    }

    public void move(Direction direction) {
        if (direction.equals(Direction.DOWN)) {
            this.coord.setY(this.coord.getY() + 1);
        }
        if (direction.equals(Direction.UP)) {
            this.coord.setY(this.coord.getY() - 1);
        }
        if (direction.equals(Direction.RIGHT)) {
            this.coord.setX(this.coord.getX() + 1);
        }
        if (direction.equals(Direction.LEFT)) {
            this.coord.setX(this.coord.getX() - 1);
        }
    }

    @Override
    public void attack(Character character) {
        if (this.damage > 0) {
            character.takeDamage(this.damage);
            this.damage -= 1;
        }
    }
    //STATS
    @Override
    public void printStats() {
        System.out.println("Health: " + health);
        System.out.println("Damage: " + damage);
        System.out.println("Armour: " + armour);

        if (!inventory.isEmpty()) {
            System.out.println("Inventory:");
            for (var i : inventory) {
                System.out.println(i.getName());
            }
        }
    }

    //FOOD
    public ArrayList<Food> getInventory() {
        return inventory;}
    public void addFood(Food food) {
        inventory.add(food);
    }
    public void eatFood(Food food) {
        this.health += food.getCalories();
    }
    public Food takeFood(String foodName) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getName().toLowerCase(Locale.ROOT).equals(foodName))
            {
                Food f = new Food();
                f = inventory.get(i);
                inventory.remove(i);
                return f;
            }
        }
        return null;
    }
}