package fr.epita.assistant.dto;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public final class BoardDTO {
    private int size;
    @NotNull
    private BoardDTO.Coord playerCoord;
    @NotNull
    private BoardDTO.Coord wizardCoord;
    @NotNull
    private ArrayList<Infos> foods;
    @NotNull
    private ArrayList<Infos> monsters;

    public BoardDTO() {
        this.foods = new ArrayList<>();
        this.monsters = new ArrayList<>();
        wizardCoord = new Coord();
        playerCoord = new Coord();
    }

    public BoardDTO(int size, @NotNull Coord playerCoord, @NotNull Coord wizardCoord, @NotNull ArrayList<Infos> monsters, @NotNull ArrayList<Infos> foods) {
        this.size = size;
        this.playerCoord = playerCoord;
        this.wizardCoord = wizardCoord;
        this.monsters = monsters;
        this.foods = foods;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @NotNull
    public BoardDTO.Coord getPlayerCoord() {
        return this.playerCoord;
    }

    public void setPlayerCoord(@NotNull BoardDTO.Coord playerCoord) {
        this.playerCoord = playerCoord;
    }

    @NotNull
    public BoardDTO.Coord getWizardCoord() {
        return this.wizardCoord;
    }

    public void setWizardCoord(@NotNull BoardDTO.Coord wizardCoord) {
        this.wizardCoord = wizardCoord;
    }

    @NotNull
    public ArrayList<Infos> getFoods() {
        return this.foods;
    }

    public void setFoods(@NotNull ArrayList<Infos> foods) {
        this.foods = foods;
    }

    @NotNull
    public ArrayList<Infos> getMonsters() {
        return this.monsters;
    }

    public void setMonsters(@NotNull ArrayList<Infos> monsters) {
        this.monsters = monsters;
    }


    public static final class Coord {
        private int x;
        private int y;

        public Coord(Integer x, Integer y) {
            this.x = x;
            this.y = y;
        }

        public Coord() {

        }

        public int getX() {
            return this.x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return this.y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    public static final class Infos {
        private int x;
        private int y;
        private String type;

        public Infos(Integer x, Integer y, String type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }

        public Infos(){

        }

        public int getX() {
            return this.x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return this.y;
        }

        public void setY(int y) {
            this.y = y;
        }

        @Nullable
        public String getType() {
            return this.type;
        }

        public void setType(@Nullable String type) {
            this.type = type;
        }
    }
}