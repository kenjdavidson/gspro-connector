package kjd.gspro.app.bridge;

import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;

@Component
public class ShotHistoryService {
    private ObservableList<Shot> shots;

    public ShotHistoryService() {
        this.shots = new SimpleListProperty<>(this, "shots");
    }
}
