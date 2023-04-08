package capcs.models;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class TreeListener {

    // MARK: - Properties

    /**
     * List of documents forming the tree locally
     */
    protected List<Document> currentVersion = List.of();

    /**
     * List of listeners
     */
    protected List<TreeListener> listeners = new ArrayList<>();

    // MARK: - Methods

    /**
     * Called when a file has been requested
     * @param document Document that has been requested
     * @return Stream of the file
     */
    public abstract InputStream fileRequested(Document document) throws IOException;

    /**
     * Called when a file has been received
     * @param document Document that has been received
     * @param stream Stream of the file
     */
    public abstract void fileReceived(Document document, InputStream stream) throws IOException;

    /**
     * Called when a file has been deleted
     * @param document Document that has been deleted
     */
    public abstract void fileDeleted(Document document) throws IOException;

    /**
     * Register a listener
     * @param listener Listener to register
     */
    public void registerListener(TreeListener listener) {
        listeners.add(listener);
    }

    /**
     * Compare the current version of the tree with the new one
     * @param documents List of documents forming the tree
     * @return List of documents that have changed
     */
    public TreeDiff compareWithCurrentVersion(List<Document> documents) {
        if (currentVersion == null) {
            return new TreeDiff(List.of(), List.of());
        }
        return new TreeDiff(
            documents.stream().filter(doc -> !currentVersion.contains(doc)).toList(),
            currentVersion.stream().filter(doc -> !documents.contains(doc)).toList()
        );
    }

    /**
     * Called when the tree has changed
     * @param documents List of documents forming the tree
     */
    public void treeChanged(List<Document> documents) {
        synchronized (currentVersion) {
            TreeDiff diff = compareWithCurrentVersion(documents);
            diff.getRemovedDocuments().stream().forEach(doc -> {
                try {
                    fileDeleted(doc);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            diff.getAddedDocuments().stream().forEach(doc -> {
                InputStream stream = requestFile(doc);
                if (stream == null) {
                    return;
                }
                try {
                    fileReceived(doc, stream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private InputStream requestFile(Document document) {
        for (TreeListener listener : listeners) {
            try {
                return listener.fileRequested(document);
            } catch (IOException e) {
                // Do nothing, we just know that this friend doesn't have the file
            }
        }
        return null;
    }
    
}
