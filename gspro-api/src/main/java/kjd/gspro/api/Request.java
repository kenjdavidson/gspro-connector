package kjd.gspro.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import kjd.gspro.data.BallData;
import kjd.gspro.data.ClubData;
import kjd.gspro.data.Options;
import kjd.gspro.data.Units;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Request {

    @NonNull
    @JsonProperty("DeviceID")
    private String deviceId;  

    @NonNull
    @JsonProperty("Units")
    private Units units;

    @NonNull
    @JsonProperty("APIVersion")
    private String version;

    /**
     * Required for shots/club, but not heartbeat?
     */
    @JsonProperty("ShotNumber")
    private Integer shotNumber; 


    @JsonProperty("BallData")
    private BallData ballData;

    @JsonProperty("ClubData")
    private ClubData clubData;

    @NonNull
    @JsonProperty("ShotDataOptions")
    private Options shotDataOptions = new Options();;

    public void setBallData(BallData data) {
        this.ballData = data;
        this.shotDataOptions.setBallData(data != null);
    }

    public void setClubData(ClubData data) {
        this.clubData = data;
        this.shotDataOptions.setClubData(data != null);
    }
}
