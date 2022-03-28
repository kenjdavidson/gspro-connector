package kjd.gspro.app.bridge.log;

import java.text.MessageFormat;

import org.springframework.stereotype.Component;

import kjd.gspro.api.Request;
import kjd.gspro.app.bridge.log.LogEntry.Type;

@Component
public class RequestLogConverter implements LogEntryConverter<Request> {

    @Override
    public LogEntry convert(Object source, Request item) {
        return new LogEntry(Type.INFO, source.getClass().getSimpleName(), message(item));
    }

    String message(Request request) {
        return request.getShotDataOptions().getHeartBeat() 
            ? heartbeat(request) 
            : request.getShotDataOptions().getBallData() 
                ? shot(request) 
                : monitor(request);
    }

    String heartbeat(Request request) {
        return MessageFormat.format("Sending heartbeat...", true);
    }

    String shot(Request request) {
        return MessageFormat.format("Sending shot data; ball data?: {0}, club data?: {1}",  
            request.getShotDataOptions().getBallData(), request.getShotDataOptions().getClubData());
    }

    String monitor(Request request) {
        return MessageFormat.format("Sending monitor state; ready: {0}, ball detected: {1}", 
            request.getShotDataOptions().getMonitorReady(), request.getShotDataOptions().getMonitorBallDetected());
    }
}
