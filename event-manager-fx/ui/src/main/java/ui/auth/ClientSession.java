package ui.auth;

public class ClientSession {
    private static String authToken;

    public static String getAuthToken() {
        return authToken;
    }

    public static void setAuthToken(String authToken) {
        ClientSession.authToken = authToken;
    }

    public static void clearAuthToken(){authToken = "";}
}
