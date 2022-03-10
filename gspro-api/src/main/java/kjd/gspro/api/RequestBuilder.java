package kjd.gspro.api;

import kjd.gspro.data.BallData;
import kjd.gspro.data.ClubData;
import kjd.gspro.data.Options;
import kjd.gspro.data.Units;

/**
 * RequestBuilder should be created only in the context for a Client 
 * connection.  It may be wise to have the {@link Client} return the 
 * {@link RequestBuilder} to ensure this, but for the time being 
 * we'll leave it up to the user to manage.
 * <p>
 * This {@link Request}(s) allows the Monitor adapter to implement
 * their own method for creating {@link Request}(s) if they would
 * prefer.
 * <p>
 * There are currently two types of requests:
 * <ul>
 *  <li>Heartbeat - which just informs the GS Pro of the sim status</li>
 *  <li>Shot Data - which requests the GS Pro launch the ball</li>
 * </ul>
 * 
 * @author kenjdavidson
 */
public class RequestBuilder {        
    private String deviceId;
    private Units units;
    private String version;
    private Integer shotNumber;

    public static RequestBuilder create(String deviceId, Units units) {
        return new RequestBuilder(deviceId, units);
    }

    private RequestBuilder(String deviceId, Units units) {
        this.deviceId = deviceId;
        this.units = units;
        this.version = "1";
        this.shotNumber = 1;
    }

    public Request shot(BallData ballData) {
        return shot(ballData, null);
    }

    public Request shot(BallData ballData, ClubData clubData) {
        return Request.builder()
            .deviceId(deviceId)
            .units(units)            
            .version(version)
            .shotNumber(shotNumber++)
            .ballData(ballData)
            .clubData(clubData)
            .shotDataOptions(Options.builder()
                .ballData(ballData != null)
                .clubData(clubData != null)
                .heartBeat(false)
                .monitorReady(false)
                .ballData(false)
                .build()
            )
            .build();
    }

    public Request heartbeat(boolean ready, boolean ballDetected) {
        return Request.builder()
            .deviceId(deviceId)
            .units(units)            
            .version(version)
            .shotDataOptions(Options.builder()
                .ballData(false)
                .clubData(false)
                .heartBeat(true)
                .monitorReady(ready)
                .ballData(ballDetected)
                .build()
            )
            .build();
    }
}