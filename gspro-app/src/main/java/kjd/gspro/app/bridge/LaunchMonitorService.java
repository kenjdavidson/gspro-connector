package kjd.gspro.app.bridge;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.stereotype.Component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import kjd.gspro.monitor.LaunchMonitor;
import kjd.gspro.monitor.LaunchMonitorProvider;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LaunchMonitorService {

    ApplicationEventPublisher publisher;

    @Getter
    ObjectProperty<LaunchMonitor> launchMonitor;

    public LaunchMonitorService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
        this.launchMonitor = new SimpleObjectProperty<>(this, "launchMonitor", null);
    }
    
    public List<LaunchMonitorProvider> availableLaunchMonitors() {
        log.debug("Looking up available LaunchMonitorProvider(s)");
        return SpringFactoriesLoader.loadFactories(LaunchMonitorProvider.class, null);
    }

}
