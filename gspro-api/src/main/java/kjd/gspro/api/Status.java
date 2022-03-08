package kjd.gspro.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Status {
    @JsonProperty("IsConnected")
    private Boolean connected;

    @JsonProperty("Message")
    private String message;
}
