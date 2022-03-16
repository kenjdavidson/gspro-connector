package kjd.gspro.app;

import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Getter;

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