package com.springproject27.springproject.role;

import com.springproject27.springproject.permission.Permission;
import com.springproject27.springproject.permission.PermissionRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Getter
@Setter
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    public void addRole(Role role){
        roleRepository.save(role);
    }

    public Role getRole(Long id){
        return roleRepository.getOne(id);
    }

    public Role getRole(String name){
        return roleRepository.findOneByName(name).get();
    }

    public void deleteRole(Long id){
        Role currentRole = roleRepository.getOne(id);
        roleRepository.delete(currentRole);
    }

    public void updateRole(Role role){
        Role updateRole = roleRepository.getOne(role.getId());
        updateRole.setName(role.getName());
        roleRepository.save(role);
    }

    public List<Role> getRoles(){
        return roleRepository.findAll();
    }

    public void assignPermissionToRole(Role role, Permission permission){
        Permission assignPermission = permissionRepository.findOneByName(permission.getName()).get();
        Role assignRole = roleRepository.findOneByName(role.getName()).get();
        assignRole.getPermissions().add(assignPermission);
        roleRepository.save(assignRole);
    }
}
