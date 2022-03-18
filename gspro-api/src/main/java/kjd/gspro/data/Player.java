package kjd.gspro.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Manage player information between the GSPro and launch monitor.
 * 
 * @author kenjdavidson
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@JsonPropertyOrder(value = {"handed", "club", "distanceToTarget"})
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
