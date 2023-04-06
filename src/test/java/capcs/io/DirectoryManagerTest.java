package capcs.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

import capcs.launcher.Launcher;

public class DirectoryManagerTest {

    private final long DELAY = 2000;

    private String source;
    private String target;
    private Launcher launcher;

    public DirectoryManagerTest() {
        try {
            source = Files.createTempDirectory("source").toFile().getAbsolutePath();
            target = Files.createTempDirectory("target").toFile().getAbsolutePath();

            launcher = new Launcher();
            launcher.addListener(new DirectoryManager(source));
            launcher.addListener(new DirectoryManager(target));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fileChanged() {
        try {
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
