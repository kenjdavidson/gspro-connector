package kjd.gspro.app;

import java.io.IOException;
import java.util.ResourceBundle;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import kjd.gspro.app.ui.PrimaryStageAware;

@Component
public class ViewManager {

    private ApplicationContext applicationContext;

    public ViewManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public <T> T load(String view) throws IOException {
        return load(view, null);
    }

    public <T> T load(String view, Stage stage) throws IOException {
        String resource = validateViewPath(view);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource), ResourceBundle.getBundle("i18n"));
        
        loader.setControllerFactory(applicationContext::getBean);
        
        T parent = loader.load();  

        if (loader.getController() instanceof PrimaryStageAware) {
            ((PrimaryStageAware)loader.getController()).setPrimaryStage(stage);
        }

        return parent;
    }

    private String validateViewPath(String view) {
        StringBuilder sb = new StringBuilder(view);
        if (!view.startsWith("/")) 
            sb.insert(0, "/");
        if (!view.endsWith(".fxml")) 
            sb.append(".fxml");
        return sb.toString();
    }
}
