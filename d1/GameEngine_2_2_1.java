package fr.epita.assistant;


import com.fasterxml.jackson.databind.ObjectMapper;
import fr.epita.assistant.dto.BoardDTO;
import fr.epita.assistant.dto.OutputDTO;
import fr.epita.assistant.game.Board;
import fr.epita.assistant.game.characters.*;
import fr.epita.assistant.game.utils.*;

import java.io.*;
import java.util.*;


public final class GameEngine {
    private final Scanner scanner;
    private final Random random;
    private final String pathToJson;
    private final String pathToCommands;
    private Board board;

    public GameEngine(String pathToJson, String pathToCommands) {
        this.pathToJson = pathToJson;
        this.pathToCommands = pathToCommands;
        scanner = new Scanner(System.in);
        random = new Random();
    }

    public void play() {
        System.out.println("Welcome to the game!");
        this.initializeBoard();
        if (pathToCommands.equals("")) {
            this.playCLIGame();
        } else {
            this.playAutomaticGame();
        }
    }

    /*
     * =======================================================
     *
     *                PLAY CLI Game
     *
     * =======================================================
     * */

    private void playCLIGame() {
        StringBuilder logs;
        int turn = 1;
        logs = new StringBuilder();
        while (board.getPlayer().isAlive()) {
            System.out.println("===========");
            System.out.println("Turn " + turn);
            System.out.println("===========\n");
            System.out.println(board.printBoard());
            System.out.println(
                    "What do you wanna do ? To choose, type the first word of the choice you want");
            StringBuilder stringBuilder =
                    (new StringBuilder()).append("-Move\t-Stats\t-Attack\t-Quit");
            System.out.println(stringBuilder);
            String choice = this.scanner.nextLine().toLowerCase().trim();
            switch (choice) {
                case "attack" -> this.attackAction(false, false, logs);
                case "move" -> this.moveAction(false, false, logs);
                case "stats" -> this.stats(false, false, logs);
                case "quit" -> {
                    logs.append("\n");
                    this.gameOutput(logs);
                    return;
                }
                default -> {
                    System.out.println("Wrong action");
                    logs.append("Error");
                }
            }
            logs.append("\n");
            turn++;
        }
    }

    /*
     * =======================================================
     *                Actions
     * =======================================================
     * */

    private void attackAction(Boolean isMonsterHere, Boolean inTheWizardHouse, StringBuilder logs) {
        if (logs != null) {
            logs.append("Attack");
        }
        System.out.println("There is no monster here");
    }

    private void moveAction(boolean isMonsterHere, boolean inTheWizardHouse, StringBuilder logs) {
        if (logs != null) {
            logs.append("Move");
        }
        System.out.println("Where do you wanna go ?");
        System.out.println("-Up\t-Down\t-Left\t-Right");
        var direction = scanner.nextLine().toLowerCase().trim();
        switch (direction) {
            case "up" -> board.movePlayer(Direction.UP);
            case "down" -> board.movePlayer(Direction.DOWN);
            case "left" -> board.movePlayer(Direction.LEFT);
            case "right" -> board.movePlayer(Direction.RIGHT);
            default -> System.out.println("Wrong direction");
        }
        if (logs != null)
            logs.append(" ").append(direction);
    }

    private void stats(boolean isMonsterHere, boolean inTheWizardHouse, StringBuilder logs) {
        logs.append("Stats");
        System.out.println("Your stats are :");
        board.getPlayer().printStats();
    }

    /*
     * =======================================================
     *
     *                PLAY Automatic Game
     *
     * =======================================================
     * */


