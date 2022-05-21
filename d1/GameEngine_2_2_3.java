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
        for (; !isLastGame; this.gameOutput(logs)) {
            System.out.println("Let's play!");
            int turn = 1;
            logs = new StringBuilder();
            boolean inTheWizardHouse;
            boolean isMonsterHere;
            while (board.getPlayer().isAlive()) {
                isMonsterHere = board.getMonsters().containsKey(board.getPlayer().getCoord());
                inTheWizardHouse = board.getWizard().getCoord().equals(board.getPlayer().getCoord());
                System.out.println("===========");
                System.out.println("Turn " + turn);
                System.out.println("===========\n");
                System.out.println(board.printBoard());
                System.out.println("What do you wanna do ? To choose, type the first word of the choice you want");
                StringBuilder stringBuilder = (new StringBuilder()).append("-Move\t-Stats");
                if (isMonsterHere) {
                    stringBuilder.append("\t-Attack the monster");
                }
                if (inTheWizardHouse) {
                    stringBuilder.append("\t-Talk to Intelli Jee").append("\t-Attack Intelli Jee");
                }
                System.out.println(stringBuilder);
                String choice = this.scanner.nextLine().toLowerCase().trim();
                switch (choice) {
                    case "attack":
                        this.attackAction(isMonsterHere, inTheWizardHouse, logs);
                        break;
                    case "move":
                        this.moveAction(isMonsterHere, inTheWizardHouse, logs);
                        break;
                    case "talk":
                        if (inTheWizardHouse) {
                            this.talkToWizard(logs);
                        } else {
                            System.out.println("You are not in the Intelli Jee's house");
                        }
                        break;
                    case "stats":
                        this.stats(isMonsterHere, inTheWizardHouse, logs);
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
                this.removeDeadMonsters();
                if (board.getMonsters().isEmpty()) {
                    System.out.println("You won!");
                    break;
                }
            }
            System.out.println("Do you want to play again ? (y/n)");
            if (this.scanner.nextLine().toLowerCase().trim().equals("n")) {
                isLastGame = true;
            }
            if (isLastGame) {
                System.out.println("Bye!");
            } else {
                this.initializeBoard();
            }
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
        if (isMonsterHere) {
            Monster monster = board.getMonsters().get(board.getPlayer().getCoord());
            if (monster != null) {
                System.out.println("You attack the " + monster.getName());
                board.getPlayer().attack(monster);
                if (monster.isDead()) {
                    System.out.println("You killed the " + monster.getName());
                    board.getMonsters().remove(board.getPlayer().getCoord());
                } else {
                    boolean isMonsterTakingRevenge = random.nextInt(10) < 4;
                    if (isMonsterTakingRevenge) {
                        System.out.println("The " + monster.getName() + " is taking revenge");
                        monster.attack(board.getPlayer());
                        if (board.getPlayer().isDead()) {
                            System.out.println("You died");
                        }
                    }
                }
            }
        } else if (inTheWizardHouse) {
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
        if (isMonsterHere) {
            var monster = board.getMonsters().get(board.getPlayer().getCoord());
            if (monster != null) {
                System.out.println("The " + monster.getName() + " attacked you before you escaped");
                monster.attack(board.getPlayer());
                if (board.getPlayer().isDead()) {
                    System.out.println("You died");
                }
            }
        }
        if (board.getPlayer().isAlive()) {
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
            if (logs != null) logs.append(" ").append(direction);

            if (board.getPlayer().getCoord() == board.getWizard().getCoord()) {
                System.out.println("You found Intelli Jee's house");
            } else if (board.getMonsters().containsKey(board.getPlayer().getCoord())) {
                var monster = board.getMonsters().get(board.getPlayer().getCoord());
                if (monster != null) {
                    System.out.println("You meet a " + monster.getName());
                }
            }
        }
    }

    private void stats(boolean isMonsterHere, boolean inTheWizardHouse, StringBuilder logs) {
        System.out.println("Your stats are :");
        logs.append("Stats");
        board.getPlayer().printStats();
        if (isMonsterHere) {
            System.out.println("The monster's stats are :");
            board.getMonsters().get(board.getPlayer().getCoord()).printStats();
        }
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
            System.out.println("Let's play!");
            int turn = 1;
            while ((line = br.readLine()) != null) {
                System.out.println("===========");
                System.out.println("Turn " + turn);
                System.out.println("===========\n");
                System.out.println(board.printBoard());
                boolean isMonsterHere =
                        board.getMonsters().containsKey(board.getPlayer().getCoord());
                boolean isFoodHere =
                        board.getFoods().containsKey(board.getPlayer().getCoord());
                boolean inTheWizardHouse =
                        board.getWizard().getCoord() == board.getPlayer().getCoord();
                ArrayList<String> action =
                        new ArrayList<>(Arrays.asList(line.split(" ")));
                String choice = action.get(0).toLowerCase();
                switch (choice) {
                    case "move" -> moveFromCmd(action);
                    case "attack" -> attackAction(isMonsterHere, isFoodHere, logs);
                    case "talk" -> talkToWizardFromCmd(action, inTheWizardHouse);
                    case "stats" -> stats(isMonsterHere, inTheWizardHouse, logs);
                    default -> System.out.println("Wrong action");
                }
                turn++;
                this.removeDeadMonsters();
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
            else if (board.getMonsters().containsKey(board.getPlayer().getCoord())) {
                var monster = board.getMonsters().get(board.getPlayer().getCoord());
                if (monster != null) {
                    System.out.println("You meet a " + monster.getName());
                }
            }
        }
    }

    /*
     * =======================================================
     *               Common Helpers to play games
     * =======================================================
     * */

    private void removeDeadMonsters() {
        for (Map.Entry<Coord, Monster> entry :
                this.board.getMonsters().entrySet()) {
            if (entry.getValue().isDead()) {
                this.board.getMonsters().remove(entry.getKey());
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
        for (Map.Entry<Coord, Monster> entry : board.getMonsters().entrySet()) {
            monsters.add(new OutputDTO.Infos(entry.getKey().getX(), entry.getKey().getY(), entry.getValue().getName()));
        }
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
        maxElementOnTheBoard = size * size - 2;
        System.out.println(
                "What will be the number of monsters on the board?");
        System.out.format(
                "How many monsters do you want on the board ? You can have maximum %d monsters.\n",
                maxElementOnTheBoard);
        int numberOfMonsters =
                getNbElement(scanner, maxElementOnTheBoard, 1);
        int nbFood = 0;
        if (maxElementOnTheBoard != 0) {
            System.out.format(
                    "How many aliments do you want on the board ? You can have maximum %d aliments.\n",
                    maxElementOnTheBoard);
            nbFood = this.getNbElement(this.scanner, maxElementOnTheBoard, 0);
        }

        ArrayList<Coord> possibleCoord = getPossibleCoords(size);
        Player player = new Player(this.getCoord(possibleCoord));
        Wizard wizard = new Wizard(this.getCoord(possibleCoord));

        HashMap<Coord, Monster> monstersHashMap = new HashMap<>();
        for (int nbMonsterAdded = 0; nbMonsterAdded < numberOfMonsters;
             nbMonsterAdded++) {
            switch (this.random.nextInt(2)) {
                case 0 -> monstersHashMap.put(this.getCoord(possibleCoord), new Coatlin());
                case 1 -> monstersHashMap.put(this.getCoord(possibleCoord), new Skalah());
            }
        }
        this.board = new Board(size, player, wizard, monstersHashMap);

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
        for (Map.Entry<Coord, Monster> entry : this.board.getMonsters().entrySet()) {
            monsters.add(
                    new BoardDTO.Infos(entry.getKey().getX(), entry.getKey().getY(),
                            entry.getValue().getName()));
        }
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

        HashMap<Coord, Monster> monstersHashMap = new HashMap<>();
        for (BoardDTO.Infos monsterDTO :
                Objects.requireNonNull(boardDTO).getMonsters()) {
            Monster monster = null;
            String monsterType = monsterDTO.getType();
            if (monsterType != null) {
                switch (monsterType) {
                    case "Coatlin" -> monster = new Coatlin();
                    case "Skalah" -> monster = new Skalah();
                    default -> System.err.println("Invalid monster type");
                }
                Coord coord = new Coord(monsterDTO.getX(), monsterDTO.getY());
                if (possibleCoord.contains(coord)) {
                    possibleCoord.remove(coord);
                } else {
                    System.err.println("Already used coord");
                    exit(1);
                }
                monstersHashMap.put(coord, monster);
            }
        }
        Coord coord = new Coord(boardDTO.getWizardCoord().getX(), boardDTO.getWizardCoord().getY());
        if (possibleCoord.contains(coord)) {
            possibleCoord.remove(coord);
        } else {
            System.err.println("Already used coord");
            exit(1);
        }
        this.board = new Board(size, new Player(new Coord(boardDTO.getPlayerCoord().getX(), boardDTO.getPlayerCoord().getY())), new Wizard(coord), monstersHashMap);
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

    private int getNbElement(Scanner scanner, int maxElement, int lowerBound) {
        int nbElement = -1;
        do {
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (lowerBound <= input) {
                    if (maxElement >= input) {
                        nbElement = input;
                        continue;
                    }
                }
                System.out.println("Please enter a positive integer inferior or equal to " + maxElement + '.');
            } catch (NumberFormatException var7) {
                System.out.println("Please enter a positive integer inferior or equal to " + maxElement + '.');
            }
        } while (nbElement == -1);

        return nbElement;
    }

}