package kjd.gspro.app.bridge.monitor;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

public class LaunchMonitorReadyStateEvent extends ApplicationEvent {
    @Getter
    Boolean monitorReady;

    @Getter 
    Boolean ballDetected;

    public LaunchMonitorReadyStateEvent(Object source, Boolean monitorReady, Boolean ballDetected) {
        super(source);

        this.monitorReady = monitorReady;
        this.ballDetected = ballDetected;
    }
}
