package kjd.gspro.app.bridge.gspro;

import org.springframework.context.ApplicationEvent;

import kjd.gspro.data.BallData;
import kjd.gspro.data.ClubData;
import kjd.gspro.data.Player;
import lombok.Getter;

public class GSProShotEvent extends ApplicationEvent {
    @Getter
    Player player;

    @Getter
    BallData ballData;

    @Getter
    ClubData clubData;

    public GSProShotEvent(Object source, BallData ballData) {
        this(source, null, ballData, null);
    }

    public GSProShotEvent(Object source, BallData ballData, ClubData clubData) {
        this(source, null, ballData, clubData);
    }

    public GSProShotEvent(Object source, Player player, BallData ballData, ClubData clubData) {
        super(source);

        this.player = player;
        this.ballData = ballData;
        this.clubData = clubData;
    }
}
