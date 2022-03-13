package kjd.gspro.app.monitor;

import java.lang.ref.WeakReference;
import java.util.Properties;

import kjd.gspro.data.Player;
import kjd.gspro.monitor.LaunchMonitor;
import lombok.Getter;

public class FormLaunchMonitor implements LaunchMonitor {

    @Getter
    private Properties properties;

    private WeakReference<Listener> listener;

    public FormLaunchMonitor(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void notifyPlayer(Player player) {
        // Player changed (hand/club) update accordingly
    }

    @Override
    public void addListener(Listener listener) {
        this.listener = new WeakReference<LaunchMonitor.Listener>(listener);
    }
    
}
