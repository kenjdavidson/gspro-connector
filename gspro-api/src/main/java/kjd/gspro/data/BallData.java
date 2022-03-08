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
 * Ball flight information.
 * <p>
 * When provided {@link Options#setBallData(Boolean)} should be true.
 * <p>
 * 
 * @author kenjdavidson
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class BallData {
    @NonNull 
    @JsonProperty("Speed")
    private Float speed;
    
    @NonNull 
    @JsonProperty("SpinAxis") 
    private Float spinAxis;

    @NonNull 
    @JsonProperty("TotalSpin")
    private Float totalSpin;
    
    @NonNull 
    @JsonProperty("HLA") 
    private Float hla;
    
    @NonNull 
    @JsonProperty("VLA")
    private Float vla;

    @JsonProperty("BackSpin")
    private Float backSpin;

    @JsonProperty("SideSpin")
    private Float sideSpin;

    @JsonProperty("CarryDistance")
    private Float carryDistance;
}
