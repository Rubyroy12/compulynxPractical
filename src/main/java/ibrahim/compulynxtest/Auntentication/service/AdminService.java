package ibrahim.compulynxtest.Auntentication.service;


import ibrahim.compulynxtest.Auntentication.entities.Role;
import ibrahim.compulynxtest.Auntentication.entities.Roles;
import ibrahim.compulynxtest.Auntentication.entities.User;
import ibrahim.compulynxtest.Auntentication.repository.RoleRepo;
import ibrahim.compulynxtest.Auntentication.repository.UserRepository;
import jakarta.annotation.PostConstruct;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@Slf4j
public class AdminService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private EntityManager entityManager;

    @PostConstruct
    public void UserCheck() {
        log.info("Users check ....");
        System.out.println((passwordEncoder.encode("admin")));

//        if (passwordEncoder.matches("admin","$2a$10$CQaGCl7cT0DCKwy8i2XaN.8X1jM09kr6aQgh2DfDV/VQT1SYP3nL6")){
//            System.out.println("Password mateches!");
//        }else {
//            System.out.println("Incorrect Password");
//        }

        run();
    }


    @Transactional
    void addAdminRole() {
        log.info("Adding role");
        Roles adminrole = new Roles(null, String.valueOf(Role.USER), "System administration");
        List<Roles> roles = new ArrayList<>();
        roles.add(adminrole);
        roleRepo.saveAllAndFlush(roles);
//        entityManager.persist(roles);


    }

    //Default admin records
    @Transactional
    void addAdmin() {
        log.info("Adding admin user");
        User user = new User();
        Set<Roles> roles = new HashSet<>();

        // Fetch a managed instance of Roles
        Roles adminrole = roleRepo.findByName(Role.USER.name())
                .orElseThrow(() -> new RuntimeException("Error : Role Not found!"));

        // Optional: re-attach the role to the persistence context
//        adminrole = roleRepo.saveAndFlush(adminrole);
        log.info("Is adminrole attached ? {}",entityManager.contains(adminrole));


        roles.add(adminrole);
        user.setRoles(roles);
        user.setFirstName("admin");
        user.setLastName("admin");
        user.setPhone("254725634469");
        user.setUsername("admin");
        user.setDateRegistered(new Date());
        user.setPassword(passwordEncoder.encode("admin"));

        try {
            userRepo.save(user);
//            entityManager.persist(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }


    public void run() {
        int countusers = userRepo.findAll().size();
        int roles = roleRepo.findAll().size();
        log.info("Users {}", countusers);
        if (roles < 1) {
            addAdminRole();
        }
        if (countusers < 1) {
            addAdmin();
        }


    }

}
