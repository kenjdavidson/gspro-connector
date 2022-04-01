package kjd.gspro.garmin.r10;

public enum Type {
    HANDSHAKE("Hanshake"),
    CHALLENGE("Challenge"),
    DISCONNECT("Disconnect"),
    PING("Ping"),
    PONG("Pong"),
    CLUB_TYPE("SetClubType"),
    BALL_TYPE("SetBallType"),
    SEND_SHOT("SendShot"),
    UNKNOWN("Unknown");

    public final String command;
    private Type(String command) {
        this.command = command;
    }

    public static Type fromCommand(String command) {
        for (Type type : Type.values()) {
            if (type.command.equals(command)) {
                return type;
            }
        }

        return UNKNOWN;
    }
}
