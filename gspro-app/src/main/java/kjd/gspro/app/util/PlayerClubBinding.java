package kjd.gspro.app.util;

import java.util.Optional;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import kjd.gspro.data.Club;
import kjd.gspro.data.Player;

public class PlayerClubBinding extends StringBinding {
    ObjectProperty<Player> player;
    public PlayerClubBinding(ObjectProperty<Player> player) {
        super.bind(player);
        this.player = player;
    }

    @Override
    protected String computeValue() {
        return Optional.ofNullable(player.getValue())
            .map(Player::getClub)
            .map(Club::toString)
            .orElse("");
    }  
}
