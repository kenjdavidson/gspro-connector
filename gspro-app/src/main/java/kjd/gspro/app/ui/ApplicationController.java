package kjd.gspro.app.ui;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import kjd.gspro.app.ApplicationShutdownEvent;
import kjd.gspro.app.event.SceneEvent;

@Component
@Scope("prototype")
public class ApplicationController implements Initializable, PrimaryStageAware {

    @Autowired 
    ApplicationEventPublisher publisher;

    @FXML
    BorderPane layout;

    @FXML 
    MenuItem miPreferences;

    @FXML
    MenuItem miQuit;

    @FXML
    MenuItem miAbout;

    @FXML 
    ListView<String> lvStatusLog;

    SimpleObjectProperty<Stage> stage = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stage.addListener((obv, o, n) -> n.setTitle(resources.getString("app.title")));
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
