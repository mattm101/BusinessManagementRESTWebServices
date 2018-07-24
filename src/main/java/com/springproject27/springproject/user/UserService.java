package com.springproject27.springproject.user;

import com.springproject27.springproject.exception.EntityAlreadyExistsException;
import com.springproject27.springproject.exception.EntityNotFoundException;
import com.springproject27.springproject.role.Role;
import com.springproject27.springproject.role.RoleRepository;
import com.springproject27.springproject.vacation.Vacation;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username).get();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findByLastName(final String lastName) {
        return userRepository.findUsersByLastNameContainingIgnoreCase(lastName);
    }

    public User findUserByEmail(final String email) {
        return userRepository.findUserByEmailContainingIgnoreCase(email)
                .orElseThrow(
                        () -> new EntityNotFoundException("User with email = " + email + " not found!")
                );
    }

    @Transactional
    public User createUser(@NotNull User user) {
        Optional<User> existingUser = userRepository.findUserByEmail(user.getEmail());
        if (existingUser.isPresent())
            throw new EntityAlreadyExistsException("User with email = " + user.getEmail() + " already exists!");
        return userRepository.save(user);
    }

    public User assignRoleToUser(Role role, User user) throws EntityNotFoundException {
        Optional<Role> assignRole = roleRepository.findOneByName(role.getName());
        Optional<User> assignUser = userRepository.findUserByUsername(user.getUsername());
        if (!assignRole.isPresent())
            throw new EntityNotFoundException("Role with id = " + role.getId() + " not found");
        if (!assignUser.isPresent())
            throw new EntityNotFoundException("User with id = " + user.getId() + " not found!");
        assignUser.get().getRoles().add(assignRole.get());
        return userRepository.save(assignUser.get());
    }

    public UserDetails loadUserByUsername(String username) throws EntityNotFoundException {
        Optional<User> account = userRepository.findUserByUsername(username);
        if (!account.isPresent())
            throw new EntityNotFoundException("User with username = " + username + " not found!");
        account.get().setLastAccessedDate(Calendar.getInstance().getTime());
        userRepository.save(account.get());
        return account.get();
    }

    public User getUser(@NotNull long id) throws EntityNotFoundException {
        return userRepository.findUserById(id).orElseThrow(() -> new EntityNotFoundException(("User doesn't exist!")));
    }

    @Transactional
    public User updateUser(Long id, @NotNull User user) throws EntityNotFoundException, EntityAlreadyExistsException {
        Optional<User> dbUser = userRepository.findUserById(id);
        if (!dbUser.isPresent()) {
            throw new EntityNotFoundException("User with id = " + id + " not found!");
        }

        String email = user.getEmail();
        if (userRepository.findUserByEmailAndIdNot(email, id).isPresent()) {
            throw new EntityAlreadyExistsException("User with email = " + email + " already exists!");
        }
        user.setId(id);
        return userRepository.save(user);
    }

    @Transactional
    public void delete(@NotNull long id) throws EntityNotFoundException {
        if (!userRepository.existsById(id))
            throw new EntityNotFoundException("User with id = " + id + " not found!");
        userRepository.deleteById(id);
    }

    public User addVacationToUser(@NotNull User user, @NotNull Vacation vacation) {
        return user.addVacation(vacation);
    }
}
