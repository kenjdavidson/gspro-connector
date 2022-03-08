package kjd.gspro.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Provides the configuration of the incoming data, whether different information is available.
 * <p>
 * Required for all messages.
 * <p>
 * 
 * @author kenjdavidson
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class Options {
    @NonNull
    @JsonProperty("ContainsBallData")
    private Boolean ballData;

    @NonNull
    @JsonProperty("ContainsClubData")
    private Boolean clubData;

    @JsonProperty("LaunchMonitorIsReady")
    private Boolean monitorReady;

    @JsonProperty("LaunchMontiorBallDetected")
    private Boolean monitorBallDetected;

    @JsonProperty("IsHeartBeat")
    private Boolean heartBeat;

    public Options() {
        this(false, false, true, true, false);
    }
}
