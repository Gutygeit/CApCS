package capcs.launcher;

import capcs.io.DirectoryManager;
import capcs.io.NetworkManager;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class DaemonLauncher extends Launcher {

    // MARK: - Properties

    /**
     * Daemon execution in background
     */
    private boolean running = true;

    // MARK: - Constructor

    /**
     * Command interface
     */
    public DaemonLauncher() {
        System.out.println("Welcome to the CApCS daemon. Daemon is now running, type 'help' for more information.");
        Scanner scanner = new Scanner(System.in);

        while (isRunning()) {
            System.out.print("$ > ");
            String command = scanner.nextLine();
            StringTokenizer keyWords = new StringTokenizer(command);

            try {
                switch (keyWords.nextToken()) {
                    case "stop" -> stop();
                    case "add" -> add(keyWords);
                    case "status" -> System.out.println("Daemon is " + (isRunning() ? "running" : "stopped"));
                    case "help" ->
                            System.out.println("Available commands: stop, status, exit, add folder <path>, add client <ip> <port>, add server <port>");
                    default -> System.out.println("Unknown command");
                }
            } catch (Exception e) {
                System.out.println("Unknown command, or missing arguments");
            }
        }
        scanner.close();

    }

    // MARK: - Methods

    /**
     * Stop the daemon
     */
    public void stop() {
        running = false;
        System.out.println("Daemon stopped");
        System.exit(0);
    }

    /**
     * Check if the daemon is running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Use the addListener method from the Launcher class in the DaemonLauncher class
     */
    public void add(StringTokenizer k) throws NoSuchElementException {
        String instruct = k.nextToken();
        System.out.println("Adding " + instruct);
        switch (instruct) {
            case "folder" -> {
                String path = k.nextToken();
                addListener(new DirectoryManager(path));
                System.out.println("Listener added in " + path);
            }
            case "client" -> {
                try {
                    new NetworkManager(k.nextToken(), Integer.parseInt(k.nextToken()));
                    System.out.println("Client added");
                } catch (NumberFormatException e) {
                    System.out.println("Port must be an integer");
                    System.out.println("Client not added");
                }
            }
            case "server" -> {
                try {
                    new NetworkManager(null, Integer.parseInt(k.nextToken()));
                    System.out.println("Server added");
                } catch (NumberFormatException e) {
                    System.out.println("Port must be an integer");
                    System.out.println("Server not added");
                }

            }
            default -> System.out.println("Unknown type");
        }
    }

}




