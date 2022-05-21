package fr.epita.assistant.game.characters;

abstract class Character {
    protected String name;
    protected int health;
    protected boolean isAlive;
    protected int armour;

    //NAME
    public String getName() {
        return name;
    }

    //HEALTH
    public int getHealth() {
        return health;
    }
    public void setHealth(int health){
        if (health < 0){
            health = 0;
        }
        this.health = health;
    }

    //ALIVE
    public boolean isAlive() {
        return isAlive;
    }
    public void setAlive(boolean alive) {
        isAlive = alive;
    }
    public boolean isDead(){
        if (this.isAlive){
            return false;
        }
        else{
            return true;
        }
    }

    //ARMOUR
    public int getArmour(){
        return armour;
    }
    public void setArmour(int armour) {
        if (armour < 0){
            armour = 0;
        }
        this.armour = armour;
    }
    abstract void printStats();
    abstract int getDamage();
    abstract void takeDamage(int damage);
}