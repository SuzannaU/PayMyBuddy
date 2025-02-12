package oc.paymybuddy.repository;

import oc.paymybuddy.model.UserRole;
import oc.paymybuddy.model.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepo extends JpaRepository<UserRole, UserRoleId> {
}