    private void playAutomaticGame() {
        final StringBuilder logs = new StringBuilder();
        try (BufferedReader br =
                     new BufferedReader(new FileReader(this.pathToCommands))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(board.printBoard());
                ArrayList<String> action =
                        new ArrayList<>(Arrays.asList(line.split(" ")));
                switch (action.get(0).toLowerCase()) {
                    case "move" -> this.moveFromCmd(action);
                    case "stats" -> this.stats(false, false, logs);
                    default -> {
                        System.out.println("Wrong action");
                        logs.append("Error");
                    }
                }
            }
            if (board.getPlayer().isAlive())
                System.out.println("You won!");
            else
                System.out.println("You lost!");
            gameOutput(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Move in the direction given in the command
    private void moveFromCmd(ArrayList<String> action) {
        if (action.size() != 2) {
            System.out.println("Wrong action");
        } else {
            switch (action.get(1).toLowerCase()) {
                case "up" -> board.movePlayer(Direction.UP);
                case "down" -> board.movePlayer(Direction.DOWN);
                case "left" -> board.movePlayer(Direction.LEFT);
                case "right" -> board.movePlayer(Direction.RIGHT);
                default -> System.out.println("Wrong direction");
            }
        }
    }


    /*
     * =======================================================
     *
     *               Save Game Output
     *
     * =======================================================
     * */

    //Save the list of commands in a .txt file
    //Call outputJson to save the result of the game in a .json file
    private void gameOutput(StringBuilder logs) {
        if (logs != null) {
            System.out.println("Enter a name for the log files");
            String filename = this.scanner.nextLine();
            if (filename.isEmpty()) {
                filename = "logs";
            }
            this.outputJson(filename);
            File file = new File(filename+".txt");
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write(logs.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Save the result of the game in a .json file
    private void outputJson(String filename) {
        OutputDTO.Coord coord = null;
        if (board.getPlayer().isAlive())
            coord = new OutputDTO.Coord(board.getPlayer().getCoord().getX(), board.getPlayer().getCoord().getY());
        ArrayList<String> inventory = new ArrayList<>();
        ArrayList<OutputDTO.Infos> monsters = new ArrayList<>();
        ArrayList<OutputDTO.Infos> foods = new ArrayList<>();
        OutputDTO.PlayerInfos playerInfos =
                new OutputDTO.PlayerInfos(
                        board.getPlayer().isAlive(), coord,
                        board.getPlayer().getHealth(), inventory);
        OutputDTO outputDTO = new OutputDTO(
                playerInfos,
                new OutputDTO.Coord(-1, -1),
                foods, monsters);
        File file = new File(filename + ".json");
        try (BufferedWriter bw =
                     new BufferedWriter(new FileWriter(file))) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(bw, outputDTO);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * =======================================================
     *
     *                INITIALIZE THE BOARD
     *
     * =======================================================
     * */

    private void initializeBoard() {
        if (!pathToJson.isEmpty()) {
            System.out.println(
                    "The board will be created thanks to the JSON file given.");
            boardFromJson();
        } else {
            System.out.println("Let's create our board !");
            boardFromCLI();
        }
    }


    /*
     * =======================================================
     *                INITIALIZE FROM CLI
     * =======================================================
     * */

    private void boardFromCLI() {
        System.out.println(
                "What will be the size of your board (m x m)? The minimum size is 5.");
        int maxElementOnTheBoard;
        int size = -1;
        do {
            try {
                maxElementOnTheBoard =
                        Integer.parseInt(this.scanner.nextLine());
                if (maxElementOnTheBoard < 5) {
                    System.out.println("The minimum size is 5");
                } else {
                    size = maxElementOnTheBoard;
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid input");
                e.printStackTrace();
            }
        } while (size == -1);
        ArrayList<Coord> possibleCoord = getPossibleCoords(size);
        Player player = new Player(this.getCoord(possibleCoord));
        this.board = new Board(size, player);

        System.out.println("Do you want to save the board in a json file ? (y/n)");
        String answer = this.scanner.nextLine();
        if (answer.equals("y")) {
            saveBoard();
        }

    }

    private void saveBoard() {
        System.out.println("What is the name of the json file ?");
        String fileName = this.scanner.nextLine();
        ArrayList<BoardDTO.Infos> monsters = new ArrayList<>();
        ArrayList<BoardDTO.Infos> foods = new ArrayList<>();
        BoardDTO.Coord playerCoord =
                new BoardDTO.Coord(this.board.getPlayer().getCoord().getX(),
                        this.board.getPlayer().getCoord().getY());
        BoardDTO.Coord wizardCoord =
                new BoardDTO.Coord(-1, -1);
        BoardDTO boardDTO =
                new BoardDTO(this.board.getSize(), playerCoord,
                        wizardCoord, monsters, foods);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(fileName), boardDTO);
        } catch (IOException e) {
            System.err.println(
                    "An error occured while saving the board in a json file");
        }
    }

    private Coord getCoord(ArrayList<Coord> possibleCoord) {
        int index = this.random.nextInt(possibleCoord.size());
        Coord coord = possibleCoord.get(index);
        possibleCoord.remove(index);
        return coord;
    }

    /*
     * =======================================================
     *                INITIALIZE FROM JSON
     * =======================================================
     * */
    private void boardFromJson() {
        BoardDTO boardDTO = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            boardDTO = mapper.readValue(new File(this.pathToJson),
                    BoardDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert boardDTO != null;
        int size = boardDTO.getSize();
        this.board = new Board(size, new Player(new Coord(boardDTO.getPlayerCoord().getX(), boardDTO.getPlayerCoord().getY())));
    }
    /*
     * =======================================================
     *                           HELPERS
     * =======================================================
     * */

    private ArrayList<Coord> getPossibleCoords(int size) {
        ArrayList<Coord> possibleCoord = new ArrayList<>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                possibleCoord.add(new Coord(x, y));
            }
        }
        return possibleCoord;
    }


}