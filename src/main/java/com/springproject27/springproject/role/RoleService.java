package com.springproject27.springproject.role;

import com.springproject27.springproject.exception.EntityNotFoundException;
import com.springproject27.springproject.permission.Permission;
import com.springproject27.springproject.permission.PermissionRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void addRole(Role role) {
        roleRepository.save(role);
    }

    public Role getRole(Long id) {
        return roleRepository.getOne(id);
    }

    public Role getRole(String name) {
        return roleRepository.findOneByName(name).get();
    }

    public void deleteRole(Long id) throws EntityNotFoundException {
        Role currentRole = roleRepository.getOne(id);
        if (currentRole == null)
            throw new EntityNotFoundException("Role with id = " + id + " not found");
        roleRepository.delete(currentRole);
    }

    public void updateRole(Role role) throws EntityNotFoundException {
        Role currentRole = roleRepository.getOne(role.getId());
        if (currentRole == null)
            throw new EntityNotFoundException("Role with id = " + role.getId() + " not found");
        roleRepository.save(role);
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public void assignPermissionToRole(Role role, Permission permission) throws EntityNotFoundException {
        Optional<Permission> assignPermission = permissionRepository.findOneByName(permission.getName());
        Optional<Role> assignRole = roleRepository.findOneByName(role.getName());
        if (!assignPermission.isPresent())
            throw new EntityNotFoundException("Permission with id = " + permission.getId() + " not found");
        if (!assignRole.isPresent())
            throw new EntityNotFoundException("Role with id = " + role.getId() + " not found");
        assignRole.get().getPermissions().add(assignPermission.get());
        roleRepository.save(assignRole.get());
    }
}
