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
     * {@link LaunchMonitorListener} is used to fire events that will be forwarded to the
     * required places.  All LaunchMonitors will have the bridge provide a listener, which
     * will be used to communicate data accordingly.
     * 
     * @param listener
     */
    void addListener(Listener listener);

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
