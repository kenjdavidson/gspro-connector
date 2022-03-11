package kjd.gspro.desktop.monitor;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import kjd.gspro.data.BallData;
import kjd.gspro.data.ClubData;
import kjd.gspro.data.Player;
import kjd.gspro.monitor.LaunchMonitor;

public class ThreeClickLaunchMonitor implements LaunchMonitor {

    @Override
    public void playerChange(Player player) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onPlayerChange(Consumer<Player> onPlayerChange) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onReadyStateChange(Consumer<Boolean> onReadyStateChange) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onBallStateChange(Consumer<Boolean> onBallStateChange) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onShot(BiConsumer<BallData, ClubData> onShot) {
        // TODO Auto-generated method stub
        
    }
    
}
