package oc.paymybuddy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @Column(name = "role_name")
    private String roleName;

    @OneToMany(
            mappedBy = "role",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JsonManagedReference("role")
    private List<UserRole> users;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<UserRole> getUsers() {
        return users;
    }

    public void setUsers(List<UserRole> users) {
        this.users = users;
    }
}
