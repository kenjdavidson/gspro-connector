package kjd.gspro.app.bridge.shot;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import kjd.gspro.data.BallData;
import kjd.gspro.data.Club;
import kjd.gspro.data.ClubData;
import kjd.gspro.data.Hand;
import kjd.gspro.data.Player;
import lombok.Getter;

@Getter
public class Shot {
    private Property<Hand> hand = new SimpleObjectProperty<>(this, "hand");
    private Property<Club> club = new SimpleObjectProperty<>(this, "club");
    private Property<Number> ballSpeed = new SimpleFloatProperty(this, "ballSpeed");
    private Property<Number> spinAxis = new SimpleFloatProperty(this, "spinAxis");
    private Property<Number> totalSpin = new SimpleFloatProperty(this, "totalSpin");
    private Property<Number> backSpin = new SimpleFloatProperty(this, "backSpin");
    private Property<Number> sideSpin = new SimpleFloatProperty(this, "sideSpin");
    private Property<Number> hla = new SimpleFloatProperty(this, "hla");
    private Property<Number> vla = new SimpleFloatProperty(this, "vla");
    private Property<Number> carryDistance = new SimpleFloatProperty(this, "carryDistance");
    private Property<Number> clubSpeed = new SimpleFloatProperty(this, "clubSpeed");
    private Property<Number> attackAngle = new SimpleFloatProperty(this, "attackAngle");
    private Property<Number> faceAngle = new SimpleFloatProperty(this, "faceAngle");

    public Shot(Player player, BallData ballData) {
        this(player, ballData, null);
    }

    public Shot(Player player, BallData ballData, ClubData clubData) {
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
