package kjd.gspro.app.util;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import kjd.gspro.app.bridge.ConnectionStatus;

public class ConnectionButtonDisableBinding extends BooleanBinding {
    ObjectProperty<ConnectionStatus> status;

    public ConnectionButtonDisableBinding(ObjectProperty<ConnectionStatus> status) {
        super.bind(status);

        this.status = status;
    }

    @Override
    protected boolean computeValue() {
        return status.isEqualTo(ConnectionStatus.CONNECTING)
            .or(status.isEqualTo(ConnectionStatus.DISCONNECTING))
            .getValue();
    }  
}
