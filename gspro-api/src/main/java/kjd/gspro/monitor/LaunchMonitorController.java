package kjd.gspro.monitor;

/**
 * Interface which must be implemented by the provided launch monitor if there is 
 * an FXML view that will be placed onto the screen.  If the views controller 
 * implements {@link LaunchMonitorController} then the {@link LaunchMonitor} will
 * be added, once it is created and connected.
 * <p>
 * The API has no ties to FXML, the provided library should include with the 
 * {@code provided} scope if one is implemented.  Otherwise the standard screen 
 * will be displayed.
 * 
 * @author kenjdavidson
 */
public interface LaunchMonitorController {
    void setLaunchMonitor(LaunchMonitor launchMonitor);
}
