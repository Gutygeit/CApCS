package capcs.launcher;

import java.util.Scanner;

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
        String command;

        while (isRunning()) {
            System.out.print("$ > ");
            command = scanner.nextLine();

            switch (command) {
                case "stop" -> stop();
                case "status" -> System.out.println("Daemon is " + (isRunning() ? "running" : "stopped"));
                case "help" -> System.out.println("Available commands: stop, status, exit");
                default -> System.out.println("Unknown command");
            }
        }

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


}




