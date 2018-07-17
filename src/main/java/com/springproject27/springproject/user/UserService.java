package com.springproject27.springproject.user;

import com.springproject27.springproject.role.Role;
import com.springproject27.springproject.role.RoleRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Getter
@Setter
public class UserService implements UserDetailsService{
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    public User getUser(String username) {
        return userRepository.findByUsername(username).get();
    }

    List<User> findAll(){
        return userRepository.findAll();
    }

    List<User> findByLastName(String lastName){
        return userRepository.findUsersByLastNameContainingIgnoreCase(lastName);
    }

    public void assignRoleToUser(Role role, User user) {
        Role assignRole = roleRepository.findOneByName(role.getName()).get();
        User assignUser = userRepository.findByUsername(user.getUsername()).get();
        assignUser.getRoles().add(assignRole);
        userRepository.save(assignUser);
    }

    public User loadUserByUsername(String username) {
        User acc = getUser(username);
        acc.setLastAccessedDate(Calendar.getInstance().getTime());
        updateUser(acc);
        return acc;
    }


    List<User> findUsersByEmail(String email){
        return userRepository.findUsersByEmailContainingIgnoreCase(email);
    }

    @Transactional
    public User createUser(@NotNull User user){
        Optional<User> existingUser = userRepository.findUserByEmail(user.getEmail());
        if(existingUser.isPresent()){
            String email = user.getEmail();
            throw new EmailAlreadyExistsException(email);
        }
        return userRepository.save(user);
    }

    public User getUser(@NotNull Long id){
       return userRepository.findUserById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    public User save(@NotNull final User user){
        return userRepository.save(user);
    }

    @Transactional
    User updateUser(@NotNull User user){
        Long id = user.getId();
        Optional<User> dbUser = userRepository.findUserById(id);
        if(!dbUser.isPresent()) {
            throw new UserNotFoundException(id);
        }

        String email = user.getEmail();
        if(userRepository.findUserByEmailAndIdNot(email, id).isPresent()){
            throw new EmailAlreadyExistsException(email);
        }
        return userRepository.save(user);
    }

    @Transactional
    public void delete(@NotNull Long id){
        userRepository.deleteById(id);
    }

}
