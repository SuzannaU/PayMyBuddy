package oc.paymybuddy.config;

import oc.paymybuddy.model.User;
import oc.paymybuddy.model.UserRole;
import oc.paymybuddy.repository.UserRepo;
import oc.paymybuddy.service.UserRoleService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepo userRepo;
    private UserRoleService userRoleService;

    public CustomUserDetailsService(UserRepo userRepo, UserRoleService userRoleService) {
        this.userRepo = userRepo;
        this.userRoleService = userRoleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> optUser = userRepo.findByUsername(username);
        if (optUser.isPresent()) {
            User user = optUser.get();
            System.out.println("user from db: " + user.getUsername());
            System.out.println("password from db: " + user.getPassword());
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    getGrantedAuthorities(user));
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(User user) {
        Set<String> roles = getRoles(user);
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return authorities;
    }

    private Set<String> getRoles(User user) {
        List<UserRole> userRoles = userRoleService.getAllUserRolesByUser(user);
        Set<String> roleNames = userRoles.stream()
                .map(ur -> ur.getRole())
                .map(r -> r.getRoleName())
                .collect(Collectors.toSet());
        return roleNames;
    }
}
