package kjd.gspro.garmin.r10;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Message {
    @JsonProperty("Type")
    private Type type;

    @JsonProperty("SubType")
    private String subType;

    @JsonProperty("ClubType")
    private Object clubType;

    @JsonProperty("BallData")
    private Object ballData;

    @JsonProperty("ClubData")
    private Object clubData;
}
