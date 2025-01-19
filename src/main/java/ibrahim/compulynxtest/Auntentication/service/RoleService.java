package ibrahim.compulynxtest.Auntentication.service;

import ibrahim.compulynxtest.Auntentication.entities.Roles;
import ibrahim.compulynxtest.Auntentication.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {



    @Autowired
    private RoleRepo roleRepo;



    public Roles addRole(Roles role){
        Roles r = roleRepo.save(role);
        return r;

    }
    public List<Roles> getRoles(){
        return roleRepo.findAll();

    }
}
