package kjd.gspro.garmin.r10;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClubData {
    @JsonProperty("ClubHeadSpeed")
    private float speed;
}
