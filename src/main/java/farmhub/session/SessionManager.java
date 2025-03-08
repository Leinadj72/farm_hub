package farmhub.session;

public class SessionManager {
    private static SessionManager instance;
    private CurrentUser currentUser;

    private SessionManager() {}

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public synchronized void setCurrentUser(CurrentUser user) {
        this.currentUser = user;
    }

    public synchronized CurrentUser getCurrentUser() {
        return currentUser;
    }

    public synchronized void clearSession() {
        currentUser = null;
    }

    public synchronized boolean isUserLoggedIn() {
        return currentUser != null;
    }
}
