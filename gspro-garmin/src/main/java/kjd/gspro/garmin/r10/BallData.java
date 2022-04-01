package kjd.gspro.garmin.r10;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BallData {
    @JsonProperty("BallSpeed")
    private float speed;
    
    @JsonProperty("SpinAxis")
    private float spinAxis;

    @JsonProperty("TotalSpin")
    private float totalSpin;

    @JsonProperty("LaunchDirection")
    private float launchDirection;

    @JsonProperty("LaunchAngle")
    private float launchAngle;
}
