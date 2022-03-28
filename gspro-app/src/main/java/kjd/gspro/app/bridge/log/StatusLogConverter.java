package kjd.gspro.app.bridge.log;

import org.springframework.stereotype.Component;

import kjd.gspro.api.Status;

@Component
public class StatusLogConverter implements LogEntryConverter<Status> {

    @Override
    public LogEntry convert(Object source, Status status) {
        return new LogEntry(type(status.getCode()), source.getClass().getSimpleName(), status.getMessage());
    }    

    LogEntry.Type type(Integer code) {
        return (code < 500) ? LogEntry.Type.INFO : LogEntry.Type.ERROR;
    }
}
