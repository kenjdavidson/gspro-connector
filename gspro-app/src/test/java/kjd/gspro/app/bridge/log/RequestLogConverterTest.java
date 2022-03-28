package kjd.gspro.app.bridge.log;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.text.MessageFormat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import kjd.gspro.api.Request;
import kjd.gspro.data.Options;

@TestInstance(Lifecycle.PER_CLASS)
public class RequestLogConverterTest {

    RequestLogConverter converter;

    @BeforeAll
    void setup() {
        converter = new RequestLogConverter();
    }

    @Test
    void testConvert() {

    }

    @Test
    void testHeartbeat() {
        Request request = mock(Request.class);
        assertEquals(converter.heartbeat(request), "Sending heartbeat...");
    }

    @Test
    void testMonitor() {
        Request request = mock(Request.class);
        Options options = Options.builder()
            .monitorReady(true)
            .monitorBallDetected(true)
            .ballData(false)
            .clubData(false)
            .build();
        doReturn(options).when(request).getShotDataOptions();

        String message = MessageFormat.format("Sending monitor state; ready: {0}, ball detected: {1}", 
            options.getMonitorReady(), options.getMonitorBallDetected());
        assertEquals(converter.monitor(request), message);
    }

    @Test
    void testShot() {
        Request request = mock(Request.class);
        Options options = Options.builder()
            .ballData(true)
            .clubData(true)
            .build();
        doReturn(options).when(request).getShotDataOptions();

        String message = MessageFormat.format("Sending shot data; ball data?: {0}, club data?: {1}",  
            options.getBallData(), options.getClubData());
        assertEquals(converter.shot(request), message);
    }

    @Test
    void testMessage_heartbeat() {

    }

    @Test
    void testMessage_shot() {

    }

    @Test
    void testMessage_monitor() {

    }
}
