package kjd.gspro.monitor;

import kjd.gspro.data.BallData;
import kjd.gspro.data.ClubData;
import kjd.gspro.data.Player;

/**
 * Launch monitors must implement the following interface in order to provide
 * communication between the GS Pro bridge.
 * 
 * @author kenjdavidson
 */
public interface LaunchMonitor {

    /**
     * Attempt to connect to the Launch Monitor.
     * <p>
     * When completed the monitor should notify the {@link Listener#onConnected()}.
     */
    void connect();

    /**
     * Attempt to disconnect from the launch monitor.
     * <p>
     * When completed the monitor should notify {@link Listener#onDisconnected()}.
     */
    void disconnect();

    /**
     * Notify the launch monitor that the current player handedness or club has changed.  This is required for 
     * monitors that need to switch between full strike clubs and putting, such as:
     * <ul>
     *  <li>Tittle/SLX Micro</li>
     * </ul>
     * 
     * @param player
     */
    void notifyPlayer(Player player);

    /**
     * The bridge implementation will provide a single listener that is used for sending requests (status) to 
     * GS Pro.  
     * <p>
     * It might be wise to implement as a {@link java.lang.ref.WeakReference} incase anything goes down on the
     * GS Pro side of things.
     * 
     * @param listener
     */
    void addListener(Listener listener);

    /**
     * Provides a method for removing a {@link Listener} if the bridge needs to reset the GS Pro connection
     * or something goes bad.
     * 
     * @param listener
     * @return
     */
    Listener removeListener(Listener listener);

    /**
     * {@link LaunchMonitor}(s) provides the {@link Listener} as a method for communication of
     * launch monitor events in the GS Pro Open Conenct format.  The currently available events 
     * are:
     * <ul>
     *  <li>{@link #onReadyStateChange(Boolean, Boolean)} which updates GS Pro with the state of
     *      the launch monitor, this includes overall state and ball state (whether the ball is
     *      decected).</li>
     *  <li>{@link #onShot(BallData, ClubData)} which provides the Shot (and optional ball) details
     *      once a swing has been completed.</li>
     *  <li>{@link #onPlayerChange(Player)} (optional) - At this point there is no method for GS Pro
     *      to accept Player/Club updates from the launch monitor.  This is in here solely for future
     *      and informational use.</li>
     * </ul>
     * 
     * @author kenjdavidson
     */
    public static interface Listener {   
        /**
         * Called when the launch monitor is connected.
         */
        void onConnected();

        /**
         * Called when the launch monitor is disconnected.
         */
        void onDisconnected();

        /**
         * Update GS Pro when of a Player/Club change on from the Simulator.  At this point the GS Pro doesn't
         * seem to accept Club changes (nor Player, which makes sense as it's controlling the game) but at some
         * point it might become available - this method may need to get split.
         * 
         * @param onPlayerChange updated {@link Player} (and club) information
         */
        void onPlayerChange(Player player);

        /**
         * Update GS Pro of ready/ball detected state.
         * <p>
         * At this point I'm unsure if GS Pro requires the launch monitor to be in ready state before
         * it accepts a shot.  This will require some testing.
         * 
         * @param readyState whether the launch monitor is in ready to accept a swing.
         * @param ballState whether a ball is detected
         */
        void onReadyStateChange(Boolean readyState, Boolean ballState);

        /**
         * Update GS Pro of a completed shot.  Shots should include {@link BallData}
         * but can also include an optoinal {@link ClubData}.
         * 
         * @param onShot
         */
        void onShot(BallData ballData, ClubData clubData);
    }
}
