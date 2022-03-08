package kjd.gspro.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Club management information for the GSPro.
 * <p>
 * When provided {@link Options#getClubData()} should be true.
 * <p>
 * 
 * @author kenjdavidson
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ClubData {
    @JsonProperty("Speed")
    private Float speed;

    @JsonProperty("AngleOfAttack")
    private Float attackAngle;

    @JsonProperty("FaceToTarget")
    private Float faceAngle;

    @JsonProperty("Lie")
    private Float lie;

    @JsonProperty("Loft")
    private Float loft;

    @JsonProperty("Path")
    private Float path;

    @JsonProperty("SpeedAtImpact")
    private Float impactSpeed;

    @JsonProperty("VerticalFaceImpact")
    private Float verticalFaceImpact;

    @JsonProperty("HorizontalFaceImpact")
    private Float horizontalFaceImpact;

    @JsonProperty("ClosureRate")
    private Float closureRate;
}
