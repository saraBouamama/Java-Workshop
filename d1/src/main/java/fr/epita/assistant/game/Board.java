package fr.epita.assistant.game;

import fr.epita.assistant.game.characters.Monster;
import fr.epita.assistant.game.characters.Player;
import fr.epita.assistant.game.characters.Wizard;
import fr.epita.assistant.game.food.Food;
import fr.epita.assistant.game.utils.Coord;
import fr.epita.assistant.game.utils.Direction;

import java.beans.beancontext.BeanContextSupport;
import java.lang.management.MonitorInfo;
import java.security.PrivateKey;
import java.util.HashMap;

public class Board {
    private int size;
    private Player player;
    private Wizard wizard;
    private HashMap<Coord, Monster> hmapmonster;
    private HashMap<Coord, Food> hmapfood;

    public HashMap<Coord, Food> getFoods() {
        return hmapfood;
    }

    public HashMap<Coord, Monster> getMonsters() {
        return hmapmonster;
    }

    //WIZARD
    public Wizard getWizard() {
        return wizard;
    }

    //PLAYER
    public Player getPlayer() {
        return player;
    }


    //SIZE
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }

    //BOARD
    public Board(int size, Player player ,Wizard wizard, HashMap<Coord, Monster> hmapmonster, HashMap<Coord, Food> hmapfood)
    {
        this.player = player;
        this.wizard = wizard;
        this.hmapfood = hmapfood;
        this.hmapmonster = hmapmonster;
        this.size = size;
    }

    public String printBoard() {
        StringBuilder board = new StringBuilder();
        for (var i = 0; i < this.size; i++) {
            for (var j = 0; j < this.size + 1; j++) {
                Coord cord = new Coord(j, i);
                if (hmapfood.containsKey(cord)) {
                    board.append("| F ");
                    if (j == player.getCoord().getX() && i == player.getCoord().getY()) {
                        board.deleteCharAt(board.length() - 1);
                        board.append(player.toString()).append(" ");
                    }
                } else if (hmapmonster.containsKey(cord)) {
                    board.append("| M ");
                    if (j == player.getCoord().getX() && i == player.getCoord().getY()) {
                        board.deleteCharAt(board.length() - 1);
                        board.append(player.toString()).append(" ");
                    }
                } else if (wizard.isAlive() && j == wizard.getCoord().getX() && i == wizard.getCoord().getY()) {
                    board.append("| W ");
                    if (j == player.getCoord().getX() && i == player.getCoord().getY()) {
                        board.deleteCharAt(board.length() - 1);
                        board.append(player.toString()).append(" ");
                    }
                } else if (j == player.getCoord().getX() && i == player.getCoord().getY()) {
                    board.append("| ").append(player.toString()).append(" ");
                }
                else if (j == this.size) {
                    board.append("|\n");
                    break;
                } else {
                    board.append("|   ");
                }
            }
        }
        return board.toString();
    }


    public boolean IsCoord(Coord coord, Direction dir){
        int x = 0;
        int y = 0;
        if (dir == Direction.LEFT){
            x = -1;
        }
        else if (dir == Direction.RIGHT){
            x = 1;
        }
        else if (dir == Direction.UP){
            y = -1;
        }
        else if (dir == Direction.DOWN){
            y = 1;
        }
        int i = coord.getX() + x;
        int j = coord.getY() + y;
        if (i >= size || j >= size || i < 0 || j < 0){
            return false;
        }
        else {
            return true;
        }
    }


    //MOVE PLAYER
    public boolean movePlayer(Direction direction){
        if (IsCoord(this.player.getCoord(), direction)){
            this.player.move(direction);
            System.out.println("You moved to " + player.getCoord().toString());
            return true;
        }
        else {
            System.out.println("You can't move there!");
            return false;
        }
    }
}