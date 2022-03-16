package kjd.gspro.app.bridge;

import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LogService {
    private ObservableList<LogEntry> entries;

    public LogService() {
        entries = FXCollections.observableArrayList();   
    }

    public ObservableList<LogEntry> getEntries() {
        return entries;
    }
}
