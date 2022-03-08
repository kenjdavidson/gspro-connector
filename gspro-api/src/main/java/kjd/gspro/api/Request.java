package kjd.gspro.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import kjd.gspro.data.BallData;
import kjd.gspro.data.ClubData;
import kjd.gspro.data.Options;
import kjd.gspro.data.Units;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Message for communicate from the monitor to the GS Pro.
 * <p>
 * {@link #shotNumber} is the unqiue id used to reference messages and responses between the bridge
 * and the GSPro.  This should be an auto-incremented integer (will look into the best way to manage
 * this, whether it be statically set for each new request or sent during processing).
 * <p>
 * 
 * @author kenjdavidson
 */
public class Request {

    @Getter
    @JsonProperty("DeviceID")
    private String deviceId;  

    @Getter
    @Setter
    @JsonProperty("Units")
    private Units units;

    @Getter
    @Setter(AccessLevel.PACKAGE)
    @JsonProperty("ShotNumber")
    private Integer shotNumber; 

    @Getter
    @Setter(AccessLevel.PACKAGE)
    @JsonProperty("APIVersion")
    private String version;

    @Getter
    @JsonProperty("BallData")
    private BallData ballData;

    @Getter
    @JsonProperty("ClubData")
    private ClubData clubData;

    @Getter
    @NonNull
    @JsonProperty("ShotDataOptions")
    private Options shotDataOptions;

    public Request(String deviceId, Integer shotNumber) {
        this(deviceId, shotNumber, null, null);
    }

    public Request(String deviceId, Integer shotNumber, BallData ballData) {
        this(deviceId, shotNumber, ballData, null);
    }

    public Request(String deviceId, Integer shotNumber, ClubData clubData) {
        this(deviceId, shotNumber, null, clubData);
    }

    public Request(String deviceId, Integer shotNumber, BallData ballData, ClubData clubData) {
        this.deviceId = deviceId;
        this.shotNumber = shotNumber;  
        this.shotDataOptions = new Options();
        this.setBallData(ballData);
        this.setClubData(clubData);
    }

    public void setBallData(BallData data) {
        this.ballData = data;
        this.shotDataOptions.setBallData(data != null);
    }

    public void setClubData(ClubData data) {
        this.clubData = data;
        this.shotDataOptions.setClubData(data != null);
    }
}
