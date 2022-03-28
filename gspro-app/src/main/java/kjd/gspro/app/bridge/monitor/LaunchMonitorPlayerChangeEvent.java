package kjd.gspro.app.bridge.monitor;

import org.springframework.context.ApplicationEvent;

import kjd.gspro.data.Player;
import lombok.Getter;

public class LaunchMonitorPlayerChangeEvent extends ApplicationEvent {
    @Getter
    Player player;

    public LaunchMonitorPlayerChangeEvent(Object source, Player player) {
        super(source);
        this.player = player;
    }
}
