package se.experis.noticeboard.Utils;

import java.util.HashMap;
import java.util.Map;

public class SessionKeeper {

  private static SessionKeeper instance = null;
  private Map<String, User> validSessions = new HashMap<>(); // stores session id + timestamp
  private final int MAX_SESSION_TIME = 10 * 60 * 1000; // 10 minutes in ms

  public static SessionKeeper getInstance() {
    if (instance == null) {
      instance = new SessionKeeper();
    }
    return instance;
  }

  /**
   * Checks if session exists and if so, if existing timestamp was less than MAX_SESSION_TIME ms ago
   * @param sessionId id
   * @param updateTimestamp whether to refresh user timestamp or not 
   * @return logged in status
   */
  public boolean checkSession(String sessionId, boolean updateTimestamp) {
    var user = validSessions.get(sessionId);

    boolean isLoggedIn = user != null && System.currentTimeMillis() < user.timestamp + MAX_SESSION_TIME;
    if(isLoggedIn && updateTimestamp) {
      updateSession(sessionId);
    }

    return isLoggedIn;
  }

  public Long getSessionAccountId(String sessionId) {
    checkSession(sessionId, true);
    
    var user = validSessions.get(sessionId);
    if(user != null) {
      return user.accountId;
    }
    return null;
  }

  public void addSession(String sessionId, long accountId) {
    validSessions.put(sessionId, new User(System.currentTimeMillis(), accountId));
  }

  public void updateSession(String sessionId) {
    var result = validSessions.get(sessionId);
    result.timestamp = System.currentTimeMillis();
  }

  public void removeSession(String sessionId) {
    if (validSessions.get(sessionId) != null) {
      validSessions.remove(sessionId);
    }
  }

}

class User {
  long timestamp;
  long accountId;

  public User(long timestamp, long id) {
    this.timestamp = timestamp;
    this.accountId = id;
  }

}
