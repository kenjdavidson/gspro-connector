package kjd.gspro.desktop.scene;

import java.io.IOException;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import kjd.gspro.desktop.ApplicationLocale;

@Component
public class FxmlManager {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ApplicationLocale applicationLocale;

    public <T> T load(String view) throws IOException {
        FXMLLoader loader = FXMLLoader.load(getClass().getResource(view), 
            applicationLocale.getResources());
        loader.setControllerFactory(applicationContext::getBean);
        return loader.load();
    }
}
