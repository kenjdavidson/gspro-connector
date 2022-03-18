package kjd.gspro.app.ui;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.controlsfx.dialog.Wizard;
import kjd.gspro.app.ApplicationShutdownEvent;
import kjd.gspro.app.bridge.GSProConnectService;
import kjd.gspro.app.ui.about.AboutDialog;
import kjd.gspro.app.util.ConnectionButtonTextBinding;
import kjd.gspro.app.util.ConnectionStatusColourBinding;
import lombok.extern.slf4j.Slf4j;

@Component
@Scope("prototype")
@Slf4j
public class ApplicationController implements Initializable, StageAware {

    ApplicationEventPublisher publisher;
    GSProConnectService bridgeService;

    @FXML BorderPane layout;

    // Menu, at some point break out
    @FXML MenuItem miPreferences;
    @FXML MenuItem miQuit;
    @FXML MenuItem miAbout;
    
    // Toolbar, at some point break out
    @FXML Label tbGsproStatus;
    Circle gsproStatusIcon = new Circle(8);

    @FXML Label tbMonitorStatus;
    Circle monitorStatusIcon = new Circle(8);

    SimpleObjectProperty<Stage> stage = new SimpleObjectProperty<>();

    public ApplicationController(ApplicationEventPublisher publisher, GSProConnectService bridgeService) {
        log.debug("Initializing application controller");
        this.publisher = publisher;
        this.bridgeService = bridgeService;
    }

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        stage.addListener((obv, o, n) -> n.setTitle(rb.getString("app.title")));

        tbGsproStatus.setGraphic(gsproStatusIcon);
        tbMonitorStatus.setGraphic(monitorStatusIcon);

        gsproStatusIcon.fillProperty().bind(new ConnectionStatusColourBinding(bridgeService.getConnectionStatus(), rb));             
    }

    @FXML
    public void onFileClose(ActionEvent event) {
        publisher.publishEvent(new ApplicationShutdownEvent(event.getSource()));
    }

    @FXML 
    public void onHelpAbout(ActionEvent event) {
        new AboutDialog().show();
    }

    @Override
    public void setStage(Stage stage) {
        this.stage.setValue(stage);
    }
}
