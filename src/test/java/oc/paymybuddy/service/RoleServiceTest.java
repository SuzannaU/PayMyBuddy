package oc.paymybuddy.service;

import oc.paymybuddy.model.Role;
import oc.paymybuddy.repository.RoleRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RoleServiceTest {

    @MockitoBean
    private RoleRepo roleRepo;

    @Autowired
    private RoleService roleService;

    @Test
    public void getRoleByRoleName_withCorrectParameters_callsRepoAndReturnsRole(){
        when(roleRepo.findByRoleName(anyString())).thenReturn(new Role());

        Role result = roleService.getRoleByRoleName("USER");

        assertNotNull(result);
        verify(roleRepo).findByRoleName(anyString());
    }
}
