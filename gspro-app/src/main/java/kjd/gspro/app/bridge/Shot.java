package kjd.gspro.app.bridge;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import kjd.gspro.data.BallData;
import kjd.gspro.data.ClubData;
import lombok.Getter;

@Getter
public class Shot {
    private FloatProperty ballSpeed = new SimpleFloatProperty(this, "ballSpeed");
    private FloatProperty spinAxis = new SimpleFloatProperty(this, "spinAxis");
    private FloatProperty totalSpin = new SimpleFloatProperty(this, "totalSpin");
    private FloatProperty backSpin = new SimpleFloatProperty(this, "backSpin");
    private FloatProperty sideSpin = new SimpleFloatProperty(this, "sideSpin");
    private FloatProperty hla = new SimpleFloatProperty(this, "hla");
    private FloatProperty vla = new SimpleFloatProperty(this, "vla");
    private FloatProperty carryDistance = new SimpleFloatProperty(this, "carryDistance");
    private FloatProperty clubSpeed = new SimpleFloatProperty(this, "clubSpeed");
    private FloatProperty attackAngle = new SimpleFloatProperty(this, "attackAngle");
    private FloatProperty faceAngle = new SimpleFloatProperty(this, "faceAngle");

    public Shot(BallData ballData) {
        this(ballData, null);
    }

    public Shot(BallData ballData, ClubData clubData) {
        ballSpeed.setValue(ballData.getSpeed());
        spinAxis.setValue(ballData.getSpinAxis());
        totalSpin.setValue(ballData.getTotalSpin());
        backSpin.setValue(ballData.getBackSpin());
        sideSpin.setValue(ballData.getSideSpin());
        hla.setValue(ballData.getHla());
        vla.setValue(ballData.getVla());
        carryDistance.setValue(ballData.getCarryDistance());

        if (clubData != null) {
            clubSpeed.setValue(clubData.getSpeed());
            attackAngle.setValue(clubData.getAttackAngle());
            faceAngle.setValue(clubData.getFaceAngle());
        }
    }
}
