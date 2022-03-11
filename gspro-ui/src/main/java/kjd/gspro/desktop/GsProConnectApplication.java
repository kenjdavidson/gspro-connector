package kjd.gspro.desktop;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class GsProConnectApplication extends Application {
    private Logger logger = LoggerFactory.getLogger(GsProConnectApplication.class);    
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void start(Stage stage) throws Exception {
        logger.debug("Starting GsProConnectApplication");

        stage.setTitle("GS Pro Connect");
        stage.show();
    }

    @Override
    public void init() {
        logger.debug("Initializing Spring ApplicationContext");
        applicationContext = new SpringApplicationBuilder(GsProConnectApplicationBoot.class).run();
    }

    @Override
    public void stop() throws Exception {
        logger.debug("Stopping GsProConnectApplication");
        applicationContext.close();
        Platform.exit();
    }      
}
