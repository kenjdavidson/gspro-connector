package kjd.gspro.app.bridge;

import java.io.IOException;

import org.springframework.context.ApplicationEventPublisher;
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
import kjd.gspro.data.Player;
import kjd.gspro.data.Units;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BridgeService implements ConnectionListener {

    ApplicationEventPublisher publisher;

    @Getter
    ObjectProperty<ConnectionStatus> connectionStatus;

    @Getter
    ObjectProperty<Player> player;

    Connection connection;
    RequestBuilder requestBuilder;

    public BridgeService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;

        connectionStatus = new SimpleObjectProperty<>(this, "gsproConnected", ConnectionStatus.DISCONNECTED);
        player = new SimpleObjectProperty<>(this, "player");
    }

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
        log.info("Connection connected");        
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
        log.info("Connection disconnected");        
        Platform.runLater(() ->connectionStatus.setValue(ConnectionStatus.DISCONNECTED));

        publisher.publishEvent(new StatusEvent(this, status));        
    }

    @Override
    public void onError(Status status, Throwable t) {
        log.error("Error during communication with GS Pro Connect", t);
        
        publisher.publishEvent(new StatusEvent(this, status));
        publisher.publishEvent(new ApplicationErrorEvent(this, status.getMessage(), t));
    }

}
