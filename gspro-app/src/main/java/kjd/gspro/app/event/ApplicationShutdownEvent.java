package kjd.gspro.app.event;

import org.springframework.context.ApplicationEvent;

public class ApplicationShutdownEvent extends ApplicationEvent {
    public ApplicationShutdownEvent(Object source) {
        super(source);
    }
}
