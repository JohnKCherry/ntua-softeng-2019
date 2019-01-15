package gr.ntua.ece.softeng18b.data.model;

import java.util.Objects;

public class User {

    private final long id;
    private final String fullname;
    private final String username;
    private final String email;
    private final String status;

    public User(long id, String fullname, String username, String email, int authorization) {
        this.id          = id;
        this.fullname    = fullname;
        this.username    = username;
        this.email       = email;
        if(authorization == 2) this.status = "Active Crowdsourcer";
        else if(authorization == 1) this.status = "Blocked Crowdsourcer";
        else if(authorization == 3) this.status = "Administrator";
        else this.status = "Unidentified user authorization";
    }

    public long getId() {
        return id;
    }

    public String getFullName() {
        return fullname;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
