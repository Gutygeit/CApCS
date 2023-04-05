package capcs;

import capcs.filesystem.DirectoryManager;

public class Main {

    public static void main(String[] args) {
        DirectoryManager source = new DirectoryManager("data/src/");
        DirectoryManager target = new DirectoryManager("data/target/");

        source.registerListener(target);
        target.registerListener(source);
    }

}