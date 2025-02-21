package oc.paymybuddy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
@Table(name = "user_role")
public class UserRole {

    @EmbeddedId
    private UserRoleId userRoleId;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @MapsId("userId")
    @JoinColumn(name="user_id")
    @JsonBackReference("userRoles")
    private User user;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @MapsId("roleName")
    @JoinColumn(name="role_name", referencedColumnName = "role_name")
    @JsonBackReference("role")
    private Role role;

    public UserRole(UserRoleId userRoleId) {
        this.userRoleId = userRoleId;
    }

    public UserRole() {

    }

    public void setUserRoleId(UserRoleId userRoleId) {
        this.userRoleId = userRoleId;
    }

    public UserRoleId getUserRoleId() {
        return userRoleId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
