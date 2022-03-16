package kjd.gspro.app.ui.bridge;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import kjd.gspro.app.bridge.BridgeService;
import kjd.gspro.app.util.BindingUtils;
import kjd.gspro.data.Club;
import kjd.gspro.data.Hand;
import kjd.gspro.data.Player;
import lombok.extern.slf4j.Slf4j;

@Component
@Scope("prototype")
@Slf4j
public class GSProPane implements Initializable {

    BridgeService bridgeService;

    @FXML
    Button connectButton;

    @FXML 
    Label handLabel;

    @FXML 
    Label clubLabel;

    Circle connectionStatus;

    public GSProPane(BridgeService bridgeService) {
        this.bridgeService = bridgeService;
    }

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        connectionStatus = new Circle(8, Color.RED);
        connectionStatus.fillProperty().bind(Bindings.when(bridgeService.getConnected()).then(Color.GREEN).otherwise(Color.RED));                
        connectButton.textProperty().bind(BindingUtils.stringBinding(rb, bridgeService.getConnected(), "app.disconnect", "app.connect"));        
        connectButton.setGraphic(connectionStatus);        

        handLabel.textProperty().bind(new PlayerHandBinding(bridgeService.getPlayer()));
        clubLabel.textProperty().bind(new PlayerClubBinding(bridgeService.getPlayer()));
    }
    
    @FXML
    public void toggleConnected(Event event) {
        if (bridgeService.getConnected().get()) {
            bridgeService.disconnect();
        } else {
            bridgeService.connect();
        }
    }

    private static class PlayerHandBinding extends StringBinding {
        ObjectProperty<Player> player;
        public PlayerHandBinding(ObjectProperty<Player> player) {
            super.bind(player);
            this.player = player;
        }

        @Override
        protected String computeValue() {
            return Optional.ofNullable(player.getValue())
                .map(Player::getHanded)
                .map(Hand::toString)
                .orElse("");
        }        
    }

    private static class PlayerClubBinding extends StringBinding {
        ObjectProperty<Player> player;
        public PlayerClubBinding(ObjectProperty<Player> player) {
            super.bind(player);
            this.player = player;
        }

        @Override
        protected String computeValue() {
            return Optional.ofNullable(player.getValue())
                .map(Player::getClub)
                .map(Club::toString)
                .orElse("");
        }        
    }
}
