package kjd.gspro.app.ui.bridge;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import kjd.gspro.app.bridge.ConnectionStatus;
import kjd.gspro.app.bridge.gspro.GSProConnectService;
import kjd.gspro.app.util.ConnectionButtonDisableBinding;
import kjd.gspro.app.util.ConnectionButtonTextBinding;
import kjd.gspro.app.util.PlayerClubBinding;
import kjd.gspro.app.util.PlayerHandBinding;
import lombok.extern.slf4j.Slf4j;

@Component
@Scope("prototype")
@Slf4j
public class GSProPane implements Initializable {

    GSProConnectService bridgeService;

    @FXML
    Button connectButton;

    @FXML
    Button heartbeatButton;

    @FXML 
    Label handLabel;

    @FXML 
    Label clubLabel;

    public GSProPane(GSProConnectService bridgeService) {
        log.debug("Initializing service");
        this.bridgeService = bridgeService;
    }

    @Override
    public void initialize(URL location, ResourceBundle rb) {        
        connectButton.textProperty().bind(new ConnectionButtonTextBinding(bridgeService.getConnectionStatus(), rb));        
        connectButton.disableProperty().bind(new ConnectionButtonDisableBinding(bridgeService.getConnectionStatus()));

        heartbeatButton.disableProperty().bind(bridgeService.getConnectionStatus().isNotEqualTo(ConnectionStatus.CONNECTED));

        handLabel.textProperty().bind(new PlayerHandBinding(bridgeService.getPlayer()));
        clubLabel.textProperty().bind(new PlayerClubBinding(bridgeService.getPlayer()));
    }
    
    @FXML
    public void toggleConnected(Event event) {                
        if (bridgeService.getConnectionStatus().get() == ConnectionStatus.CONNECTED) {
            log.debug("Attempting to disconnect from GS Pro Connect");
            bridgeService.disconnect();
        } else {
            log.debug("Attempting to connect with GS Pro Connect");
            bridgeService.connect();
        }
    }

    @FXML
    public void sendHeartbeat(Event event) {
        bridgeService.sendHeartbeat();
    }
}
