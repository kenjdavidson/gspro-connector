package kjd.gspro.app.monitor.form;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import kjd.gspro.data.BallData;
import kjd.gspro.data.ClubData;
import kjd.gspro.monitor.LaunchMonitor;
import kjd.gspro.monitor.LaunchMonitorController;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FormLaunchMonitorController implements LaunchMonitorController, Initializable {

    ObjectProperty<FormLaunchMonitor> launchMonitor = new SimpleObjectProperty<>(this, "launchMonitor");

    @FXML ToggleButton monitorReadyButton;
    @FXML ToggleButton ballDetectedButton;
    @FXML Button hitShotButton;

    @FXML Slider ballSpeed;
    @FXML Slider ballSpinAxis;
    @FXML Slider ballTotalSpin;
    @FXML Slider ballLaunchAngle;
    @FXML Slider ballLaunchDirection;
    @FXML Slider ballCarryDistance;
    @FXML Slider clubSpeed;
    @FXML Slider clubAttackAngle;
    @FXML Slider clubFaceAngle;
    
    @FXML Button driverPreset;
    @FXML Button wood3Preset;
    @FXML Button wood5Preset;
    @FXML Button iron4Preset;
    @FXML Button iron5Preset;
    @FXML Button iron6Preset;
    @FXML Button iron7Preset;
    @FXML Button iron8Preset;
    @FXML Button iron9Preset;
    @FXML Button pwPreset;
    @FXML Button gwPreset;

    @Override
    public void setLaunchMonitor(LaunchMonitor launchMonitor) {
        this.launchMonitor.setValue((FormLaunchMonitor) launchMonitor);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("Initializing the FormMonitorController/View");

        monitorReadyButton.disableProperty().bind(launchMonitor.isNull());
        monitorReadyButton.setOnAction(this::onMonitorReady);
        ballDetectedButton.disableProperty().bind(launchMonitor.isNull());
        ballDetectedButton.setOnAction(this::onBallDetected);
        hitShotButton.disableProperty().bind(launchMonitor.isNull());        
        hitShotButton.setOnAction(this::onHitShot);

        driverPreset.setOnAction(e -> setValues(FormLaunchPreset.DRIVER));
        wood3Preset.setOnAction(e -> setValues(FormLaunchPreset.THREE_WOOD));
        wood5Preset.setOnAction(e -> setValues(FormLaunchPreset.FIVE_WOOD));
        wood5Preset.setOnAction(e -> setValues(FormLaunchPreset.FIVE_WOOD));
        iron4Preset.setOnAction(e -> setValues(FormLaunchPreset.FOUR_IRON));
        iron5Preset.setOnAction(e -> setValues(FormLaunchPreset.FIVE_IRON));
        iron6Preset.setOnAction(e -> setValues(FormLaunchPreset.SIX_IRON));
        iron7Preset.setOnAction(e -> setValues(FormLaunchPreset.SEVEN_IRON));
        iron8Preset.setOnAction(e -> setValues(FormLaunchPreset.EIGHT_IRON));
        iron9Preset.setOnAction(e -> setValues(FormLaunchPreset.NINE_IRON));
        pwPreset.setOnAction(e -> setValues(FormLaunchPreset.PITCHING_WEDGE));
        gwPreset.setOnAction(e -> setValues(FormLaunchPreset.GAP_WEDGE));
    }
    
    private void setValues(FormLaunchPreset preset) {
        log.debug("Setting shot presets for {}", preset.name());
        ballSpeed.setValue(preset.ballSpeed);
        ballSpinAxis.setValue(preset.ballSpinAxis);
        ballTotalSpin.setValue(preset.ballTotalSpin);
        ballLaunchAngle.setValue(preset.ballLaunchAngle);
        ballLaunchDirection.setValue(preset.ballLaunchDirection);
        ballCarryDistance.setValue(preset.ballCarryDistance);

        clubSpeed.setValue(preset.clubSpeed);
        clubAttackAngle.setValue(preset.clubAttackAngle);
        clubFaceAngle.setValue(preset.clubFaceAngle);
    }

    @FXML
    public void onHitShot(Event event) {
        log.debug("Manual sending shot data");
        monitorReadyButton.setSelected(false);
        ballDetectedButton.setSelected(false);

        Optional.ofNullable(launchMonitor.getValue()).ifPresent(lm -> {
            lm.sendShot(balldata(), clubdata());
        });
    }

    @FXML
    public void onMonitorReady(Event event) {
        log.debug("Manual monitor ready");
        Optional.ofNullable(launchMonitor.getValue()).ifPresent(lm -> {
            lm.monitorReady(monitorReadyButton.isSelected(), ballDetectedButton.isSelected());
        });
    }

    @FXML 
    public void onBallDetected(Event event) {
        log.debug("Manual ball detected");
        Optional.ofNullable(launchMonitor.getValue()).ifPresent(lm -> {
            lm.monitorReady(monitorReadyButton.isSelected(), ballDetectedButton.isSelected());
        });
    }

    private BallData balldata() {
        return BallData.builder()
            .speed((float) ballSpeed.getValue())
            .spinAxis((float) ballSpinAxis.getValue())
            .totalSpin((float) ballTotalSpin.getValue())
            .hla((float) ballLaunchAngle.getValue())
            .vla((float) ballLaunchDirection.getValue())
            .carryDistance((float) ballCarryDistance.getValue())
            .build();
    }

    private ClubData clubdata() {
        return ClubData.builder()
            .speed((float) clubSpeed.getValue())
            .attackAngle((float) clubAttackAngle.getValue())
            .faceAngle((float) clubFaceAngle.getValue())
            .build();
    }
}

