package capcs.io;

import java.io.IOException;
import java.io.InputStream;

import capcs.models.Document;
import capcs.models.TreeListener;

public class NetworkManager extends TreeListener {

    /*
     * TODO
     * Create a socket (client or server),
     * to receive events from the other side
     * and forward our events to the other side.
     * For this, we will use a packet system.
     */

    @Override
    public InputStream fileRequested(Document document) throws IOException {
        // TODO
        return null;
    }

    @Override
    public void fileReceived(Document document, InputStream stream) throws IOException {
        // TODO
    }

    @Override
    public void fileDeleted(Document document) throws IOException {
        // TODO
    }
    
}
