package kjd.gspro.app.bridge.gspro;

import java.io.IOException;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import kjd.gspro.api.Connection;
import kjd.gspro.api.ConnectionListener;
import kjd.gspro.api.Request;
import kjd.gspro.api.RequestBuilder;
import kjd.gspro.api.Status;
import kjd.gspro.app.ApplicationErrorEvent;
import kjd.gspro.app.bridge.ConnectionStatus;
import kjd.gspro.app.bridge.StatusEvent;
import kjd.gspro.app.bridge.monitor.LaunchMonitorReadyStateEvent;
import kjd.gspro.app.bridge.monitor.LaunchMonitorShotEvent;
import kjd.gspro.data.Player;
import kjd.gspro.data.Units;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class GSProConnectService implements ConnectionListener {

    final ApplicationEventPublisher publisher;

    @Getter ObjectProperty<ConnectionStatus> connectionStatus = new SimpleObjectProperty<>(this, "gsproConnected", ConnectionStatus.DISCONNECTED);;
    @Getter ObjectProperty<Player> player = new SimpleObjectProperty<>(this, "player");;

    Connection connection;
    RequestBuilder requestBuilder;

    public void connect() {
        if (connection == null || !connection.isConnected()) {
            log.debug("Attempting connection to GS Pro Connect");
            connectionStatus.setValue(ConnectionStatus.CONNECTING);
            publisher.publishEvent(new StatusEvent(this, Status.connecting()));

            connection = new Connection(this);
            connection.start();

            requestBuilder = RequestBuilder.create("GS Pro Connect Client", Units.YARDS);
        } else {
            log.debug("Already connected to GS Pro Connect, connect() skipped");
        }
    }

    public void disconnect() {
        if (connection != null && connection.isConnected()) {
            log.debug("Attempting disconnect from GS Pro Connect");
            connection.disconnect();
            
            requestBuilder = null;
        } else {
            log.debug("Already disconnected, disconnect() skipped");
        }
    }

    public void sendHeartbeat() {
        Request heartbeat = requestBuilder.heartbeat(true, true);
        try {
            connection.write(heartbeat);
        } catch (IOException e) {
            connection.disconnect();

            publisher.publishEvent(new StatusEvent(this, Status.error(e, true)));
            publisher.publishEvent(new ApplicationErrorEvent(this, "Error during communication with GS Pro", e));
        }
    }

    @Override
    public void onConnected(Status status) {
        log.info("Successfully connected to GS Pro Connector API");        
        Platform.runLater(() ->connectionStatus.setValue(ConnectionStatus.CONNECTED));

        publisher.publishEvent(new StatusEvent(this, status));        
    }

    @Override
    public void onStatus(Status status) {
        publisher.publishEvent(new StatusEvent(this, status));

        if (status.getCode() == 201) {
            // Maybe look into separating to onPlayerStatus() instead of regular status
            // Or even have a PlayerService incase more player data needs to get managed 
            // and listen for a PlayerChangeEvent with the update
            Platform.runLater(() -> player.setValue(status.getPlayer()));
        } else if (status.getCode() >= 500) {
            publisher.publishEvent(new ApplicationErrorEvent(this, status.getMessage(), new Exception(status.getMessage())));
        }
    }

    @Override
    public void onDisconnect(Status status) {
        log.info("Disconnected from GS Pro Connector API");        
        Platform.runLater(() ->connectionStatus.setValue(ConnectionStatus.DISCONNECTED));

        publisher.publishEvent(new StatusEvent(this, status));        
    }

    @Override
    public void onError(Status status, Throwable t) {
        log.error("Error during communication with GS Pro Connect", t);
        
        publisher.publishEvent(new StatusEvent(this, status));
        publisher.publishEvent(new ApplicationErrorEvent(this, status.getMessage(), t));
    }

    @EventListener
    public void onLaunchMonitorReady(LaunchMonitorReadyStateEvent event) {

    }

    @EventListener 
    public void onLaunchMonitorShot(LaunchMonitorShotEvent event) {
        publisher.publishEvent(new GSProShotEvent(this, player.get(), event.getBallData(), event.getClubData()));
    }
}
