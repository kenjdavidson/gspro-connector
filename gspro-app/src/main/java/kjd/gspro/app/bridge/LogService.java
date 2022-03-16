package kjd.gspro.app.bridge;

import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LogService {
    @Getter
    private ObservableList<LogEntry> entries;

    public LogService() {
        entries = FXCollections.observableArrayList();   
    }
}
