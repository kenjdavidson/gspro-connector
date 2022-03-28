package kjd.gspro.app.bridge.monitor;

import org.springframework.context.ApplicationEvent;

import kjd.gspro.data.BallData;
import kjd.gspro.data.ClubData;
import lombok.Getter;

public class LaunchMonitorShotEvent extends ApplicationEvent {
    @Getter
    BallData ballData;

    @Getter
    ClubData clubData;

    public LaunchMonitorShotEvent(Object source, BallData ballData) {
        this(source, ballData, null);
    }


    public LaunchMonitorShotEvent(Object source, BallData ballData, ClubData clubData) {
        super(source);

        this.ballData = ballData;
        this.clubData = clubData;
    }
}
