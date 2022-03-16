package kjd.gspro.app.bridge;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import kjd.gspro.api.Connection;
import kjd.gspro.api.ConnectionListener;
import kjd.gspro.api.Status;
import kjd.gspro.client.Client;
import kjd.gspro.data.Player;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BridgeService implements ConnectionListener {

    ApplicationEventPublisher publisher;

    @Getter
    BooleanProperty connected;

    @Getter
    ObjectProperty<Player> player;

    Connection connection;

    public BridgeService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;

        connected = new SimpleBooleanProperty(this, "gsproConnected", false);
        player = new SimpleObjectProperty<>(this, "player");
    }

    public void connect() {
        if (connection == null || !connection.isConnected()) {
            publisher.publishEvent(new StatusEvent(this, Status.connecting()));
            connection = new Connection(Client.DEFAULT_HOST, Client.DEFAULT_PORT, this);
            connection.start();
        }
    }

    public void disconnect() {
        if (connection != null && connection.isConnected()) {
            connection.disconnect();
        }
    }

    @Override
    public void onConnected(Status status) {
        connected.setValue(true);
        
        publisher.publishEvent(new StatusEvent(this, status));
    }

    @Override
    public void onStatus(Status status) {
        publisher.publishEvent(new StatusEvent(this, status));
    }

    @Override
    public void onDisconnect(Status status) {
        connected.setValue(false);
        publisher.publishEvent(new StatusEvent(this, status));
    }

    @Override
    public void onError(Status status, Throwable t) {
        publisher.publishEvent(new StatusEvent(this, status));
    }

}
