package kjd.gspro.app.bridge;

import org.springframework.context.ApplicationEvent;

import kjd.gspro.api.Status;
import lombok.Getter;

public class StatusEvent extends ApplicationEvent {

    @Getter
    private final Status status;
    
    public StatusEvent(Object source, Status status) {
        super(source);
        
        this.status = status;
    }    

}
