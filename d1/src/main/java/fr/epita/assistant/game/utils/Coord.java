package fr.epita.assistant.game.utils;


import java.util.Objects;

public class Coord {
    private int x = 0;
    private int y = 0;

    public Coord(int x, int y){
        if (x < 0){
            x = 0;
        }
        if (y < 0){
            y =  0;
        }
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        if (x >= 0) {
            this.x = x;
        }
    }

    public int getY() {
        return y;
    }
    public void setY(int y) {
        if (y >= 0){
            this.y = y;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return x == coord.x && y == coord.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return '(' +
                "x=" + x +
                ", y=" + y +
                ")";
    }
    public void move(Direction direction){
        switch(direction){
            case UP: //UP
                y--;
                break;
            case DOWN: //DOWN
                y++;
                break;
            case LEFT: //LEFT
                x--;
                break;
            case RIGHT: //RIGHT
                x++;
                break;
        }
    }
}
