package fr.epita.assistant;

public class Main {

    public static void main(String[] args) {
        String commandFile = "";
        String boardFile = "";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-c")) {
                if (i + 1 < args.length) {
                    commandFile = args[i + 1];
                    System.out.println("Command file: " + commandFile);
                } else {
                    System.err.println("No command file specified");
                    return;
                }
            }
            if (args[i].equals("-b")) {
                if (i + 1 < args.length) {
                    boardFile = args[i + 1];
                } else {
                    System.err.println("No board file specified");
                    return;
                }
            }
            if (args[i].equals("-h")) {
                System.out.println(
                        "Usage: java .jar [-c <command file>] [-b <board file>]");
                return;
            }
        }
        new GameEngine(boardFile, commandFile).play();
    }
}