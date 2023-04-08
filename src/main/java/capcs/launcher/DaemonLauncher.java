package capcs.launcher;
import java.lang.String;
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

        do {
            System.out.print("$ > ");
            command = scanner.nextLine();

            switch (command) {
                case "stop" -> stop();
                case "start" -> start();
                case "restart" -> restart();
                case "status" -> System.out.println("Daemon is " + (isRunning() ? "running" : "stopped"));
                case "exit" -> System.out.println("Program terminated");
                case "help" -> System.out.println("Available commands: start, stop, restart, status, exit");
                default -> System.out.println("Unknown command");
            }
        } while (!command.equals("exit"));
        System.exit(0);
    }




    // MARK: - Methods


    /**
     * Start the daemon
     */
    public void start() {
        running = true;
        System.out.println("Daemon started");
    }

    /**
     * Stop the daemon
     */
    public void stop() {
        running = false;
        System.out.println("Daemon stopped");
    }

    /**
     * Check if the daemon is running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Restart the daemon
     */
    public void restart() {
        if(running){
            stop();
            start();
            System.out.println("Daemon restarted");
        }
        else System.out.println("Daemon is not running");
    }

    /**
     * Read the command Line to create the adequate listener
     */
}




