package oc.paymybuddy.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserRoleId implements Serializable {

    private int userId;
    private String roleName;

    public UserRoleId(int userId, String roleName) {
        this.userId = userId;
        this.roleName = roleName;
    }

    public UserRoleId() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        UserRoleId that = (UserRoleId) o;
        return userId == that.userId && Objects.equals(roleName, that.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roleName);
    }
}
