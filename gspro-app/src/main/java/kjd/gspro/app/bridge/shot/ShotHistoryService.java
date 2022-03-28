package kjd.gspro.app.bridge.shot;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kjd.gspro.app.bridge.gspro.GSProShotEvent;
import kjd.gspro.data.BallData;
import kjd.gspro.data.ClubData;
import kjd.gspro.data.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class ShotHistoryService {

    @Getter private ObservableList<Shot> shotHistory = FXCollections.observableArrayList();;

    @EventListener
    public void onLaunchMonitorShot(GSProShotEvent event) {
        log.debug("Received launch monitor shot details");

        shotHistory.add(shot(event.getPlayer(), event.getBallData(), event.getClubData()));
    }

    private Shot shot(Player player, BallData ballData, ClubData clubData) {
        return new Shot(player, ballData, clubData);
    }
}
