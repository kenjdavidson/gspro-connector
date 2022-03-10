package kjd.gspro.monitor;

import java.util.Properties;

/**
 * Provides a method for creating {@link LaunchMonitor} implementations for use with the
 * {@link java.util.ServiceLoader}.
 * 
 * @author kenjdavidson
 */
public interface LaunchMonitorProvider {

    /**
     * Create a {@link LaunchMonitor}.
     * 
     * @param properties configuration that was saved.
     * @return
     */
    LaunchMonitor create(Properties properties);

}
