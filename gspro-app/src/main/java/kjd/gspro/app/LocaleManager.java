package kjd.gspro.app;

import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

import lombok.Getter;

/**
 * Manage application locale.  I'm not sure how important this is to have, I can't imagine anyone would
 * have their OS set to English but want anything else on an application (ie. they'd have their OS set
 * accordingly).
 * <p>
 * May be removed.
 * 
 * @author kenjdavidson
 * @deprecated debating the requirement for this since the OS Locale.getDefault() should be correct
 */
@Component
public class LocaleManager {

    public static final String NAME = "i18n";

    Locale locale = Locale.getDefault();

    @Getter
    ResourceBundle resources = ResourceBundle.getBundle(NAME, locale);

    public ResourceBundle changeLocale(String locale) {
        return changeLocale(Locale.forLanguageTag(locale));
    }

    public ResourceBundle changeLocale(Locale locale) {
        this.locale = locale;
        this.resources = ResourceBundle.getBundle(NAME, locale);
        return this.resources;
    }
}
