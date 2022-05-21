package fr.epita.assistant.game.characters;

import fr.epita.assistant.game.utils.Coord;

public class Skalah extends Monster{
    public Skalah() {
        this.health = 100;
        this.damage = 20;
        this.armour = 10;
        this.name = "Skalah";
        this.isAlive = true;
    }

    @Override
    public void attack(Character character) {
        super.attack(character);
        character.takeDamage(this.damage);
    }
}
