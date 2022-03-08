package kjd.gspro.data;

import java.util.Arrays;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Club {
    DRIVER("DR"),
    THREE_WOOD("3W"),
    FIVE_WOOD("5W"),
    SEVEN_WOOD("7W"),
    THREE_HYBRID("3H"),
    FOUR_HYBRID("4H"),
    FIVE_HYBRID("5H"),
    THREE_IRON("3I"),
    FOUR_IRON("4I"),
    FIVE_IRON("5I"),
    SIX_IRON("6I"),
    SEVEN_IRON("7I"),
    EIGHT_IRON("8I"),
    NINE_IRON("9I"),
    PITCHING_WEDGE("PW"),
    GAP_WEDGE("GW"),
    SAND_WEDGE("SW"),
    LOB_WEDGE("LW"),
    PUTTER("P");

    @JsonValue
    public final String abbr;
    
    private Club(String abbr) {
        this.abbr = abbr;
    }

    public static Club fromAbbr(String abbr) {
        Optional<Club> club = Arrays.stream(Club.values())
            .filter(c -> c.abbr.equals(abbr))
            .findFirst();

        return club.orElseThrow(() -> new EnumConstantNotPresentException(Club.class, abbr));
    }
}
