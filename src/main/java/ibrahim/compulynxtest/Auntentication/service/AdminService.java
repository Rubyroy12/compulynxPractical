package ibrahim.compulynxtest.Auntentication.service;


import ibrahim.compulynxtest.Auntentication.Enums.Role;
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
        Roles adminrole = new Roles(null, String.valueOf(Role.ADMIN), "System administration");
        Roles maker = new Roles(null, String.valueOf(Role.INITIATOR), "maker");
        Roles checker= new Roles(null, String.valueOf(Role.VERIFIER), "checker");
        List<Roles> roles = new ArrayList<>();
        roles.add(adminrole);
        roles.add(maker);
        roles.add(checker);
        roleRepo.saveAllAndFlush(roles);
//        entityManager.persist(roles);


    }

    //Default admin records
    @Transactional
    void addAdmin() {
        log.info("Adding admin user");
        List<User> users = new ArrayList<>();
        User user = new User();
        Set<Roles> roles = new HashSet<>();
        Set<Roles> makerrole = new HashSet<>();
        Set<Roles> checkerole = new HashSet<>();

        // Fetch a managed instance of Roles
        Roles adminrole = roleRepo.findByName(Role.ADMIN.name())
                .orElseThrow(() -> new RuntimeException("Error : Role Not found!"));

        // Optional: re-attach the role to the persistence context
//        adminrole = roleRepo.saveAndFlush(adminrole);
//        log.info("Is adminrole attached ? {}",entityManager.contains(adminrole));
//

        roles.add(adminrole);
        user.setRoles(roles);
        user.setFirstName("admin");
        user.setLastName("admin");
        user.setPhone("254725634469");
        user.setUsername("admin");
        user.setDateRegistered(new Date());
        user.setPassword(passwordEncoder.encode("admin"));


        //maker user
        User maker = new User();
        Roles initiator = roleRepo.findByName(Role.INITIATOR.name())
                .orElseThrow(() -> new RuntimeException("Error : Role Not found!"));

        makerrole.add(initiator);
        maker.setRoles(makerrole);
        maker.setFirstName("maker");
        maker.setLastName("maker");
        maker.setPhone("254725634469");
        maker.setUsername("maker");
        maker.setDateRegistered(new Date());
        maker.setPassword(passwordEncoder.encode("admin"));


        //checker user
        User checker = new User();
        Roles verifier = roleRepo.findByName(Role.VERIFIER.name())
                .orElseThrow(() -> new RuntimeException("Error : Role Not found!"));

        checkerole.add(verifier);
        roles.add(adminrole);
        checker.setRoles(checkerole);
        checker.setFirstName("checker");
        checker.setLastName("checker");
        checker.setPhone("254725634469");
        checker.setUsername("checker");
        checker.setDateRegistered(new Date());
        checker.setPassword(passwordEncoder.encode("admin"));


        users.add(user);
        users.add(maker);
        users.add(checker);




        try {
            userRepo.saveAllAndFlush(users);
//            entityManager.persist(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }


    public void run() {
        int countusers = userRepo.findAll().size();
        int roles = roleRepo.findAll().size();
        log.info("Users {}", countusers);
        if (roles < 3) {
            addAdminRole();
        }
        if (countusers < 3) {
            addAdmin();
        }


    }

}
