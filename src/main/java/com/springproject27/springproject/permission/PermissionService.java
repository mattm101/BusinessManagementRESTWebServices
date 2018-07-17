package com.springproject27.springproject.permission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;

@Service
@Transactional
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    public PermissionRepository getPermissionRepository(){
        return permissionRepository;
    }

    public void setPermissionRepository(PermissionRepository permissionRepository){
        this.permissionRepository = permissionRepository;
    }

    public void addPermission(Permission permission){
        permissionRepository.save(permission);
    }

    public Permission getPermission(Long id){
        return permissionRepository.getOne(id);
    }

    public Permission getPermission(String name){
        return permissionRepository.findOneByName(name).get();
    }

    public void deletePermission(Long id){
        Permission currentPermission = permissionRepository.getOne(id);
        permissionRepository.delete(currentPermission);
    }

    public void updatePermission(Permission permission){
        Permission updatePermission = permissionRepository.getOne(permission.getId());
        updatePermission.setName(permission.getName());
        permissionRepository.save(permission);
    }

    public List<Permission> getPermissions(){
        return permissionRepository.findAll();
    }
}
