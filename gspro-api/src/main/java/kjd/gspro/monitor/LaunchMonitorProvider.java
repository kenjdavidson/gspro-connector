package kjd.gspro.monitor;

import java.util.Locale;
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

    /**
     * Provide the defined name for the launch monitor
     * 
     * @param locale used to get the name
     * @return
     */
    String getName(Locale locale);

    /**
     * Provide a description for the launch monitor
     * 
     * @param locale used to get the description
     * @return
     */
    String getDescription(Locale locale);

    /**
     * Launch monitor default {@link Properties}.  These properties may be saved or updated
     * by the bridge (user) and will be provided to to the {@link LaunchMonitorProvider}
     * during startup.
     * 
     * @return
     */
    Properties getDefaultProperties();

}
