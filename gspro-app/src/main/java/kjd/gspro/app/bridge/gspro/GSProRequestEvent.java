package kjd.gspro.app.bridge.gspro;

import org.springframework.context.ApplicationEvent;

import kjd.gspro.api.Request;
import lombok.Getter;

public class GSProRequestEvent extends ApplicationEvent {
    @Getter
    Request request;

    public    GSProRequestEvent(Object source, Request request) {
        super(source);

        this.request = request;
    }
}
