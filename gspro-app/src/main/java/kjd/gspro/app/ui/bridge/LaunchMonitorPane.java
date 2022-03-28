package kjd.gspro.app.ui.bridge;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import org.controlsfx.glyphfont.Glyph;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import kjd.gspro.app.ApplicationErrorEvent;
import kjd.gspro.app.bridge.ConnectionStatus;
import kjd.gspro.app.bridge.monitor.LaunchMonitorService;
import kjd.gspro.app.util.ConnectionButtonDisableBinding;
import kjd.gspro.app.util.ConnectionButtonTextBinding;
import kjd.gspro.monitor.LaunchMonitor;
import kjd.gspro.monitor.LaunchMonitorController;
import kjd.gspro.monitor.LaunchMonitorProvider;
import lombok.extern.slf4j.Slf4j;

@Component
@Scope("prototype")
@Slf4j
public class LaunchMonitorPane implements Initializable {

    ApplicationEventPublisher publisher;
    LaunchMonitorService launchMonitorService;
    
    Optional<LaunchMonitorController> launchMonitorController;

    @FXML 
    ComboBox<LaunchMonitorProvider> launchMonitorSelect;

    @FXML 
    Button configureButton;

    @FXML 
    Button connectButton;

    /**
     * Provides an AnchorPane where (if provided) the LaunchMonitor UI will be 
     * displayed.  Using the LaunchMonitorProvider#
     */
    @FXML
    ScrollPane launchMonitorPane;

    public LaunchMonitorPane(ApplicationEventPublisher publisher, 
            LaunchMonitorService launchMonitorService) {
        this.publisher = publisher;
        this.launchMonitorService = launchMonitorService;
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {        
        launchMonitorSelect.setPromptText(resources.getString("tab.monitor.choose"));
		launchMonitorSelect.setItems(FXCollections.observableArrayList(launchMonitorService.availableLaunchMonitors()));
        launchMonitorSelect.setCellFactory(this::launchMonitorCellFactory);
        launchMonitorSelect.setConverter(launchMonitorStringConverter());
        launchMonitorSelect.disableProperty().bind(launchMonitorService.getConnectionStatus().isNotEqualTo(ConnectionStatus.DISCONNECTED));
        launchMonitorSelect.getSelectionModel().selectedItemProperty().addListener((ob,o,n) -> onLaunchMonitorProviderChange(n));
        
        configureButton.setGraphic(Glyph.create("FontAwesome|GEAR"));
        configureButton.disableProperty().bind(launchMonitorService.getConnectionStatus().isNotEqualTo(ConnectionStatus.DISCONNECTED));

        connectButton.textProperty().bind(new ConnectionButtonTextBinding(launchMonitorService.getConnectionStatus(), resources));
        connectButton.disableProperty().bind(new ConnectionButtonDisableBinding(launchMonitorService.getConnectionStatus()));
	}    

    @FXML
    public void configureLaunchMonitor(Event event) {

    }

    @FXML
    public void toggleConnection(Event event) {
        if (launchMonitorService.getConnectionStatus().getValue().equals(ConnectionStatus.CONNECTED)) {
            launchMonitorService.disconnect();
        } else {
            LaunchMonitorProvider provider = launchMonitorSelect.getSelectionModel().getSelectedItem();
            if (provider != null) {
                LaunchMonitor launchMonitor = provider.create(provider.getDefaultProperties());                
                launchMonitorController.ifPresent(c -> c.setLaunchMonitor(launchMonitor));
                launchMonitorService.connect(launchMonitor);
            }
        }
    }

    void onLaunchMonitorProviderChange(LaunchMonitorProvider selected) {
        launchMonitorPane.setContent(new VBox());
        launchMonitorController = Optional.empty();

        selected.getLaunchMonitorViewUrl().ifPresent(url -> loadLaunchMonitorFXML(url));
    }

    void loadLaunchMonitorFXML(URL url) {
        try {
            FXMLLoader loader = new FXMLLoader(url);
            Parent parent = loader.load();
            launchMonitorPane.setContent(parent);

            Object controller = loader.getController();
            if (controller instanceof LaunchMonitorController) {
                launchMonitorController = Optional.of((LaunchMonitorController) controller);
            }            
        } catch (Exception e) {
            String msg = MessageFormat.format("Could not load view {0}", url.toString());
            publisher.publishEvent(new ApplicationErrorEvent(this, msg, e));
            
            log.error(msg, e);
        }
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
