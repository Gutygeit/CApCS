package capcs.models;

import java.util.List;

public class TreeDiff {

    /**
     * List of documents that have been added
     */
    private List<Document> addedDocuments;

    /**
     * List of documents that have been removed
     */
    private List<Document> removedDocuments;

    /**
     * Constructor
     * @param addedDocuments List of documents that have been added
     * @param removedDocuments List of documents that have been removed
     */
    public TreeDiff(List<Document> addedDocuments, List<Document> removedDocuments) {
        this.addedDocuments = addedDocuments;
        this.removedDocuments = removedDocuments;
    }

    /**
     * Get the list of documents that have been added
     * @return List of documents that have been added
     */
    public List<Document> getAddedDocuments() {
        return addedDocuments;
    }

    /**
     * Get the list of documents that have been removed
     * @return List of documents that have been removed
     */
    public List<Document> getRemovedDocuments() {
        return removedDocuments;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        addedDocuments.forEach(document -> sb.append("+" + document.getPath() + document.getName() + " (" + document.getDate() + ")" + "\n"));
        removedDocuments.forEach(document -> sb.append("-" + document.getPath() + document.getName() + " (" + document.getDate() + ")" + "\n"));
        return sb.toString();
    }
    
}
