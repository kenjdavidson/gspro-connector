package kjd.gspro.app.bridge.log;

import java.time.LocalDateTime;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;

/**
 * JavaFX "bean" used for handling/logging details.  This doesn't follow the standard
 * JavaFX bean providing the three methods {@code setValue}, {@code getValue} and 
 * {@code valueProperty} since using Lombok is less annoying.
 * <p>
 * The only real issue is that we can't use the build in 
 * {@link javafx.scene.control.cell.PropertyValueFactory} functionality in a number
 * of components.  This is a minor issue, as we can just directly reference the 
 * appropriate {@code get} method.
 * 
 * @author kenjdavidson
 */
@Getter
public class LogEntry {
    public static enum Type {
        ERROR,
        WARNING,
        INFO
    }

    Property<Type> type = new SimpleObjectProperty<>(this, "type");
    Property<LocalDateTime> date = new SimpleObjectProperty<>(this, "date");
    Property<String> systemName = new SimpleStringProperty(this, "systemName");
    Property<String> message = new SimpleStringProperty(this, "message");

    public LogEntry(Type type, String systemName, String message) {
        this.type.setValue(type);
        this.date.setValue(LocalDateTime.now());
        this.systemName.setValue(systemName);
        this.message.setValue(message);
    }
}
