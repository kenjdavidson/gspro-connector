package kjd.gspro.app.monitor.form;

import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.UUID;

import kjd.gspro.monitor.LaunchMonitor;
import kjd.gspro.monitor.LaunchMonitorProvider;

public class FormLaunchMonitorProvider implements LaunchMonitorProvider {
    
    private static UUID ID = UUID.randomUUID();
    private static Properties defaultProperties;

    @Override 
    public String getId() {
        return ID.toString();
    }

    @Override
    public LaunchMonitor create(Properties properties) {
        return new FormLaunchMonitor(properties);
    }

    @Override
    public Properties getDefaultProperties() {
        if (defaultProperties == null) {
            defaultProperties = new Properties();
        }

        return defaultProperties;
    }

    @Override
    public String getName(Locale locale) {
        return ResourceBundle.getBundle("form_launch_monitor", locale).getString("name");
    }

    @Override
    public String getDescription(Locale locale) {
        return ResourceBundle.getBundle("form_launch_monitor", locale).getString("description");
    }

	@Override
	public Optional<URL> getLaunchMonitorViewUrl() {
        String path = String.format("/%s.fxml", FormLaunchMonitor.class.getName().replaceAll("\\.", "/").replace("Controller", ""));
		return Optional.ofNullable(getClass().getResource(path));
	}

}
