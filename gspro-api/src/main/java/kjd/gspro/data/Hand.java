package kjd.gspro.data;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Hand {
    RIGHT("RH"),
    LEFT("LH");

    @JsonValue
    public final String abbr;
    
    private Hand(String abbr) {
        this.abbr = abbr;
    }

    public static Hand fromAbbr(String abbr) {
        if (abbr.equals(RIGHT.abbr)) return RIGHT;
        if (abbr.equals(LEFT.abbr)) return LEFT;
        throw new EnumConstantNotPresentException(Hand.class, abbr);
    }
}
