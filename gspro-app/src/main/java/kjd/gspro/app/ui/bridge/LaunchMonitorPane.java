package kjd.gspro.app.ui.bridge;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.controlsfx.glyphfont.Glyph;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.StringConverter;
import kjd.gspro.app.bridge.LaunchMonitorService;
import kjd.gspro.monitor.LaunchMonitorProvider;

@Component
@Scope("prototype")
public class LaunchMonitorPane implements Initializable {

    LaunchMonitorService launchMonitorService;

    @FXML 
    ComboBox<LaunchMonitorProvider> launchMonitorSelect;

    @FXML 
    Button launchMonitorConfigButton;

    public LaunchMonitorPane(LaunchMonitorService launchMonitorService) {
        this.launchMonitorService = launchMonitorService;
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {        
        launchMonitorSelect.setPromptText(resources.getString("tab.monitor.choose"));
		launchMonitorSelect.setItems(FXCollections.observableArrayList(launchMonitorService.availableLaunchMonitors()));
        launchMonitorSelect.setCellFactory(this::launchMonitorCellFactory);
        launchMonitorSelect.setConverter(launchMonitorStringConverter());
        launchMonitorSelect.disableProperty().bind(launchMonitorService.getLaunchMonitor().isNotNull());

        launchMonitorConfigButton.setGraphic(Glyph.create("FontAwesome|GEAR"));
	}    

    @FXML
    public void configureLaunchMonitor(Event event) {

    }

    @FXML
    public void toggleConnection(Event event) {

    }

    ListCell<LaunchMonitorProvider> launchMonitorCellFactory(ListView<LaunchMonitorProvider> view) {
        return new ListCell<LaunchMonitorProvider>() {
            @Override public void updateItem(LaunchMonitorProvider item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText("");
                } else {
                    setText(item.getName(Locale.getDefault()));
                }
            }
        };
    }

    StringConverter<LaunchMonitorProvider> launchMonitorStringConverter() {
        return new StringConverter<LaunchMonitorProvider>(){
			@Override
			public String toString(LaunchMonitorProvider monitor) {
				return monitor.getName(Locale.getDefault());
			}

			@Override
			public LaunchMonitorProvider fromString(String string) {
				return launchMonitorSelect.getItems().stream()
                    .filter(p -> p.getName(Locale.getDefault()).equals(string))
                    .findFirst()
                    .orElse(null);
			}
            
        };
    }
}
