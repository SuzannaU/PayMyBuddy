package oc.paymybuddy.service;

import oc.paymybuddy.model.Role;
import oc.paymybuddy.model.User;
import oc.paymybuddy.model.UserRole;
import oc.paymybuddy.repository.UserRoleRepo;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserRoleServiceTest {

    @MockitoBean
    private UserRoleRepo userRoleRepo;

    @Autowired
    private UserRoleService userRoleService;

    @Test
    public void getAllUserRolesByUser_withCorrectParameters_callsRepoAndReturnsUserRoles() {
        when(userRoleRepo.findAllByUser(any(User.class))).thenReturn(new ArrayList<>());

        List<UserRole> result =  userRoleService.getAllUserRolesByUser(new User());

        assertNotNull(result);
        verify(userRoleRepo).findAllByUser(any(User.class));
    }

    @Test
    public void assignRoleToUser_withCorrectParameters_setsRoleAndCallsRepo() throws Exception {
        when(userRoleRepo.save(any())).thenReturn(new UserRole());

        User user = new User();
        Field idField = User.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, 1);
        Role role = new Role();
        role.setRoleName("TEST");
        ArgumentCaptor<UserRole> userRoleCaptor = ArgumentCaptor.forClass(UserRole.class);

        userRoleService.assignRoleToUser(user, role);

        verify(userRoleRepo).save(userRoleCaptor.capture());
        UserRole capturedUserRole = userRoleCaptor.getValue();
        assertNotNull(capturedUserRole);
        assertEquals(user, capturedUserRole.getUser());
        assertEquals(role, capturedUserRole.getRole());
        assertEquals(1, capturedUserRole.getUser().getId());
        assertEquals("TEST", capturedUserRole.getRole().getRoleName());
    }
}
