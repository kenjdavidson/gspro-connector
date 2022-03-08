package kjd.gspro.api;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscriber;

import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kjd.gspro.api.client.Publisher;
import lombok.Getter;
import lombok.Synchronized;

/**
 * The GS Pro client controls communication between the bridge and GS Pro software.
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

    private Publisher<Status> statusPublisher;

    public Client() {
        this(DEFAULT_HOST, DEFAULT_PORT);
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

    public boolean isConnected() {
        return connection != null && connection.isConnected();
    }

    public boolean connect() {
        connection = new Connection();
        return true;
    }

    public void cancel() {
        if (connection != null) {
            connection.cancel();
        }
    }    

    @Override
    public void subscribe(Flow.Subscriber<? super Status> subscriber) {
        this.statusPublisher.subscribe(subscriber);
    }

    /**
     * Manages the connection and communication functionality to the GS Pro socket.
     * 
     * @author kenjdavidson
     */
    private class Connection implements Runnable {
        private final Object[] $LOCK = new Object[0];

        private OutputStreamWriter os;
        private boolean connected;
        private volatile boolean cancelled;

        public Connection() {
            this.connected = false;
            this.cancelled = false;
        }

        @Synchronized("$LOCK")
        public boolean isConnected() {
            return connected;
        }

        @Synchronized("$LOCK")
        public boolean isCancelled() {
            return cancelled;
        }

        @Synchronized("$LOCK")
        public void cancel() {
            this.cancelled = true;
        }

        @Synchronized("$LOCK")
        public void write(String data) throws IOException {
            os.write(data);
        }

        @Override
        public void run() {                   
            logger.info("Attempting connection to {}:{}", host, port);

            try (Socket socket = new Socket();
                    InputStreamReader is = new InputStreamReader(socket.getInputStream(), Charset.forName("ASCII"));
                    OutputStreamWriter os = new OutputStreamWriter(socket.getOutputStream(), Charset.forName("ASCII"))) {
                socket.connect(new InetSocketAddress(host, port), timeout);
                this.connected = true;
                this.os = os;

                logger.info("Connection successful {}:{}", host, port);
                                
                while (!cancelled) {
                    StringBuilder data = new StringBuilder();
                    char c;

                    while ((c = (char) is.read()) != -1) {
                        data.append(String.valueOf(c));
                    }

                    logger.info("Received data: {}", data.toString());

                }                
            } catch (IOException e) {
                logger.error("Connection error {}:{} - {}", host, port, e.getMessage());
            }  
        }
    }   
}
