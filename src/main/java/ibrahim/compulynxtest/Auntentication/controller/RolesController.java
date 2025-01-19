package ibrahim.compulynxtest.Auntentication.controller;


import ibrahim.compulynxtest.Auntentication.entities.Roles;
import ibrahim.compulynxtest.Auntentication.service.RoleService;
import ibrahim.compulynxtest.Utils.ApiResponse;
import ibrahim.compulynxtest.Utils.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/vi/roles")
public class RolesController {

    @Autowired
    private RoleService roleService;

    @PostMapping("add")
    public ResponseEntity<ApiResponse<Roles>> addRole(@RequestBody Roles role) {
        Roles r = roleService.addRole(role);
        ApiResponse res = ResponseBuilder.success("Role Added Succesfully.",r);
        return ResponseEntity.ok(res);
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<Roles>> getRoles() {
        List<Roles> roles = roleService.getRoles();
       ApiResponse res = ResponseBuilder.success("Role Added Succesfully.",roles);
        return ResponseEntity.ok(res);
    }
}
