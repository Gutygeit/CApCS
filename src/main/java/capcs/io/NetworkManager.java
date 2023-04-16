package capcs.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import capcs.models.Document;
import capcs.models.ListenerWithStream;
import capcs.models.TreeListener;

/**
 * Create a socket (client or server),
 * to receive events from the other side
 * and forward our events to the other side.
 * For this, we will use a packet system:
 * - The first byte will be the type of the packet
 * - The next bytes will be the content of the packet
 * Packet types:
 * - 0: File requested
 * - 1: File received
 * - 2: File deleted
 */
public class NetworkManager extends TreeListener {

    // MARK: - Constants

    /**
     * Packet type: File requested
     */
    private static final int PACKET_TYPE_FILE_REQUESTED = 0;

    /**
     * Packet type: File received
     */
    private static final int PACKET_TYPE_FILE_RECEIVED = 1;

    /**
     * Packet type: File deleted
     */
    private static final int PACKET_TYPE_FILE_DELETED = 2;

    /**
     * Packet type: File requested reply
     */
    private static final int PACKET_TYPE_FILE_REQUESTED_REPLY = 3;

    // MARK: - Properties

    /**
     * Address of the other side, or null if we are the server
     */
    private String address;

    /**
     * Port of the other side, or the port we will listen to
     */
    private int port;

    /**
     * Socket to communicate with the other side
     */
    private Socket socket;

    /**
     * Server socket, in case we are the server
     */
    private ServerSocket serverSocket;

    /**
     * Input stream of the socket
     */
    private DataInputStream inputStream;

    /**
     * Output stream of the socket
     */
    private DataOutputStream outputStream;

    // MARK: - Constructor

