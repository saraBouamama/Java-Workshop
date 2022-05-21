package fr.epita.assistant.game.characters;

import fr.epita.assistant.game.utils.Coord;

public class Wizard extends Character {
    private Integer manaPoint;
    private Coord coord;

    public Coord getCoord() {
        return coord;
    }

    public Integer getManaPoint() {
        return manaPoint;
    }
    public void setManaPoint(Integer manaPoint) {
        if (manaPoint < 100){
            this.manaPoint = manaPoint;
        }
        else{
            this.manaPoint = 100;
        }
    }

    @Override
    public String toString() {
        return "W";
    }

    public Wizard(Coord coord){
        this.coord = coord;
        this.health = 100;
        this.armour = 15;
        this.manaPoint = 100;
        this.name = "Intelli Jee";
        setAlive(true);
    }

    public void printStats() {
        System.out.println("Health: " + health);
        System.out.println("Damage: " + this.getDamage());
        System.out.println("Armour: "+  armour);
        System.out.println("Mana Point: "+ manaPoint);
    }

    @Override
    public int getDamage() {
        return 0;
    }

    public void heal(Player player) {
        if (manaPoint < 20){
            System.out.println("Intelli Jee doesn't have enough mana to heal");
        }
        else if (player.isDead()){
            System.out.println("You are dead, you can't be healed");
        }
        else if (player.getHealth() >= 100){
            System.out.println("You already have the maximum health");
        }
        else{
            player.health = player.health + 10;
            this.manaPoint = this.manaPoint - 20;
            System.out.println("Intelli Jee healed you, you now have " + player.health + " health");
        }
    }

    public boolean enchant(Player player) {
        if (this.manaPoint < 15){
            System.out.println("Intelli Jee doesn't have enough mana to use the Eclipse spell and enchant your weapon");
            return false;
        }
        else{
            player.setDamage(player.getDamage() + 10);
            this.manaPoint -= 15;
            System.out.println("Intelli Jee screams \"Eclipse !\" and enchants your weapon, you deal now " + player.getDamage() + " damage at each attack");
            return true;
        }

    }

    @Override
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0){
            this.setAlive(false);
            this.health = 0;
        }
    }
}
