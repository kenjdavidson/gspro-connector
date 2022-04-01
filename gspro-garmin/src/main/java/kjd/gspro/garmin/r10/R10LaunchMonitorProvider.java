package kjd.gspro.garmin.r10;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kjd.gspro.monitor.LaunchMonitor;
import kjd.gspro.monitor.LaunchMonitorProvider;

public class R10LaunchMonitorProvider implements LaunchMonitorProvider {

    private static final Logger log = LoggerFactory.getLogger(R10LaunchMonitorProvider.class);
    private static final String ID = "r10-launch-monitor";

    private Properties defaultProperties = new Properties();

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public LaunchMonitor create(Properties properties) {
        return new R10LaunchMonitor(properties);
    }

    @Override
    public String getName(Locale locale) {
        return ResourceBundle.getBundle("r10", locale).getString("name");
    }

    @Override
    public String getDescription(Locale locale) {
        return ResourceBundle.getBundle("r10", locale).getString("description");
    }

    @Override
    public Properties getDefaultProperties() {      
        try {
            defaultProperties.load(getClass().getResource("r10-config.properties").openStream());
        } catch (IOException e) {
            log.error("Unable to load r10-config.properties", e);
        } 

        return defaultProperties;
    }

    @Override
    public Optional<URL> getLaunchMonitorViewUrl() {
        return Optional.empty();
    }
    
}
