package kjd.gspro.desktop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.availability.ApplicationAvailabilityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import kjd.gspro.desktop.event.SceneEvent;

public class GsProConnectApplication extends Application {
    private Logger logger = LoggerFactory.getLogger(GsProConnectApplication.class);    
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void start(Stage stage) throws Exception {
        logger.debug("Starting GsProConnectApplication");

        applicationContext.publishEvent(new SceneEvent(this, stage, "kjd/gspro/desktop/ui/Application"));
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
