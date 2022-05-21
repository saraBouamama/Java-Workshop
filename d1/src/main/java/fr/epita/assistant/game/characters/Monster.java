package fr.epita.assistant.game.characters;

public abstract class Monster extends Character implements Attacker{
    protected int damage;
    protected String name;


    @Override
    public void printStats(){
        System.out.println("Health: " + health);
        System.out.println("Damage: " + damage);
        System.out.println("Armour: " + armour);
    }

    //DAMAGE
    @Override
    public int getDamage() {
        return damage;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }
    @Override
    void takeDamage(int damage){
        double dam = (damage - armour * 0.1);
        this.health =  this.health - (int)dam;
        this.setArmour(this.getArmour() - 1);
        System.out.println(this.name + " takes " + damage + " damage");
        if (this.getHealth() <= 0){
            this.setHealth(0);
            this.setAlive(false);
            System.out.println(this.name + " is dead");
        }
    }

    //NAME
    @Override
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    //ATTACK
    @Override
    public void attack(Character character){
        System.out.println(this.name + " attacks " + character.name);
    }
    @Override
    public String toString(){
        return "M";
    }

}