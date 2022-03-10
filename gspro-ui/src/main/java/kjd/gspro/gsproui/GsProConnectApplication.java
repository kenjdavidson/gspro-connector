package kjd.gspro.gsproui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class GsProConnectApplication extends Application {
    private Logger logger = LoggerFactory.getLogger(GsProConnectApplication.class);
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void start(Stage stage) throws Exception {
        logger.debug("Starting GsProConnectApplication");
        stage.show();
    }

    @Override
    public void init() {
        logger.debug("Initializing Spring ApplicationContext");
        applicationContext = new SpringApplicationBuilder(GSProConnectApplicationBoot.class).run();
    }

    @Override
    public void stop() throws Exception {
        logger.debug("Stopping GsProConnectApplication");
        applicationContext.close();
        Platform.exit();
    }      
}
