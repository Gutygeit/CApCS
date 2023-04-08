package capcs.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import capcs.models.Document;
import capcs.models.TreeListener;

public class DirectoryManager extends TreeListener {

    // MARK: - Properties

    /**
     * Path of the directory
     */
    private String path;

    // MARK: - Constructor

    /**
     * Create a new DirectoryManager
     * @param path Path of the directory, ending with a slash
     */
    public DirectoryManager(String path) {
        this.path = path;
        if (!this.path.endsWith("/")) {
            this.path += "/";
        }
        try {
            Files.createDirectories(Path.of(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ugly (T-riz) way to watch for changes
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);

                    List<Document> newVersion = listContent("");
                    if (!newVersion.equals(currentVersion)) {
                        listeners.forEach(listener -> listener.treeChanged(newVersion));
                        currentVersion = newVersion;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // MARK: - TreeListener

    @Override
    public InputStream fileRequested(Document document) throws IOException {
        return new FileInputStream(path + document.getPath() + document.getName());
    }

    @Override
    public void fileReceived(Document document, InputStream stream) throws IOException {
        // Create folder if needed
        Files.createDirectories(
            Path.of(path, document.getPath())
        );

        // Write the file, with correct size
        FileOutputStream outputStream = new FileOutputStream(
            path + document.getPath() + document.getName(),
            false // Do not append, so replace the file
        );
        byte[] buffer = new byte[1024];
        long left = document.getSize();
        int length;
        while ((length = stream.read(buffer)) > 0) {
            if (length > left) {
                length = (int) left;
            }
            outputStream.write(buffer, 0, length);
            left -= length;
            if (left == 0) {
                break;
            }
        }
        outputStream.close();

        // Update date to match the one from the other side
        new File(
            path + document.getPath(), document.getName()
        ).setLastModified(document.getDate());
    }

    @Override
    public void fileDeleted(Document document) throws IOException {
        Files.deleteIfExists(Path.of(path, document.getPath(), document.getName()));
    }

    // MARK: - File management

    private List<Document> listContent(String path) {
        File file = new File(this.path + path);

        if (!file.exists()) {
            throw new IllegalArgumentException("Path does not exist");
        }
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("Path is not a directory");
        }
        return Arrays.asList(file.listFiles())
            .stream()
            .flatMap(runningFile -> {
                if (runningFile.isFile()) {
                    return List.of(new Document(
                        path,
                        runningFile.getName(),
                        runningFile.lastModified(),
                        runningFile.length()
                    )).stream();
                }
                return listContent(path + "/" + runningFile.getName()).stream();
            })
            .toList();
    }

    // MARK: - Object

    @Override
    public String toString() {
        return "DirectoryManager[path=" + path + "]";
    }
    
}
