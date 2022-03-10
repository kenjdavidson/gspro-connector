package kjd.gspro.api;

import java.io.IOException;
import java.util.concurrent.Flow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

/**
 * {@link Client} implementation for communication with the GS Pro Open Connect V1 protocol.
 * <p>
 * 
 * @author kenjdavidson
 */
public class Client implements Flow.Publisher<Status> {
    
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

    @Getter
    private Publisher<Status> statusPublisher;

    public Client() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    public Client(String host) {
        this(host, DEFAULT_PORT);
    }

    public Client(String host, Integer port) {
        this(host, port, DEFAULT_TIMEOUT);
    }

    public Client(String host, Integer port, Integer timeout) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;

        this.statusPublisher = new Publisher<>();
    }

    /**
     * Whether we have an active connection to the GS Pro Open Connect V1 server.
     * 
     * @return
     */
    public boolean isConnected() {
        return connection != null && connection.isConnected();
    }

    /**
     * Request a connection to the GS Pro Open Connect V1 server.  The client must be subscribed
     * to in order to get status updates and responses.
     * 
     * @return the {@link ClientTest} once the connection is started
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
     * @return the {@link ClientTest}
     * @throws InterruptedException
     */
    public void disconnect() throws ConnectionException {
        validateConnection();

        connection.disconnect();
        connection = null;
    }    

    /**
     * Subscription to start receiving Status (and response) messages from the 
     * GS Pro software.
     * 
     * @param subscriber Status subscriber
     */
    @Override
    public void subscribe(Flow.Subscriber<? super Status> subscriber) {
        statusPublisher.subscribe(subscriber);
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
            statusPublisher.error(e);
        }

        return false;
	}  

    /**
     * Creates/returns the {@link Connection}.
     * 
     * @return the {@link Connection} being used.
     */
    protected Connection createConnection() {
        return new Connection(host, port, statusPublisher);
    }

    /**
     * Ensure that we have a valid connection.
     * 
     * @throws ConnectionException
     */
    protected void validateConnection() throws ConnectionException {
        if (connection == null) 
            throw new ConnectionException("Not connected to GS Pro");
    }

}
