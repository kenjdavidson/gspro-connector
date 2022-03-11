package kjd.gspro.desktop.scene;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kjd.gspro.desktop.event.SceneEvent;

@Component
public class SceneManager implements ApplicationListener<SceneEvent> {
    @Autowired 
    FxmlManager fxmlManager;

    @Override
    public void onApplicationEvent(SceneEvent event) {
        Stage stage = event.getStage();

        try {
            Parent parent = (Parent) fxmlManager.load(event.getView());    
            stage.setScene(new Scene(parent));
            event.getTitle().ifPresent(stage::setTitle);
            stage.show();
        } catch (IOException e) {
            
        }                
    }
}
