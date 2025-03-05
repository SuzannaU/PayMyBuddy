package oc.paymybuddy;

import oc.paymybuddy.model.UserRole;
import oc.paymybuddy.repository.UserRepo;
import oc.paymybuddy.service.UserRoleService;
import oc.paymybuddy.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootApplication
public class PayMyBuddyApplication {


    public static void main(String[] args) {
        SpringApplication.run(PayMyBuddyApplication.class, args);

//        @Bean
//        public LayoutDialect layoutDialect() {
//            return new LayoutDialect();
//        }
        }




}
