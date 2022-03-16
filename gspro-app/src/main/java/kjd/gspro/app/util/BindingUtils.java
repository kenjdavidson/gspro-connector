package kjd.gspro.app.util;

import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;

public abstract class BindingUtils {
    public static StringBinding stringBinding(ResourceBundle rb, 
            BooleanProperty observable,
            String when,
            String otherwise) {
        return Bindings.when(observable)
            .then(rb.getString(when))
            .otherwise(rb.getString(otherwise));
    }
}
