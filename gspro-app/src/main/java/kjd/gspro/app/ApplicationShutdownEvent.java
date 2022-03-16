package kjd.gspro.app;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.context.ApplicationEvent;

/**
 * Shutdown request event.
 * <p>
 * Shutdown events can be listened to and requested to be stopped.  If any listener stops the event
 * they are required to provide a message that will be alerted to the user.  This should be used
 * incase any shutdown processing needs to occur.
 * <p>
 * <strong>Pending Shutdown</strong>
 * <pre><code>
 * </code></pre>
 * 
 * @author kenjdavidson
 */
public class ApplicationShutdownEvent extends ApplicationEvent {
    Map<Future<Boolean>, String> pending = new HashMap<>();

    public ApplicationShutdownEvent(Object source) {
        super(source);
    }

    void cancelShutdown(String message, Future<Boolean> future) {
        this.pending.put(future, message);
    }
}
