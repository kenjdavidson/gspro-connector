package kjd.gspro.adapter;

/**
 * Provides the interface required by the bridge in order to implement the connection between
 * a launch monitor and the GS Pro Connect Api.
 * 
 * @author kenjdavidson
 */
public interface LaunchMonitor {
    
    /**
     * Connect to the launch montior.
     * 
     * @return whether the connection was successful
     */
    boolean connect();

    /**
     * Disconnect from the launch monitor.
     * 
     * @return whether the disconnect was successful
     */
    boolean disconnect();
}
