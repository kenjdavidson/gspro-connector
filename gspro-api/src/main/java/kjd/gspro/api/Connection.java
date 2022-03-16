package kjd.gspro.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static final String DEFAULT_HOST = "127.0.0.1";
    public static final Integer DEFAULT_PORT = 921;
    
    private final Logger logger = LoggerFactory.getLogger(Connection.class);
    private final Object[] $CONNECTION_LOCK = new Object[0];

    private String host;
    private Integer port;
    private Socket socket;
    private boolean connected;
    private Optional<ConnectionListener> listener;
    private volatile boolean cancelled;

    public Connection(ConnectionListener listener) {
        this(DEFAULT_HOST, DEFAULT_PORT, listener);
    }

    public Connection(String host, Integer port) {
        this(host, port, null);
    }

    public Connection(String host, Integer port, ConnectionListener listener) {
        this.connected = false;
        this.cancelled = false;

        this.host = host;
        this.port = port;
        this.listener = Optional.ofNullable(listener);
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
        this.cancelled = true;

        safeClose();
        
        this.connected = false;
    }

    @Synchronized("$CONNECTION_LOCK")
    public void write(Request request) throws IOException {
        socket.getOutputStream().write(Json.writeValue(request).getBytes());
    }        

    @Synchronized("$CONNECTION_LOCK")
    public void addListener(ConnectionListener listener) {
        this.listener = Optional.ofNullable(listener);
    }

    @Synchronized("$CONNECTION_LOCK")
    public void clearListener() {
        this.listener = Optional.empty();
    }

    @Override
    public void run() {                   
        try {
            connect();

            InputStream is = socket.getInputStream();
            StringBuilder sb = new StringBuilder();  
            int stack = 0;

            while (!cancelled) {    
                int read;           

                // Continue reading until we run out of bytes. 
                // While reading we want to check to see that we're getting '{'' and '}', once we get a matching count
                // attempt to parse and send the Status.
                // Finally reset the string and stack so that on the next read we start at the first next '{' 
                while ((read = is.read()) > -1) {
                    sb.append(String.valueOf((char) read));
                    
                    if (read == '{') stack++;
                    else if (read == '}') stack--;

                    if (stack == 0) {                                                                
                        try {
                            Status status = Json.readValue(sb.toString(), Status.class);
                            sb.delete(0, sb.length());
                            stack = 0;

                            logger.debug("Received message from GS Pro: {}", status.getMessage());
                            listener.ifPresent(l -> l.onStatus(status));
                        } catch (JsonProcessingException e) {
                            error("Cannot process message", e);
                        }  
                        
                        break;    
                    }
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
            logger.debug("Disconnected from GS Pro, canceled: {}", cancelled);
            listener.ifPresent(l -> l.onDisconnect(Status.disconnected()));
        }
    } 

    /**
     * If not already connected, attempt to create the connection.
     * 
     * @throws UnknownHostException
     * @throws IOException
     */
    void connect() throws UnknownHostException, IOException { 
        if (!connected) {
            socket = createSocket(host, port);        
            connected = true;

            logger.debug("Successfully connected to GS Pro");
            listener.ifPresent(l -> l.onStatus(Status.connected()));
        }
    }

    private void safeClose() {
        try { 
            if (socket != null) {
                socket.close(); 
            }                    
        } catch(IOException e) { 
            logger.error("Error while attempting to close", e);
        }
    }

    private void safeSleep(long sleep) {
        try { 
            sleep(sleep); 
        } catch(InterruptedException e) { 
            logger.error("Connection thread interupted, continuing...", e); 
        }      
    }

    private void error(String message, Throwable t) {
        logger.error(message, t);

        Status error = Status.builder().code(504).message(message).build();
        listener.ifPresent(l -> l.onError(error, t));
    }
}