    /**
     * Create a new NetworkManager
     * @param address Address of the other side, or null if we are the server
     * @param port Port of the other side, or the port we will listen to
     */
    public NetworkManager(String address, int port) {
        this.address = address;
        this.port = port;

        new Thread(() -> {
            try {
                createSocket();
                while (true) {
                    // Read packet type
                    int type = inputStream.read();
                    if (type == -1) {
                        break;
                    }

                    // Handle packet
                    switch (type) {
                        case PACKET_TYPE_FILE_REQUESTED -> handleFileRequested();
                        case PACKET_TYPE_FILE_RECEIVED -> handleFileReceived();
                        case PACKET_TYPE_FILE_DELETED -> handleFileDeleted();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    closeSocket();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // MARK: - Getters

    /**
     * Get the address of the other side
     * @return Address of the other side
     */
    public String getAddress() {
        return address;
    }

    /**
     * Get the port of the other side
     * @return Port of the other side
     */
    public int getPort() {
        return port;
    }

    // MARK: - TreeListener

    @Override
    public InputStream fileRequested(Document document) throws IOException {
        // Write packet document
        outputStream.write(PACKET_TYPE_FILE_REQUESTED);
        outputStream.write(document.getPath().length());
        outputStream.write(document.getPath().getBytes());
        outputStream.write(document.getName().length());
        outputStream.write(document.getName().getBytes());
        outputStream.writeLong(document.getDate());
        outputStream.writeLong(document.getSize());

        // Give our stream which has the file content
        return inputStream;
    }

    @Override
    public void fileReceived(Document document, InputStream stream) throws IOException {
        // Save document in current version
        currentVersion = Stream.concat(
            currentVersion
                .stream()
                .filter(d -> !d.getName().equals(document.getName()) || !d.getPath().equals(document.getPath())),
            Stream.of(document)
        ).toList();

        // Write packet document
        outputStream.write(PACKET_TYPE_FILE_RECEIVED);
        outputStream.write(document.getPath().length());
        outputStream.write(document.getPath().getBytes());
        outputStream.write(document.getName().length());
        outputStream.write(document.getName().getBytes());
        outputStream.writeLong(document.getDate());
        outputStream.writeLong(document.getSize());
        
        // Write packet content (file)
        byte[] buffer = new byte[1024];
        long left = document.getSize();
        int length;
        while ((length = stream.read(buffer)) != -1) {
            if (length > left) {
                length = (int) left;
            }
            outputStream.write(buffer, 0, length);
            left -= length;
            if (left == 0) {
                break;
            }
        }
    }

    @Override
    public void fileDeleted(Document document) throws IOException {
        // Save document in current version
        currentVersion = currentVersion
            .stream()
            .filter(d -> !d.getName().equals(document.getName()) || !d.getPath().equals(document.getPath()))
            .toList();

        // Write packet document
        outputStream.write(PACKET_TYPE_FILE_DELETED);
        outputStream.write(document.getPath().length());
        outputStream.write(document.getPath().getBytes());
        outputStream.write(document.getName().length());
        outputStream.write(document.getName().getBytes());
        outputStream.writeLong(document.getDate());
        outputStream.writeLong(document.getSize());
    }

    // MARK: - Network management

    private void createSocket() throws IOException {
        if (socket != null) {
            return;
        }
        if (address != null) {
            // Client socket
            socket = new Socket(address, port);
        } else {
            // Server socket
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
        }

        // Create streams
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
    }

    private void closeSocket() throws IOException {
        if (socket != null) {
            socket.close();
            socket = null;
        }
        if (serverSocket != null) {
            serverSocket.close();
            serverSocket = null;
        }
    }

    private void handleFileRequested() throws IOException {
        Document doc = new Document(
            new String(inputStream.readNBytes(inputStream.read())),
            new String(inputStream.readNBytes(inputStream.read())),
            inputStream.readLong(),
            inputStream.readLong()
        );

        InputStream stream = requestFile(doc);
        if (stream == null) {
            return;
        }

        // Write response
        outputStream.write(PACKET_TYPE_FILE_REQUESTED_REPLY);
        byte[] buffer = new byte[1024];
        long left = doc.getSize();
        int length;
        while ((length = stream.read(buffer)) != -1) {
            if (length > left) {
                length = (int) left;
            }
            outputStream.write(buffer, 0, length);
            left -= length;
            if (left == 0) {
                break;
            }
        }
    }

    private void handleFileReceived() throws IOException {
        Document doc = new Document(
            new String(inputStream.readNBytes(inputStream.read())),
            new String(inputStream.readNBytes(inputStream.read())),
            inputStream.readLong(),
            inputStream.readLong()
        );
        currentVersion = Stream.concat(
            currentVersion
                .stream()
                .filter(d -> !d.getName().equals(doc.getName()) || !d.getPath().equals(doc.getPath())),
            Stream.of(doc)
        ).toList();

        // Create streams for each listener, and write to them
        List<ListenerWithStream> streams = listeners.stream().map(l -> {
            try {
                PipedOutputStream out = new PipedOutputStream();
                InputStream in = new PipedInputStream(out);
                return new ListenerWithStream(l, doc, in, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).toList();
        byte[] buffer = new byte[1024];
        long left = doc.getSize();
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            if (length > left) {
                length = (int) left;
            }
            for (ListenerWithStream stream : streams) {
                stream.getOutputStream().write(buffer, 0, length);
            }
            left -= length;
            if (left == 0) {
                for (ListenerWithStream stream : streams) {
                    stream.attachStreamToListener();
                }
                break;
            }
        }
    }

    private void handleFileDeleted() throws IOException {
        Document doc = new Document(
            new String(inputStream.readNBytes(inputStream.read())),
            new String(inputStream.readNBytes(inputStream.read())),
            inputStream.readLong(),
            inputStream.readLong()
        );
        currentVersion = currentVersion
            .stream()
            .filter(d -> !d.getName().equals(doc.getName()) || !d.getPath().equals(doc.getPath()))
            .toList();
        listeners.forEach(l -> {
            try {
                l.fileDeleted(doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // MARK: - Object

    @Override
    public String toString() {
        return "NetworkManager[" + (address == null ? "server@" + port : "client@" + address + ":" + port) + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof NetworkManager)) {
            return false;
        }
        NetworkManager other = (NetworkManager) obj;
        return Objects.equals(address, other.address) && port == other.port;
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, port);
    }
    
}
