package kjd.gspro.api;

import java.util.concurrent.Flow;

/**
 * GS Pro Open Connect client.
 * 
 * @author kenjdavidson
 */
public interface Client {

    /**
     * Whether the {@link Client} is has an active connection to the
     * api.
     * 
     * @return whether there is an active connections
     */
    boolean isConnected();

    /**
     * Attempt to connect.  
     * 
     * @return the {@link Client} once a connect has been established
     * @throws ConnectionException if an error occurs during connection
     */
    Client connect() throws ConnectionException;
  
    /**
     * Attempt to cancel.
     * 
     * @return the {@link Client} once the request is completed
     * @throws ConnectionException if not currently connected
     */
    Client cancel() throws ConnectionException;

    /**
     * Subscribe to the client to receive {@link Status} updates from the
     * GS Pro.  Status messages include:
     * <ul>
     *  <li>Successful shot data</li>
     *  <li>Player change information</li>
     *  <li>Errors</li>
     *  <li>Connection Information</li>
     * </ul>
     * Note - that the connection information is not part of the Conenct API
     * but the client (just to help manage user experience).
     * 
     * @param subscriber {@link Flow.Subscriber} used for messages
     */
    void subscribe(Flow.Subscriber<? super Status> subscriber);

    /**
     * Sends a {@link Request} to the GS Pro.  This request can be either
     * a heartbeat or shot/club details.  There are no results as the API doesn't 
     * provide a method for matching responses (unless they assume the next message)
     * is in response to the most recent message sent (which I wouldn't assume with
     * two way sockets).
     * <p>
     * It looks like a Python implementation of GS Pro assumes this, although at
     * this point I'm going to leave it up to the user to subscribe for these 
     * messages instead.
     * 
     * @param request the heartbeat or shot request
     * @throws ConnectionException if not currently connected
     */
    boolean send(Request request) throws ConnectionException;

}
