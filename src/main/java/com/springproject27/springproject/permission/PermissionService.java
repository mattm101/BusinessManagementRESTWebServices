package com.springproject27.springproject.permission;

import com.springproject27.springproject.exception.EntityNotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Getter
@Setter
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    public void addPermission(Permission permission) {
        permissionRepository.save(permission);
    }

    public Permission getPermission(Long id) {
        return permissionRepository.getOne(id);
    }

    public Permission getPermission(String name) {
        return permissionRepository.findOneByName(name).get();
    }

    public void deletePermission(Long id) throws EntityNotFoundException {
        Permission currentPermission = permissionRepository.getOne(id);
        if (currentPermission == null)
            throw new EntityNotFoundException("Permission with id = " + id + " not found");
        permissionRepository.delete(currentPermission);
    }

    public void updatePermission(Permission permission) throws EntityNotFoundException {
        Permission currentPermission = permissionRepository.getOne(permission.getId());
        if (currentPermission == null)
            throw new EntityNotFoundException("Permission with id = " + permission.getId() + " not found");
        permissionRepository.save(permission);
    }

    public List<Permission> getPermissions() {
        return permissionRepository.findAll();
    }
}
