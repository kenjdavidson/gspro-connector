package kjd.gspro.app.bridge.monitor;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.stereotype.Component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import kjd.gspro.api.Status;
import kjd.gspro.app.bridge.ConnectionStatus;
import kjd.gspro.app.bridge.StatusEvent;
import kjd.gspro.app.bridge.gspro.GSProShotEvent;
import kjd.gspro.data.BallData;
import kjd.gspro.data.ClubData;
import kjd.gspro.data.Player;
import kjd.gspro.monitor.LaunchMonitor;
import kjd.gspro.monitor.LaunchMonitorProvider;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LaunchMonitorService implements LaunchMonitor.Listener {

    ApplicationEventPublisher publisher;

    @Getter
    ObjectProperty<ConnectionStatus> connectionStatus;

    @Getter
    ObjectProperty<LaunchMonitor> launchMonitor;

    public LaunchMonitorService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
        
        this.connectionStatus = new SimpleObjectProperty<>(this, "connectionStatus", ConnectionStatus.DISCONNECTED);
        this.connectionStatus.addListener((ob, o, n) -> publishStatus(100, "Status changed {0}", n));
        this.launchMonitor = new SimpleObjectProperty<>(this, "launchMonitor", null);
    }
    
    public List<LaunchMonitorProvider> availableLaunchMonitors() {
        log.debug("Looking up available LaunchMonitorProvider(s)");
        return SpringFactoriesLoader.loadFactories(LaunchMonitorProvider.class, null);
    }

    public void connect(LaunchMonitor monitor) {
        if (launchMonitor.isNotNull().getValue()) {
            // This should never happen, but if it does we need to disconnect from the
            // already connected Launch Monitor.  This should probably throw an exception
            // or have a forceable disconnect, or maintain a map of connected monitors
            // but for the time being just hard cancel and connect.
            launchMonitor.getValue().removeListener(this);
            launchMonitor.getValue().disconnect();
        }

        launchMonitor.set(monitor);
        connectionStatus.setValue(ConnectionStatus.CONNECTING);

        monitor.addListener(this);
        monitor.connect();               
    }

    public void disconnect() {
        connectionStatus.setValue(ConnectionStatus.DISCONNECTING);
        Optional.ofNullable(launchMonitor.getValue()).ifPresent(lm -> lm.disconnect());
    }

    @Override
    public void onConnected() {
        this.connectionStatus.setValue(ConnectionStatus.CONNECTED);
    }

    @Override
    public void onDisconnected() {
        connectionStatus.setValue(ConnectionStatus.DISCONNECTED);
        launchMonitor.getValue().removeListener(this);
        launchMonitor.setValue(null);
    }

    @Override
    public void onPlayerChange(Player player) {
        publisher.publishEvent(new LaunchMonitorPlayerChangeEvent(this, player));   
    }

    @Override
    public void onReadyStateChange(Boolean readyState, Boolean ballState) {
        publisher.publishEvent(new LaunchMonitorReadyStateEvent(this, readyState, ballState));
    }

    @Override
    public void onShot(BallData ballData, ClubData clubData) {
        publisher.publishEvent(new LaunchMonitorShotEvent(this, ballData, clubData));
    }

    void publishStatus(Integer code, String message, Object...args) {
        String msg = MessageFormat.format(message, args);
        StatusEvent event = new StatusEvent(this, Status.builder().code(code).message(msg).build());

        publisher.publishEvent(event);
    }
}
