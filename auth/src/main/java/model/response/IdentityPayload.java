package model.response;

import model.uid.UserIdentity;

public class IdentityPayload {
    private long id;
    private String username;
    private String role;

    public IdentityPayload() {}

    public IdentityPayload(long id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public IdentityPayload(UserIdentity userIdentity) {
        this.id = userIdentity.getId();
        this.username = userIdentity.getUsername();
        this.role = userIdentity.getRole();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "IdentityPayload{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
