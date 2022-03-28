package kjd.gspro.app.bridge.log;

public interface LogEntryConverter<T> {
    LogEntry convert(Object source, T item);
}
