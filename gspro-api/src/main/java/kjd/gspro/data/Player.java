package kjd.gspro.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NonNull;

/**
 * Manage player information between the GSPro and launch monitor.
 * 
 * @author kenjdavidson
 */
@Data
public class Player {
    @NonNull
    @JsonProperty("Handed")
    private Hand handed;

    @NonNull
    @JsonProperty("Club")
    private Club club;

    @JsonProperty
    private Float distanceToTarget;    
}
