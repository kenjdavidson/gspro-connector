package kjd.gspro.monitor;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
     * Handle GS Pro Player/Club changes.
     * 
     * @param player
     */
    void playerChange(Player player);

    /**
     * Update GS Pro when of a Player/Club change on from the Simulator.
     * 
     * @param onPlayerChange
     */
    void onPlayerChange(Consumer<Player> onPlayerChange);

    /**
     * Update GS Pro of ready state.
     * 
     * @param onReadyStateChange
     */
    void onReadyStateChange(Consumer<Boolean> onReadyStateChange);

    /**
     * Update GS Pro of ball state (ball detected) state.
     * 
     * @param onBallStateChange
     */
    void onBallStateChange(Consumer<Boolean> onBallStateChange);

    /**
     * Update GS Pro of a completed shot.  Shots should include {@link BallData}
     * but can also include an optoinal {@link ClubData}.
     * 
     * @param onShot
     */
    void onShot(BiConsumer<BallData,ClubData> onShot);

}
