package kjd.gspro.app.event;

import org.springframework.context.ApplicationEvent;

import javafx.stage.Stage;
import lombok.Getter;

public class ApplicationStartupEvent extends ApplicationEvent {
    @Getter 
    private Stage stage;

    public ApplicationStartupEvent(Object source, Stage stage) {
        super(source);
        this.stage = stage;
    }
}
