package fr.epita.assistant.game.characters;

public class Coatlin extends Monster {

    public Coatlin() {
        this.health = 100;
        this.damage = 10;
        this.armour = 10;
        this.isAlive = true;
        this.name = "Coatlin";
    }

    @Override
    public void attack(Character character) {
        super.attack(character);
        character.takeDamage((int) (this.damage + 0.1*this.health));
    }
}
