package capcs.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

import capcs.launcher.Launcher;

public class NetworkManagerTest {

    private final long DELAY = 2000;

    @Test
    public void fileChanged() {
        try {
            String source = Files.createTempDirectory("source").toFile().getAbsolutePath();
            String target = Files.createTempDirectory("target").toFile().getAbsolutePath();
            int port = 49152;

            Launcher launcherServer = new Launcher();
            launcherServer.addListener(new DirectoryManager(source));
            launcherServer.addListener(new NetworkManager(null, port));
            Launcher launcherClient = new Launcher();
            launcherClient.addListener(new DirectoryManager(target));
            launcherClient.addListener(new NetworkManager("127.0.0.1", port));

            Files.writeString(Path.of(source, "test.txt"), "test");
            Thread.sleep(DELAY);

            assertTrue(new File(target, "test.txt").exists());
            assertEquals("test", Files.readString(Path.of(target, "test.txt")));

            Files.writeString(Path.of(source, "test.txt"), "edited");
            Thread.sleep(DELAY);

            assertEquals("edited", Files.readString(Path.of(target, "test.txt")));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fileChangedSubfolder() {
        try {
            String source = Files.createTempDirectory("source").toFile().getAbsolutePath();
            String target = Files.createTempDirectory("target").toFile().getAbsolutePath();
            int port = 49153;

            Launcher launcherServer = new Launcher();
            launcherServer.addListener(new DirectoryManager(source));
            launcherServer.addListener(new NetworkManager(null, port));
            Launcher launcherClient = new Launcher();
            launcherClient.addListener(new DirectoryManager(target));
            launcherClient.addListener(new NetworkManager("127.0.0.1", port));

            Files.createDirectories(Path.of(source, "subfolder"));
            Files.writeString(Path.of(source, "subfolder/test.txt"), "test");
            Thread.sleep(DELAY);

            assertEquals("test", Files.readString(Path.of(target, "subfolder/test.txt")));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fileDeleted() {
        try {
            String source = Files.createTempDirectory("source").toFile().getAbsolutePath();
            String target = Files.createTempDirectory("target").toFile().getAbsolutePath();
            int port = 49154;

            Launcher launcherServer = new Launcher();
            launcherServer.addListener(new DirectoryManager(source));
            launcherServer.addListener(new NetworkManager(null, port));
            Launcher launcherClient = new Launcher();
            launcherClient.addListener(new DirectoryManager(target));
            launcherClient.addListener(new NetworkManager("127.0.0.1", port));

            Files.writeString(Path.of(source, "deleted.txt"), "test");
            Thread.sleep(DELAY);

            assertTrue(new File(target, "deleted.txt").exists());

            Files.delete(Path.of(source, "deleted.txt"));
            Thread.sleep(DELAY);

            assertTrue(!new File(target, "deleted.txt").exists());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
