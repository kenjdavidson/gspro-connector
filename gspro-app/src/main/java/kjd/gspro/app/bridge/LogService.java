package kjd.gspro.app.bridge;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kjd.gspro.api.Status;
import kjd.gspro.app.bridge.LogEntry.Type;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LogService {
    @Getter
    private ObservableList<LogEntry> entries;

    public LogService() {
        log.debug("Initializing service");
        entries = FXCollections.observableArrayList();   
    }

    @EventListener
    public void onStatusEvent(StatusEvent event) {
        log.debug("Processing StatusEvent {}", event);
        LogEntry entry = log(event.getSource(), event.getStatus());
        entries.add(entry);
    }

    private LogEntry log(Object source, Status status) {
        return new LogEntry(type(status.getCode()), system(source), status.getMessage());
    }

    private Type type(Integer code) {
        return (code < 500) ? Type.INFO : Type.ERROR;
    }

    private String system(Object source) {
        return String.format("%-20s", source.getClass().getSimpleName());
    }
}
