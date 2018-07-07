package com.springproject27.springproject.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User saveOrUpdate(@NotNull final User user){
        return userRepository.save(user);
    }

    List<User> findAll(){
        return userRepository.findAll();
    }

    List<User> findByLastName(String lastName){
        return userRepository.findUsersByLastNameContainingIgnoreCase(lastName);
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

    User getUser(@NotNull Long id){
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
