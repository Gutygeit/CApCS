package capcs.models;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ListenerWithStream {

    /**
     * Listener
     */
    private TreeListener listener;

    /**
     * Document
     */
    private Document document;

    /**
     * Input Stream
     */
    private InputStream stream;

    /**
     * Optional Output Stream
     */
    private OutputStream outputStream;

    /**
     * Constructor
     * @param listener Listener
     * @param document Document
     * @param stream Stream
     */
    public ListenerWithStream(TreeListener listener, Document document, InputStream stream) {
        this.listener = listener;
        this.document = document;
        this.stream = stream;
    }

    /**
     * Constructor
     * @param listener Listener
     * @param document Document
     * @param stream Stream
     * @param outputStream Output Stream
     */
    public ListenerWithStream(TreeListener listener, Document document, InputStream stream, OutputStream outputStream) {
        this.listener = listener;
        this.document = document;
        this.stream = stream;
        this.outputStream = outputStream;
    }

    /**
     * Getter for listener
     * @return Listener
     */
    public TreeListener getListener() {
        return listener;
    }

    /**
     * Getter for stream
     * @return Stream
     */
    public InputStream getStream() {
        return stream;
    }

    /**
     * Getter for output stream
     * @return Output Stream
     */
    public OutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * Attach the stream to the listener for the fileReceived event
     * @throws IOException
     */
    public void attachStreamToListener() throws IOException {
        listener.fileReceived(document, stream);
    }
    
}
