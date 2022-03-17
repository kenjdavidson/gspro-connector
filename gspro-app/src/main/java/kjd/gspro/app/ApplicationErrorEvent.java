package kjd.gspro.app;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

public class ApplicationErrorEvent extends ApplicationEvent {
    @Getter private String message;
    @Getter private Throwable throwable;

    public ApplicationErrorEvent(Object source, String message, Throwable throwable) {
        super(source);

        this.message = message;
        this.throwable = throwable;
    }
}
