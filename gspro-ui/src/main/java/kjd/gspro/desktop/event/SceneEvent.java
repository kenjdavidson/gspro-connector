package kjd.gspro.desktop.event;

import java.util.Optional;

import org.springframework.context.ApplicationEvent;

import javafx.stage.Stage;
import lombok.Getter;

public class SceneEvent extends ApplicationEvent {
    @Getter
    private Stage stage;

    @Getter 
    private String view; 

    @Getter 
    private Optional<String> title;

    public SceneEvent(Object source, Stage onStage, String withScene) {
        this(source, onStage, withScene, null);
    }   

    public SceneEvent(Object source, Stage onStage, String withScene, String withTitle) {
        super(source);

        this.stage = onStage;
        this.view = withScene;
        this.title = Optional.of(withTitle);
    }
}
