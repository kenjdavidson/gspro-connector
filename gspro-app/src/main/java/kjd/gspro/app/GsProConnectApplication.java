package kjd.gspro.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import kjd.gspro.app.event.ApplicationShutdownEvent;
import kjd.gspro.app.event.ApplicationStartupEvent;

public class GsProConnectApplication extends Application 
        implements ApplicationListener<ApplicationShutdownEvent> {

    private Logger logger = LoggerFactory.getLogger(GsProConnectApplication.class);    
    private ConfigurableApplicationContext applicationContext;

    ;

    @Override
    public void start(Stage stage) throws Exception {
        logger.debug("Starting GsProConnectApplication");

        applicationContext.publishEvent(new ApplicationStartupEvent(this, stage));
    }

    @Override
    public void init() {
        logger.debug("Initializing Spring ApplicationContext");

        applicationContext = new SpringApplicationBuilder(GsProConnectApplicationBoot.class)
            .sources(GsProConnectApplicationBoot.class)
            .initializers(initializers())
            .run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void stop() throws Exception {
        logger.debug("Stopping GsProConnectApplication");
        
        applicationContext.close();
        Platform.exit();
    }     

    ApplicationContextInitializer<GenericApplicationContext> initializers() { 
        return ac -> {
            ac.registerBean(Application.class, () -> GsProConnectApplication.this);
            ac.registerBean(Parameters.class, this::getParameters);
            ac.registerBean(HostServices.class, this::getHostServices);
        };
    }

    /**
     * Handle requests to shut down, this should actually be run last along the line of event
     * listeners and check that the ApplicationShutdownEvent has not been stopped (ie) a 
     * listener of the event still has some unsaved data, etc.
     * 
     * @param event
     */
    @Override
    public void onApplicationEvent(ApplicationShutdownEvent event) {
        try {
            this.stop();
        } catch (Exception e) {
            logger.error("Looks like you're not closing the app today!", e);
        }
    }
}
