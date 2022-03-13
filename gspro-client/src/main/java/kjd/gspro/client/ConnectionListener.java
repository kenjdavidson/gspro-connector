package kjd.gspro.client;

import kjd.gspro.api.Status;

public interface ConnectionListener {
    void onStatus(Status status);
    void onError(Status status, Throwable t);    
}
