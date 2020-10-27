package se.experis.noticeboard.Utils;

import java.util.HashMap;
import java.util.Map;

public class SessionKeeper {

  private static SessionKeeper instance = null;
  private Map<String, SessionUser> validSessions = new HashMap<>(); // sessionId => User: { timestamp, accountId }
  private final int MAX_SESSION_TIME = 10 * 60 * 1000; // 10 minutes in ms

  public static SessionKeeper getInstance() {
    if (instance == null) {
      instance = new SessionKeeper();
    }
    return instance;
  }

  public void addSession(String sessionId, long accountId) {
    validSessions.put(sessionId, new SessionUser(System.currentTimeMillis(), accountId));    
  }

  public void removeSession(String sessionId) {
    if (validSessions.get(sessionId) != null) {
      validSessions.remove(sessionId);
    }
  }

  public Long getSessionAccountId(String sessionId) {
    var user = validSessions.get(sessionId);
    if(user != null) {
      boolean timedOut = System.currentTimeMillis() > user.timestamp + MAX_SESSION_TIME;
      if(!timedOut) {
        user.timestamp = System.currentTimeMillis();
        return user.accountId;
      } else {
        removeSession(sessionId);
      }
    }
    return null;
  }

  public boolean isLoggedIn(String sessionId) {
    return getSessionAccountId(sessionId) != null;
  }
}

class SessionUser {
  long timestamp;
  long accountId;

  public SessionUser(long timestamp, long id) {
    this.timestamp = timestamp;
    this.accountId = id;
  }

}
