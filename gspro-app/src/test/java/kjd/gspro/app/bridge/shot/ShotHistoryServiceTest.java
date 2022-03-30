package kjd.gspro.app.bridge.shot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import kjd.gspro.app.bridge.gspro.GSProShotEvent;
import kjd.gspro.app.monitor.form.FormLaunchPreset;
import kjd.gspro.data.BallData;

public class ShotHistoryServiceTest {

    ShotHistoryService service;

    @Before
    public void setup() {
        service = new ShotHistoryService();
    }

    @Test
    public void testInitialization_getShotHistory_empty() {
        assertEquals(service.getShotHistory().size(), 0);
    }

    @Test
    public void testOnLaunchMonitorShot_getShotHistory_added() {
        BallData ballData = ballData(FormLaunchPreset.DRIVER);
        service.onLaunchMonitorShot(new GSProShotEvent(this, ballData));
        assertEquals(service.getShotHistory().size(), 1);
    }

    BallData ballData(FormLaunchPreset club) {
        return BallData.builder()
            .speed(club.ballSpeed)
            .spinAxis(club.ballSpinAxis)
            .totalSpin(club.ballTotalSpin)
            .hla(club.ballLaunchAngle)
            .vla(club.ballLaunchDirection)
            .build();
    };
}
