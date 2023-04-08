package capcs;

import capcs.io.DirectoryManager;
import capcs.launcher.Launcher;
import capcs.launcher.DaemonLauncher;

public class Main {

    public static void main(String[] args) {
        // TODO: Create the launcher in Deamon or UI, and allow to add listeners from there
        Launcher launcher = new DaemonLauncher();
        launcher.addListener(new DirectoryManager("data/src/"));
        launcher.addListener(new DirectoryManager("data/target/"));
    }

}