package fr.epita.assistant.dto;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public final class OutputDTO {
    @Nullable
    private final fr.epita.assistant.dto.OutputDTO.PlayerInfos playerInfos;
    @NotNull
    private final fr.epita.assistant.dto.OutputDTO.Coord wizardCoord;
    @NotNull
    private final ArrayList<fr.epita.assistant.dto.OutputDTO.Infos> foods;
    @NotNull
    private final ArrayList<fr.epita.assistant.dto.OutputDTO.Infos> monsters;


    @Nullable
    public fr.epita.assistant.dto.OutputDTO.PlayerInfos getPlayerInfos() {
        return this.playerInfos;
    }

    @NotNull
    public fr.epita.assistant.dto.OutputDTO.Coord getWizardCoord() {
        return this.wizardCoord;
    }

    public OutputDTO(@Nullable fr.epita.assistant.dto.OutputDTO.PlayerInfos playerInfos, @NotNull fr.epita.assistant.dto.OutputDTO.Coord wizardCoord, @NotNull ArrayList<fr.epita.assistant.dto.OutputDTO.Infos> foods, @NotNull ArrayList<fr.epita.assistant.dto.OutputDTO.Infos> monsters) {
        this.playerInfos = playerInfos;
        this.wizardCoord = wizardCoord;
        this.foods = foods;
        this.monsters = monsters;
    }

    @NotNull
    public ArrayList<fr.epita.assistant.dto.OutputDTO.Infos> getFoods() {
        return this.foods;
    }

    @NotNull
    public ArrayList<fr.epita.assistant.dto.OutputDTO.Infos> getMonsters() {
        return this.monsters;
    }

    public static final class PlayerInfos {
        private final boolean isAlive;
        @Nullable
        private final fr.epita.assistant.dto.OutputDTO.Coord position;
        private final int health;
        private final @NotNull ArrayList<String> inventory;

        public boolean isAlive() {
            return this.isAlive;
        }

        @Nullable
        public fr.epita.assistant.dto.OutputDTO.Coord getPosition() {
            return this.position;
        }

        public int getHealth() {
            return this.health;
        }

        public PlayerInfos(boolean isAlive, @Nullable fr.epita.assistant.dto.OutputDTO.Coord position, int health, @NotNull ArrayList<String> inventory) {
            super();
            this.isAlive = isAlive;
            this.position = position;
            this.health = health;
            this.inventory = inventory;
        }

        @NotNull
        public ArrayList<String> getInventory() {
            return this.inventory;
        }


    }

    public static final class Coord {
        private final int x;
        private final int y;

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    public static final class Infos {
        private final int x;
        private final int y;
        @NotNull
        private final String type;

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        @NotNull
        public String getType() {
            return this.type;
        }

        public Infos(int x, int y, @NotNull String type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }


    }
}