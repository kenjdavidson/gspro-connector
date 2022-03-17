package kjd.gspro.app.bridge;

public enum ConnectionStatus {
    DISCONNECTING("Disconnecting"),
    DISCONNECTED("Connect"),
    CONNECTING("Connecting"),
    CONNECTED("Disconnect");

    final public String command;
    private ConnectionStatus(String command) {
        this.command = command;
    }
}
