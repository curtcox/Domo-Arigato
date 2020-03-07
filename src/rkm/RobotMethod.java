package rkm;


/**
 * Methods of IRobot for the stream.
 */
public enum RobotMethod
{
    KEY_PRESS("KP"),
    KEY_RELEASE("KR"),
    MOUSE_MOVE("MM"),
    MOUSE_PRESS("MP"),
    MOUSE_RELEASE("MR"),
    MOUSE_WHEEL("MW"),
    CREATE_SCREEN_CAPTURE("SC"),
    START_REQUESTS("StartRequests");

    private final String token;

    RobotMethod(String token) {
        this.token = token;
    }

    public boolean matches(String line) {
        return line.equals(token);
    }

    public static RobotMethod parse(String string) {
        for (RobotMethod method : values()) {
            if (method.matches(string)) {
                return method;
            }
        }
        throw new IllegalArgumentException(string);
    }

    public String getToken() {
        return token;
    }
}
