package capcs.models;

public class Document {

    // MARK: - Properties

    /**
     * Path of the file, without the name
     */
    private String path;

    /**
     * Name of the file, with its extension
     */
    private String name;

    /**
     * Last modification date of the file,
     * in milliseconds since January 1, 1970, 00:00:00 GMT
     */
    private long date;

    /**
     * Size of the file, in bytes
     */
    private long size;

    // MARK: - Constructor

    /**
     * Constructor
     * @param path Path of the file, without the name, ending by a slash
     * @param name Name of the file, with its extension
     * @param date Last modification date of the file
     * @param size Size of the file, in bytes
     */
    public Document(String path, String name, long date, long size) {
        this.path = path;
        this.name = name;
        this.date = date;
        this.size = size;

        if (!this.path.endsWith("/")) {
            this.path += "/";
        }
    }

    // MARK: - Getters

    /**
     * Get the path of the file
     * @return Path of the file, without the name
     */
    public String getPath() {
        return path;
    }

    /**
     * Get the name of the file
     * @return Name of the file, with its extension
     */
    public String getName() {
        return name;
    }

    /**
     * Get the last modification date of the file
     * @return Last modification date of the file
     */
    public long getDate() {
        return date;
    }

    /**
     * Get the size of the file
     * @return Size of the file, in bytes
     */
    public long getSize() {
        return size;
    }

    // MARK: - Object

    @Override
    public String toString() {
        return path + name + " (" + date + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Document)) {
            return false;
        }
        Document document = (Document) obj;
        return document.path.equals(path) && document.name.equals(name) && document.date == date;
    }

    @Override
    public int hashCode() {
        return path.hashCode() + name.hashCode() + (int) date;
    }
    
}
