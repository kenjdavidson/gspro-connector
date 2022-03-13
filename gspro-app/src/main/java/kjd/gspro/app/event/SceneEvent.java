package kjd.gspro.app.event;

import org.springframework.context.ApplicationEvent;

import javafx.stage.Stage;
import lombok.Getter;

public class SceneEvent extends ApplicationEvent {
    @Getter 
    private String scene;

    @Getter 
    private Stage stage;

    public SceneEvent(Object source, String scene, Stage stage) {
        super(source);

        this.scene = scene;
        this.stage = stage;
    }
}
