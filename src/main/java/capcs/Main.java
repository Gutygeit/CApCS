package capcs;

import capcs.launcher.DaemonLauncher;
import capcs.launcher.UiLauncher;

public class Main {

    public static void main(String[] args) {
        new DaemonLauncher();
        new UiLauncher();
    }

}