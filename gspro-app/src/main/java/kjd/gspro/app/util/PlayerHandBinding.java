package kjd.gspro.app.util;

import java.util.Optional;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import kjd.gspro.data.Hand;
import kjd.gspro.data.Player;

public class PlayerHandBinding extends StringBinding {
    ObjectProperty<Player> player;
    
    public PlayerHandBinding(ObjectProperty<Player> player) {
        super.bind(player);
        this.player = player;
    }

    @Override
    protected String computeValue() {
        return Optional.ofNullable(player.getValue())
            .map(Player::getHanded)
            .map(Hand::toString)
            .orElse("");
    }  
}
