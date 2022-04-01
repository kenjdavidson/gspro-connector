package kjd.gspro.garmin.r10;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import kjd.gspro.data.Player;
import kjd.gspro.monitor.LaunchMonitor;

public class R10LaunchMonitor implements LaunchMonitor {

    private final Properties properties;
    private final Set<Listener> listeners;

    private ConnectionThread connectionThread;
    private ConnectedThread connectedThread;
    private Timer pingTimer;

    public R10LaunchMonitor(Properties properties) {
        this.properties = properties;        
        this.listeners = Collections.synchronizedSet(new HashSet<>());
    }

    /**
     * Creates a {@link java.net.ServerSocket} that listens for connections and creates SocketHandler 
     * that manages and forwards requests through to the listener (GS Pro).
     */
    @Override
    public void connect() {                
        clearConnection();
        clearConnected();
    
        int listenOn = Integer.parseInt(properties.getProperty("server.port", "2483"));
        connectionThread = new ConnectionThread(listenOn);
        connectionThread.start();
    }

    @Override
    public void disconnect() {
        clearConnection();
        clearConnected();

        pingTimer.cancel();
    }

    @Override
    public void notifyPlayer(Player player) {
        // Garmin R10 doesn't need to have the player updated in order to 
        // perform reading and firing of events.
    }

    @Override
    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    @Override
    public Listener removeListener(Listener listener) {
        this.listeners.remove(listener);
        return listener;
    }

    void clearConnection() {
        if (connectionThread != null) {
            connectionThread.cancel();
            connectionThread = null;
        }
    }

    void clearConnected() {
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }
    }

    void handleConnection(Socket socket) {        
        clearConnection();

        startPingTimer();
        

        listeners.stream().forEach(l -> l.onConnected());
    }

    void handleConnectionError(Exception e) {
        clearConnection();
        
        listeners.stream().forEach(l -> l.onDisconnected());                
    }

    void startPingTimer() {
        pingTimer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                
            }}, 1000, Integer.parseInt(properties.getProperty("server.ping", "10000")));
    }

    /**
     * Start a {@link ServerSocket} (cancellable) and wait for the Garmin R10 to connect.  This will result
     * in either passing the {@link Socket} to the launch monitor or firing off the event and resulting
     * in a disconnected state.
     * <p>
     * At this point the connection needs to be kicked off again.
     * 
     * @author kenjdavidson
     */
    private class ConnectionThread extends Thread {
        private ServerSocket serverSocket;
        private int port;

        public ConnectionThread(int port) {
            this.port = port;
        }

        public synchronized boolean cancel() {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                    serverSocket = null;                
                    return true;
                } catch (IOException e) {
                    // Ignored for now
                }
            }            

            return false;
        }

        @Override
        public void run() {
            try (ServerSocket ss = new ServerSocket(port)) {
                serverSocket = ss;

                Socket socket = serverSocket.accept();
                handleConnection(socket);
            } catch (IOException e) {
                handleConnectionError(e);
            } finally {
                serverSocket = null;
            }
        }
    }

    /**
     * Manages active connection to the R10 device.
     */
    private class ConnectedThread extends Thread {

        private Socket socket;
    
        public ConnectedThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (InputStream is = socket.getInputStream()) {
                while (!socket.isClosed()) {

                }
            } catch (IOException e) {
                handleConnectionError(e);
            }
        }
        
        public synchronized boolean cancel() {
            if (socket != null) {
                try {
                    socket.close();
                    socket = null;               
                    return true;
                } catch (IOException e) {
                    // Ignore the shut down exception and just return false
                    // This needs to be updated, but for now it should work
                }
            }            

            return false;
        }        
    }
}
