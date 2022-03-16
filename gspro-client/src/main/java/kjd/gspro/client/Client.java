package kjd.gspro.client;

import java.io.IOException;
import java.util.concurrent.Flow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kjd.gspro.api.Connection;
import kjd.gspro.api.ConnectionException;
import kjd.gspro.api.ConnectionListener;
import kjd.gspro.api.Request;
import kjd.gspro.api.Status;
import lombok.Getter;

/**
 * {@link Client} implementation for communication with the GS Pro Open Connect V1 protocol.
 * <p>
 * 
 * @author kenjdavidson
 * @deprecated probably don't need this level since we can handle events in application
 */
@Deprecated
public class Client implements Flow.Publisher<Status>, ConnectionListener {
    public static final String DEFAULT_HOST = "127.0.0.1";
    public static final Integer DEFAULT_PORT = 921;
    public static final Integer DEFAULT_TIMEOUT = 300;

    private Logger logger = LoggerFactory.getLogger(Client.class);

    @Getter
    private String host;

    @Getter
    private Integer port;

    @Getter 
    private Integer timeout;

    private Connection connection;

    private Publisher<Status> publisher;

    /**
     * Create a new {@link Client}.
     */
    public Client() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    /**
     * Create a new {@link Client}.
     * 
     * @param host hostname in which to connect
     */
    public Client(String host) {
        this(host, DEFAULT_PORT);
    }

    /**
     * Create a new {@link Client}.
     * 
     * @param host hostname in which to connect
     * @param port port in which to connect
     */
    public Client(String host, Integer port) {
        this(host, port, DEFAULT_TIMEOUT);
    }

    /**
     * Create a new {@link Client}.
     * 
     * @param host hostname in which to connect
     * @param port port in which to connect
     * @param timeout timeout is currently ignored
     */
    public Client(String host, Integer port, Integer timeout) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;

        this.publisher = new Publisher<>();
    }

    /**
     * Determine whether we have an active connection to the GS Pro Open Connect server.
     * 
     * @return whether connected
     */
    public boolean isConnected() {
        return connection != null && connection.isConnected();
    }

    /**
     * Request a connection to the GS Pro Open Connect V1 server.  In order to receive any information
     * regarding the active connection (other than using {@link #isConnected()}) the client should 
     * be subscribed.
     * 
     */
    public void connect() throws ConnectionException {
        if (isConnected()) 
            throw new ConnectionException("Client is already connected to the GS Pro Connect Api");

        connection = createConnection();
        connection.start();
    }

    /**
     * Requests that the client disconnects from the GS Pro Open Connect V1 server.  This may not happen
     * right away; the Subscriptions are still valid and should be monitored for successful disconnect.
     * <p>
     * Look into updating this so that cancel returns a `Future` or `join` with the connection thread
     * to make sure it's completely shut down.
     * 
     * @throws ConnectionException if the {@link Connection} cannot be established
     */
    public void disconnect() {
        if (connection.isConnected()) {
            connection.disconnect();
            connection = null;
        }
    }    

    /**
     * Subscription to start receiving Status (and response) messages from the 
     * GS Pro software.
     * 
     * @param subscriber a Subscriber that wishes to listen to events from the GS Pro
     */
    @Override
    public void subscribe(Flow.Subscriber<? super Status> subscriber) {
        publisher.subscribe(subscriber);
    }

    /**
     * Send a message to the GS Pro.  Requests should be pre-generated by the monitor
     * adapter/bridge either manually or using the {@link RequestBuilder}.
     * 
     * @param request the {@link Request} to send
     */
	public boolean send(Request request) throws ConnectionException {
        validateConnection();

        try {
            connection.write(request);
            return true;
        } catch (IOException e) {
            logger.error("Error sending to GS Pro: {0}", e.getMessage());
            publisher.error(e);
        }

        return false;
	}  

    /**
     * Creates a new {@link Connection} for use with the {@link Client}.  Extending classes can choose
     * to override this in order to provide custom {@link Connection} logic.
     * 
     * @return the {@link Connection} being used.
     */
    protected Connection createConnection() {
        return new Connection(host, port);
    }

    /**
     * Ensure that we have a valid connection.
     * 
     * @throws ConnectionException if the connection is null or disconnected
     */
    protected void validateConnection() throws ConnectionException {
        if (connection == null) 
            throw new ConnectionException("Not connected to GS Pro");
    }

    /**
     * {@link ConnectionListener} implementation for delegating messages.
     */
    @Override
    public void onStatus(Status status) {
        
        publisher.publish(status);
    }

    /**
     * {@link ConnectionListener} implementation for delegating errors.
     */
    @Override
    public void onError(Status error, Throwable t) {
        publisher.error(new GSProException(error.getMessage(), t));
    }

    @Override
    public void onConnected(Status status) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onDisconnect(Status status) {
        // TODO Auto-generated method stub
        
    }

}
