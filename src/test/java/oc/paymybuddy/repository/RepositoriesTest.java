package oc.paymybuddy.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import oc.paymybuddy.model.Relation;
import oc.paymybuddy.model.Role;
import oc.paymybuddy.model.Transaction;
import oc.paymybuddy.model.User;
import oc.paymybuddy.model.UserRole;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/paymybuddy_test?serverTimezone=UTC",
        "spring.datasource.driverClassname=com.mysql.cj.jdbc.Driver",
        "spring.datasource.username=root",
        "spring.datasource.password=root"
})


public class RepositoriesTest {

    @Autowired
    private RelationRepo relationRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserRoleRepo userRoleRepo;

    private User user1;

    @Test
    public void findAllByInvitingUserOrInvitedUser_returnsRelations() {

        user1 = userRepo.findByUsername("user1").get();
        List<Relation> result = relationRepo.findAllByInvitingUserOrInvitedUser(user1, user1);

        assertEquals(2, result.size());
    }

    @Test
    public void findByRoleName_returnsRole() {

        Role result = roleRepo.findByRoleName("USER");

        assertEquals("USER", result.getRoleName());
    }

    @Test
    public void findAllBySender_returnsTransactions() {

        user1 = userRepo.findByUsername("user1").get();
        List<Transaction> result = transactionRepo.findAllBySender(user1, null);

        assertEquals(2, result.size());
    }

    @Test
    public void findByUsername_returnsUser() {
        User result = userRepo.findByUsername("user2").get();

        assertEquals("user2@example.com", result.getEmail());
    }

    @Test
    public void findByEmail_returnsUser() {
        User result = userRepo.findByEmail("user2@example.com").get();

        assertEquals("user2", result.getUsername());
    }

    @Test
    public void findAllByUser_returnsUserRoles() {

        user1 = userRepo.findByUsername("user1").get();
        List<UserRole> result = userRoleRepo.findAllByUser(user1);

        assertEquals(2, result.size());
    }

}
