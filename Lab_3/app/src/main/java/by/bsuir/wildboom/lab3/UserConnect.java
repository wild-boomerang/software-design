package by.bsuir.wildboom.lab3;

import java.io.Serializable;

public class UserConnect implements Serializable {
    private String connectionId;
    private User user;

    public UserConnect(User user, String connectionId) {
        this.user = user;
        this.connectionId = connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
