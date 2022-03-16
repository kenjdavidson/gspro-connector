package kjd.gspro.api;

public interface ConnectionListener {
    void onStatus(Status status);
    void onError(Status status, Throwable t);    
}
