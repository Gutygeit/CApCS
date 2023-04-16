package capcs;

import java.util.Arrays;

import capcs.launcher.DaemonLauncher;
import capcs.launcher.UILauncher;

public class Main {

    public static void main(String[] args) {
        if (Arrays.asList(args).contains("--nogui")) {
            new DaemonLauncher();
            return;
        }
        new UILauncher();
    }

}