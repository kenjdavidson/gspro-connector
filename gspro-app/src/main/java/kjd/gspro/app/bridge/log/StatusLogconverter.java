package kjd.gspro.app.bridge.log;

import org.springframework.stereotype.Component;

import kjd.gspro.api.Status;

@Component
public class StatusLogconverter implements LogEntryConverter<Status> {

    @Override
    public LogEntry convert(Object source, Status status) {
        return new LogEntry(type(status.getCode()), system(source), status.getMessage());
    }    

    private LogEntry.Type type(Integer code) {
        return (code < 500) ? LogEntry.Type.INFO : LogEntry.Type.ERROR;
    }

    private String system(Object source) {
        return String.format("%-20s", source.getClass().getSimpleName());
    }
}
