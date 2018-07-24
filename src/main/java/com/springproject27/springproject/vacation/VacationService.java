package com.springproject27.springproject.vacation;


import com.springproject27.springproject.exception.EntityNotFoundException;
import com.springproject27.springproject.user.User;
import com.springproject27.springproject.user.UserRepository;
import com.springproject27.springproject.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Transactional
@Service
public class VacationService {

    private final VacationRepository vacationRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    public List<Vacation> findAllByUserId(Long userId) {
        return vacationRepository.findByUserId(userId);
    }

    public Vacation getVacation(Long id) throws EntityNotFoundException {
        return vacationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Vacation with id = " + id + " not found!"));
    }

    @Transactional
    public void createVacation(@NotNull Vacation vacation, Long userId) throws EntityNotFoundException {
        Optional<User> user = userRepository.findUserById(userId);
        if (!user.isPresent())
            throw new EntityNotFoundException("User with id = " + userId + " not found!");
        vacation.setUser(userService.addVacationToUser(user.get(), vacation));
        vacationRepository.save(vacation);
    }

    @Transactional
    public Vacation updateVacation(Long userId, Long id, @NotNull Vacation vacationRequest) throws EntityNotFoundException {
        if (!userRepository.existsById(userId))
            throw new EntityNotFoundException("User with id = " + userId + " not found!");
        if (!vacationRepository.existsById(id))
            throw new EntityNotFoundException("Vacation with id = " + id + " not found!");
        vacationRequest.setId(id);
        return vacationRepository.save(vacationRequest);
    }

    @Transactional
    public void deleteVacation(@NotNull Long userId, @NotNull Long id) throws EntityNotFoundException {
        if (!userRepository.existsById(userId))
            throw new EntityNotFoundException("User with id = " + userId + " not found!");
        if (!vacationRepository.existsById(id))
            throw new EntityNotFoundException("Vacation with id = " + id + " not found!");
        vacationRepository.deleteById(id);
    }
}
