package kjd.gspro.app.bridge;

import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ShotHistoryService {
    @Getter
    private ObservableList<Shot> shotHistory;

    public ShotHistoryService() {
        this.shotHistory = FXCollections.observableArrayList();
    }
}
