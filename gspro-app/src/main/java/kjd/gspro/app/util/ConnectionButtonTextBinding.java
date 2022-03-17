package kjd.gspro.app.util;

import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.commons.text.WordUtils;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import kjd.gspro.app.bridge.ConnectionStatus;

public class ConnectionButtonTextBinding extends StringBinding {
    ObjectProperty<ConnectionStatus> status;
    ResourceBundle resourceBundle;

    public ConnectionButtonTextBinding(ObjectProperty<ConnectionStatus> status, ResourceBundle resourceBundle) {
        super.bind(status);

        this.status = status;
        this.resourceBundle = resourceBundle;
    }

    @Override
    protected String computeValue() {
        return Optional.ofNullable(status.getValue())
            .map(s -> String.format("app.%s", s.command.toLowerCase()))
            .map(resourceBundle::getString)
            .orElse(WordUtils.capitalize(status.getValue().name()));
    }  
}
