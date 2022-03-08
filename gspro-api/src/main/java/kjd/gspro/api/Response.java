package kjd.gspro.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import kjd.gspro.data.Player;
import lombok.Data;

/**
 * Request/Command from the GS Pro to the monitor/simulator.
 * <p>
 * The following codes are available:
 * <ul>
 *  <li><strong>200</strong> Shot received successfully</li>
 *  <li><strong>201</strong> Player information</li>
 *  <li><strong>501/5XX</strong> Failure ocurred</li>
 * </ul>
 * 
 * @author kenjdavidson
 */
@Data
public class Response {
    @JsonProperty("Code")
    private Integer code;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("Player")
    private Player player;
}
