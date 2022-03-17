package kjd.gspro.app.util;

import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Paint;
import kjd.gspro.app.bridge.ConnectionStatus;

public class ConnectionStatusColourBinding extends ObjectBinding<Paint> {
    final ObjectProperty<ConnectionStatus> status;
    final ResourceBundle resourceBundle;
    final String[] colours = new String[] { "yellow", "red", "yellow", "green" };

    public ConnectionStatusColourBinding(ObjectProperty<ConnectionStatus> status, ResourceBundle resourceBundle) {
        super.bind(status);

        this.status = status;
        this.resourceBundle = resourceBundle;
    }

    @Override
    protected Paint computeValue() {
        return Optional.ofNullable(status.getValue())
            .map(s -> colours[s.ordinal()])
            .map(Paint::valueOf)
            .orElse(Paint.valueOf("white"));
    }  
}
