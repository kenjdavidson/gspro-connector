package kjd.gspro.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import kjd.gspro.data.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request/Command from the GS Pro to the monitor/simulator.
 * <p>
 * The following codes are available:
 * <ul>
 *  <li><strong>100</strong> Bridge informational code</li>
 *  <li><strong>200</strong> Shot received successfully</li>
 *  <li><strong>201</strong> Player information</li>
 *  <li><strong>501/5XX</strong> Failure ocurred</li>
 * </ul>
 * 
 * @author kenjdavidson
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder(value = {"code", "message", "player", "connected"})
public class Status {
    public static final Status connecting() {
        return Status.builder()
            .code(101)
            .connected(false)
            .message("Connecting to GS Pro...")
            .build();
    }

    public static final Status connected() {
        return Status.builder()
            .code(102)
            .connected(true)
            .message("Connected to GS Pro!")
            .build();
    }

    public static final Status disconnected() {
        return Status.builder()
            .code(103)
            .connected(false)
            .message("Disconnected.")
            .build();
    }

    public static final Status error(Throwable t, Boolean connected) {
        return Status.builder()
            .code(500)
            .connected(connected)
            .message(t.getMessage())
            .build();
    }

    @JsonProperty("Code")
    private Integer code;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("Player")
    private Player player;

    private boolean connected;
}
