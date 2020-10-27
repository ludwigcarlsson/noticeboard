package se.experis.noticeboard.Utils;

import java.util.HashMap;
import java.util.Map;

public class SessionKeeper {

  private static SessionKeeper instance = null;
  private Map<String, Long> validSessions = new HashMap<>(); // stores session id + timestamp
  private final int MAX_SESSION_TIME = 10 * 60 * 1000; // 10 minutes in ms

  public static SessionKeeper getInstance() {
    if (instance == null) {
      instance = new SessionKeeper();
    }
    return instance;
  }

  /**
   * Checks if session exists and if so, if existing timestamp was less than MAX_SESSION_TIME ms ago
   * @param session id
   * @param updateTimestamp whether to refresh user timestamp or not 
   * @return logged in status
   */
  public boolean checkSession(String session, boolean updateTimestamp) {
    var timestamp = validSessions.get(session);
    boolean isLoggedIn = timestamp != null && System.currentTimeMillis() < timestamp + MAX_SESSION_TIME;
    if(isLoggedIn && updateTimestamp) {
      updateSession(session);
    }

    return isLoggedIn;
  }


  public void updateSession(String session) {
    validSessions.put(session, System.currentTimeMillis());
  }

  public void removeSession(String session) {
    if (validSessions.get(session) != null) {
      validSessions.remove(session);
    }
  }

}
