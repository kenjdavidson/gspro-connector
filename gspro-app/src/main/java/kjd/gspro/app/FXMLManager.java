package kjd.gspro.app;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import kjd.gspro.app.ui.PrimaryStageAware;

@Component
@Scope("singleton")
public class FXMLManager {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    LocaleService localeService;

    public <T> T load(String view) throws IOException {
        return load(view, null);
    }

    public <T> T load(String view, Stage stage) throws IOException {
        String resource = validateView(view);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource), localeService.getResources());
        loader.setControllerFactory(applicationContext::getBean);
        
        T parent = loader.load();  

        if (loader.getController() instanceof PrimaryStageAware) {
            ((PrimaryStageAware)loader.getController()).setPrimaryStage(stage);
        }
        
        return parent;
    }

    String validateView(String view) {
        StringBuilder sb = new StringBuilder(view);
        if (!view.startsWith("/")) 
            sb.insert(0, "/");
        if (!view.endsWith(".fxml")) 
            sb.append(".fxml");
        return sb.toString();
    }
}
