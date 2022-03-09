package kjd.gspro.api.v1;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kjd.gspro.api.ConnectionException;
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
 * Thread (or should be) safe.
 * 
 * @author kenjdavidson
 */
public class ConnectionImpl implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionImpl.class);

    private final Object[] $CONNECTION_LOCK = new Object[0];

    private Publisher<Status> publisher;

    private Socket socket;
    private BufferedReader is;
    private PrintWriter os;
    private boolean connected;
    private volatile boolean cancelled;

    public ConnectionImpl(Publisher<Status> publisher) {
        this.connected = false;
        this.cancelled = false;
        this.publisher = publisher;
    }

    @Synchronized("$CONNECTION_LOCK")
    public boolean isConnected() {
        return connected;
    }

    @Synchronized("$CONNECTION_LOCK")
    public boolean isCancelled() {
        return cancelled;
    }

    @Synchronized("$CONNECTION_LOCK")
    public ConnectionImpl connect(String host, Integer port) throws ConnectionException {
        if (socket != null && socket.isConnected()) {
            throw new ConnectionException("Already connected to server");
        }

        logger.debug("Attempting connection to GS Pro @ {}:{}", host, port);
        publisher.publish(Status.connecting());

        try {
            socket = createSocket(host, port);
            os = new PrintWriter(socket.getOutputStream(), true);
            is = new BufferedReader(new InputStreamReader(socket.getInputStream(), "ASCII"));
            
            connected = true;

            logger.debug("Successfully connected to GS Pro");
            publisher.publish(Status.connected());            
        } catch (IOException e) {
            safeClose(is);
            safeClose(os);
            safeClose(socket);            

            throw new ConnectionException(e);
        }        

        return this;
    }

    protected Socket createSocket(String host, Integer port) throws UnknownHostException, IOException {
        return new Socket(host, port);
    }

    @Synchronized("$CONNECTION_LOCK")
    public void cancel() {
        this.cancelled = true;

        safeClose(socket);
    }

    @Synchronized("$CONNECTION_LOCK")
    public void write(Request request) throws IOException {
        os.println(Json.writeValue(request));
    }        

    private void safeClose(Closeable closable) {
        try { 
            if (closable != null) {
                closable.close(); 
            }                    
        } catch(IOException e) { 
            logger.error("Unable to close {} {}", closable.toString(), e.getMessage());
        }
    }

    private void safeSleep(long sleep) {
        try { 
            Thread.sleep(100l); 
        } catch(InterruptedException e) { 
            logger.error("Connection thread interupted, continuing..."); 
        }      
    }

    @Override
    public void run() {                   
        try {
            logger.debug("Listening for GS Pro messages");

            while (!cancelled) {         
                if (is.ready()) {
                    String data = is.readLine();
                    logger.debug("Received data: {}", data.toString());
                    publisher.publish(Json.readValue(data, Status.class));
                }            
                
                safeSleep(100l)                    ;
            }                        
        } catch (IOException e) {
            if (!cancelled) {
                logger.error("Connection error: {}", e.getMessage());
                publisher.error(new ConnectionException(e));
            }            
        } finally {     
            connected = false;

            safeClose(socket);
            safeClose(is);
            safeClose(os);

            publisher.complete();            
        }
    }
}
