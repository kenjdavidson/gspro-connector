package kjd.gspro.app.ui;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import kjd.gspro.app.ApplicationShutdownEvent;
import kjd.gspro.app.bridge.BridgeService;
import kjd.gspro.app.event.SceneEvent;
import kjd.gspro.app.util.BindingUtils;

@Component
@Scope("prototype")
public class ApplicationController implements Initializable, PrimaryStageAware {

    ApplicationEventPublisher publisher;
    BridgeService bridgeService;

    @FXML BorderPane layout;

    // Menu, at some point break out
    @FXML MenuItem miPreferences;
    @FXML MenuItem miQuit;
    @FXML MenuItem miAbout;
    
    // Toolbar, at some point break out
    @FXML Label tbGsproStatus;
    @FXML Label tbMonitorStatus;

    SimpleObjectProperty<Stage> stage = new SimpleObjectProperty<>();

    public ApplicationController(ApplicationEventPublisher publisher, BridgeService bridgeService) {
        this.publisher = publisher;
        this.bridgeService = bridgeService;
    }

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        stage.addListener((obv, o, n) -> n.setTitle(rb.getString("app.title")));

        tbGsproStatus.textProperty().bind(BindingUtils.stringBinding(rb, bridgeService.getConnected(), "app.connected", "app.disconnected"));
    }

    @FXML
    public void onFileClose(ActionEvent event) {
        publisher.publishEvent(new ApplicationShutdownEvent(event.getSource()));
    }

    @FXML 
    public void onHelpAbout(ActionEvent event) {
        
    }

    @Override
    public void setPrimaryStage(Stage stage) {
        this.stage.setValue(stage);
    }
}
