package kjd.gspro.api;

public interface ConnectionListener {
    void onConnected(Status status);    
    void onStatus(Status status);
    void onDisconnect(Status status);
    void onError(Status status, Throwable t);    
}
