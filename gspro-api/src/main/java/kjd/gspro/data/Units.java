package kjd.gspro.data;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Units {
    YARDS("Yards"),
    METERS("Meters");

    @JsonValue
    public final String abbr;

    private Units(String abbr) {
        this.abbr = abbr;
    }

    public static Units fromAbbr(String abbr) {
        if (abbr.equals(YARDS.abbr)) return YARDS;
        if (abbr.equals(METERS.abbr)) return METERS;
        throw new EnumConstantNotPresentException(Hand.class, abbr);
    }
}
