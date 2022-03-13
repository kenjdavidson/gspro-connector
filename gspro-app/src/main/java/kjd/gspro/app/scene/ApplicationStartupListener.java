package kjd.gspro.app.scene;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kjd.gspro.app.FXMLManager;
import kjd.gspro.app.event.ApplicationStartupEvent;
import kjd.gspro.app.ui.ApplicationController;
import kjd.gspro.app.ui.PrimaryStageAware;

@Component
@Scope("singleton")
public class ApplicationStartupListener implements ApplicationListener<ApplicationStartupEvent> {

    Logger logger = LoggerFactory.getLogger(ApplicationStartupListener.class);    

    @Autowired 
    FXMLManager fxmlManager;

    @Override
    public void onApplicationEvent(ApplicationStartupEvent event) {
        logger.debug("Loading Application Scene");
        Stage stage = event.getStage();

        try {
            Parent parent = (Parent) fxmlManager.load("kjd/gspro/app/ui/Application", stage);            
            stage.setScene(new Scene(parent));
            stage.setResizable(true);
        } catch (IOException e) {
            logger.error("Error occurred while changing scenes", e);
        }                

        stage.show();
    }
}
