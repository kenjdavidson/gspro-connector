package kjd.gspro.app.monitor;

import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.Optional;
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
        System.out.println(MessageFormat.format("Changing player {0}", player));
    }

    @Override
    public void addListener(Listener listener) {
        this.listener = new WeakReference<LaunchMonitor.Listener>(listener);
    }

    @Override
    public Listener removeListener(Listener listener) {
        Listener l = this.listener.get();
        this.listener.clear();
        return l;
    }

    /**
     * Connect always completes successfully.
     */
	@Override
	public void connect() {
        Optional.of(listener.get()).ifPresent(l -> l.onConnected());
	}
    
    /**
     * Disconnect always completes successfully.
     */
	@Override
	public void disconnect() {
        Optional.of(listener.get()).ifPresent(l -> l.onDisconnected());
	}
    
}
