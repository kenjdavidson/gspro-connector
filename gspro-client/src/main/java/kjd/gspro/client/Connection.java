package kjd.gspro.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kjd.gspro.api.Json;
import kjd.gspro.api.Publisher;
import kjd.gspro.api.Request;
import kjd.gspro.api.Status;
import lombok.Synchronized;

/**
 * Manages the connection and communication functionality to the GS Pro socket.  At this point we 
 * just read data until we don't get anymore and then send the data to the Client for parsing.  
 * At this point I'm not sure whether there is a delimiter or any way to tell when a message
 * has been completed (hopefully {@code \n}).
 * <p>
 * From the examples I've seen there are some different ways to handle:
 * <ul>
 *  <li>Just logging whatever comes to the console</li>
 *  <li>If the data contains the same number of { and } it assumes full message</li>
 *  <li>Getting char[8192] and assuming it's a full message</li>
 *  <li>At this point I'm assuming that they send one at a time with an accepted delimiter</li>
 * </ul>
 * The connection and reading is done within the Thread so this does not require two steps
 * of {@code connect} then {@code start}.
 * 
 * @author kenjdavidson
 */
public class Connection extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(Connection.class);

    private final Object[] $CONNECTION_LOCK = new Object[0];

    private String host;
    private Integer port;
    private Publisher<Status> publisher;
    private Socket socket;
    private boolean connected;
    private volatile boolean cancelled;

    public Connection(String host, Integer port, Publisher<Status> publisher) {
        this.connected = false;
        this.cancelled = false;

        this.host = host;
        this.port = port;
        this.publisher = publisher;
    }

    @Synchronized("$CONNECTION_LOCK")
    public boolean isConnected() {
        return connected;
    }

    protected Socket createSocket(String host, Integer port) throws UnknownHostException, IOException {
        return new Socket(host, port);
    }

    @Synchronized("$CONNECTION_LOCK")
    public void disconnect() {
        safeClose();
        this.cancelled = true;
        this.connected = false;
    }

    @Synchronized("$CONNECTION_LOCK")
    public void write(Request request) throws IOException {
        socket.getOutputStream().write(Json.writeValue(request).getBytes());
    }        

    @Override
    public void run() {                   
        try {
            connect();

            while (!cancelled) {
                InputStream is = socket.getInputStream();

                StringBuilder sb = new StringBuilder();                
                byte[] data = new byte[4096];

                while (is.read(data) > 0) {
                    sb.append(String.valueOf(data));
                    parse(sb).ifPresent(publisher::publish);
                }

                safeSleep(100l);
            }                        
        } catch(UnknownHostException e) {
            error("Unable to connect to GS Pro Connect Api", e);
        } catch (IOException e) {
            if (!cancelled) {
                error("Error during communication, dropping connection", e);
            }            
        } finally {     
            safeClose();
            connected = false;
            publisher.complete();            
        }
    }

    /**
     * Attempt to parse the current data (there are different implementations of this, so this is using
     * the safest one).  This moves through the data looking for '{' and '}' and when they match we've 
     * hit the end of a full JSON object, so we attempt to send it.
     * <p>
     * This can get updated if it turns out the api uses delimiters or standard message sizes.
     * 
     * @param data {@link StringBuilder} containing the currently buffered data
     * @return {@link Optional} {@link Status} from GSPro 
     */
    Optional<Status> parse(StringBuilder data) {
        Optional<Status> status = Optional.empty();

        int[] stack = new int[2];
        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) == '{') 
                stack[0]++;
            else if (data.charAt(i) == '}') 
                stack[1]++;

            if (stack[0] == stack[1]) {                
                String json = data.substring(0, i+1);
                data.delete(0, i+1);

                try {
                    status = Optional.of(Json.readValue(json, Status.class));
                } catch (JsonProcessingException e) {
                    error("Cannot process message", e);
                }    

                break;
            }
        }

        return status;
    }    

    /**
     * If not already connected, attempt to create the connection.
     * 
     * @throws UnknownHostException
     * @throws IOException
     */
    void connect() throws UnknownHostException, IOException { 
        if (!connected) {
            logger.debug("Attempting connection to GS Pro @ {}:{}", host, port);
            publisher.publish(Status.connecting());

            socket = createSocket(host, port);        
            connected = true;

            logger.debug("Successfully connected to GS Pro");
            publisher.publish(Status.connected());
        }
    }

    /**
     * Safely close the socket.
     * 
     * @param closable
     */
    private void safeClose() {
        try { 
            if (socket != null) {
                socket.close(); 
            }                    
        } catch(IOException e) { 
            logger.error("Error while attempting to close", e);
        }
    }

    /**
     * Sleep handling any interupts accordingly.
     * 
     * @param sleep milliseconds to sleep
     */
    private void safeSleep(long sleep) {
        try { 
            sleep(sleep); 
        } catch(InterruptedException e) { 
            logger.error("Connection thread interupted, continuing...", e); 
        }      
    }

    /**
     * Log and publish error
     * 
     * @param message
     * @param t
     */
    private void error(String message, Throwable t) {
        logger.error(message, t);
        publisher.error(t);
    }
}
