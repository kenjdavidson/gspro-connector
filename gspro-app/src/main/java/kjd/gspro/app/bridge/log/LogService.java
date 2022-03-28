package kjd.gspro.app.bridge.log;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kjd.gspro.app.bridge.StatusEvent;
import kjd.gspro.app.bridge.gspro.GSProRequestEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class LogService {

    final StatusLogConverter statusConverter;
    final RequestLogConverter requestConverter;

    @Getter
    private ObservableList<LogEntry> entries = FXCollections.observableArrayList();

    @EventListener
    public void onStatusEvent(StatusEvent event) {
        log.debug("Processing StatusEvent {}", event);
        entries.add(statusConverter.convert(event.getSource(), event.getStatus()));
    }

    @EventListener 
    public void onRequestEvent(GSProRequestEvent event) {
        log.debug("processing RequestEvent {}", event);
        entries.add(requestConverter.convert(event.getSource(), event.getRequest()));
    }

}
