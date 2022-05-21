package fr.epita.assistant;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.epita.assistant.dto.BoardDTO;
import fr.epita.assistant.dto.OutputDTO;
import fr.epita.assistant.game.Board;
import fr.epita.assistant.game.characters.*;
import fr.epita.assistant.game.utils.*;

import java.io.*;
import java.util.*;

import static java.lang.System.exit;


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
        boolean isLastGame = false;
        System.out.println("Let's play!");
        int turn = 1;
        logs = new StringBuilder();
        boolean inTheWizardHouse;
        while (board.getPlayer().isAlive()) {
            inTheWizardHouse =
                    board.getWizard().getCoord().equals(board.getPlayer().getCoord());
            System.out.println("===========");
            System.out.println("Turn " + turn);
            System.out.println("===========\n");
            System.out.println(board.printBoard());
            System.out.println(
                    "What do you wanna do ? To choose, type the first word of the choice you want");
            StringBuilder stringBuilder =
                    (new StringBuilder()).append("-Move\t-Stats");
            if (inTheWizardHouse) {
                stringBuilder.append("\t-Talk to Intelli Jee")
                        .append("\t-Attack Intelli Jee");
            } else {
                stringBuilder.append("\t-Attack");
            }
            System.out.println(stringBuilder);
            String choice = this.scanner.nextLine().toLowerCase().trim();
            switch (choice) {
                case "attack":
                    this.attackAction(false, inTheWizardHouse, logs);
                    break;
                case "quit":
                    logs.append("\n");
                    this.gameOutput(logs);
                    return;
                case "move":
                    this.moveAction(false, inTheWizardHouse, logs);
                    break;
                case "talk":
                    if (inTheWizardHouse) {
                        this.talkToWizard(logs);
                    } else {
                        System.out.println("You are not in the Intelli Jee's house");
                    }
                    break;
                case "stats":
                    this.stats(false, inTheWizardHouse, logs);
                    break;
                default:
                    System.out.println("Wrong action");
                    logs.append("Error");
                    break;
            }

            logs.append("\n");
            if(!choice.equals("talk"))
                board.getWizard().setManaPoint(board.getWizard().getManaPoint() + 10);
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
        if (inTheWizardHouse) {
            System.out.println("You attack the wizard");
            board.getPlayer().attack(board.getWizard());
        } else {
            System.out.println("There is no monster here");
        }
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
        if (board.getPlayer().getCoord() == board.getWizard().getCoord())
            System.out.println("You found Intelli Jee's house");
        if (logs != null)
            logs.append(" ").append(direction);
    }

    private void stats(boolean isMonsterHere, boolean inTheWizardHouse, StringBuilder logs) {
        logs.append("Stats");
        System.out.println("Your stats are :");
        board.getPlayer().printStats();
        if (inTheWizardHouse) {
            System.out.println("Intelli Jee's stats are :");
            board.getWizard().printStats();
        }
    }

    private void talkToWizard(StringBuilder logs) {
        System.out.println("What should the wizard do ?");
        System.out.println("-Heal you\t-Enchant your weapon");
        logs.append("Talk");
        switch (scanner.nextLine().toLowerCase().trim()) {
            case "heal" -> {
                board.getWizard().heal(board.getPlayer());
                logs.append(" Heal");
            }

            case "enchant" -> {
                board.getWizard().enchant(board.getPlayer());
                logs.append(" Enchant");
            }
            default -> {
                System.out.println("Wrong action");
                logs.append(" Error");
            }
        }
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
                boolean inTheWizardHouse =
                        board.getWizard().getCoord() == board.getPlayer().getCoord();
                ArrayList<String> action =
                        new ArrayList<>(Arrays.asList(line.split(" ")));
                String choice = action.get(0).toLowerCase();
                switch (choice) {
                    case "move" -> moveFromCmd(action);
                    case "attack" -> attackAction(isMonsterHere, isFoodHere, logs);
                    case "talk" -> talkToWizardFromCmd(action, inTheWizardHouse);
                    case "stats" -> stats(false, inTheWizardHouse, logs);
                    default -> System.out.println("Wrong action");
                }
                if(!choice.equals("talk"))
                    board.getWizard().setManaPoint(board.getWizard().getManaPoint() + 10);
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

    /*
     * =======================================================
     *                Actions
     * =======================================================
     * */

    private void talkToWizardFromCmd(ArrayList<String> action, boolean inTheWizardHouse) {
        if (action.size() != 2) {
            System.out.println("Wrong action");
        } else {
            if (inTheWizardHouse) {
                switch (action.get(1).toLowerCase().trim()) {
                    case "heal" -> board.getWizard().heal(board.getPlayer());
                    case "enchant" -> board.getWizard().enchant(board.getPlayer());
                    default -> System.out.println("Wrong action");
                }
            } else {
                System.out.println("You are not in the wizard's house");
            }
        }
    }

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
            if (board.getPlayer().getCoord() == board.getWizard().getCoord())
                System.out.println("You found Intelli Jee's house");
        }
    }


    /*
     * =======================================================
     *
     *               Save Game Output
     *
     * =======================================================
     * */

    private void gameOutput(StringBuilder logs) {
        if (logs != null) {
            System.out.println("Enter a name for the log files");
            String filename = this.scanner.nextLine();
            if (filename.isEmpty()) {
                filename = "logs";
            }
            this.outputJson(filename);
            File file = new File(filename + ".txt");
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write(logs.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
                new OutputDTO.Coord(board.getWizard().getCoord().getX(),
                        board.getWizard().getCoord().getY()),
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
        Wizard wizard = new Wizard(this.getCoord(possibleCoord));
        this.board = new Board(size, player, wizard);

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
                new BoardDTO.Coord(this.board.getWizard().getCoord().getX(),
                        this.board.getWizard().getCoord().getY());
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
        ArrayList<Coord> possibleCoord = getPossibleCoords(size);
        Coord coord = new Coord(boardDTO.getWizardCoord().getX(), boardDTO.getWizardCoord().getY());
        if (possibleCoord.contains(coord)) {
            possibleCoord.remove(coord);
        } else {
            System.err.println("Already used coord");
            exit(1);
        }
        this.board = new Board(size, new Player(new Coord(boardDTO.getPlayerCoord().getX(), boardDTO.getPlayerCoord().getY())), new Wizard(coord));
    }
    /*
     * =======================================================
     *                    COMMON HELPERS
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