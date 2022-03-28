package kjd.gspro.app.bridge.shot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import kjd.gspro.app.bridge.gspro.GSProShotEvent;
import kjd.gspro.app.monitor.form.FormLaunchPreset;
import kjd.gspro.data.BallData;

@TestInstance(Lifecycle.PER_CLASS)
public class ShotHistoryServiceTest {

    ShotHistoryService service;

    @BeforeAll
    void setup() {
        service = new ShotHistoryService();
    }

    @Test 
    void testInitialization_getShotHistory_empty() {
        assertEquals(service.getShotHistory().size(), 0);
    }

    @Test
    void testOnLaunchMonitorShot_getShotHistory_added() {
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
