package com.springproject27.springproject.user;

import com.springproject27.springproject.exception.EntityAlreadyExistsException;
import com.springproject27.springproject.exception.EntityNotFoundException;
import com.springproject27.springproject.vacation.Vacation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    @Mock
    private User user1;

    @Mock
    private User user2;

    private Vacation vacation;

    @InjectMocks
    private UserService userService;

    @Before
    public void setUp() {
        userRepository.deleteAll();
        vacation = Vacation.builder()
                .id(1L)
                .beginDateOfVacation(LocalDate.of(2018, 9, 7))
                .endDateOfVacation(LocalDate.of(2018, 9, 13))
                .reasonOfVacation("Wakacje")
                .typeOfVacation("Urlop")
                .build();
        user1 = User.builder()
                .id(1L)
                .username("Andrzej20")
                .firstName("Andrzej")
                .lastName("Kowalski")
                .email("andrzejkowalski@wp.pl")
                .phoneNumber("500300200")
                .vacationDaysForUseInYear(26)
                .availableVacationDays(26)
                .tokenExpired(true)
                .enabled(true)
                .build();
        user2 = User.builder()
                .id(2L)
                .username("Michalek31")
                .firstName("Michał")
                .lastName("Kowalski")
                .email("michalkowalski@wp.pl")
                .phoneNumber("500300200")
                .vacationDaysForUseInYear(26)
                .availableVacationDays(26)
                .tokenExpired(true)
                .enabled(true)
                .build();
    }

    @Test
    public void shouldReturnEmptyList() {
        when(userRepository.findAll())
                .thenReturn(new ArrayList<>());
        Assert.assertTrue(userService.findAll().isEmpty());
    }

    @Test
    public void shouldReturnOne() {
        when(userRepository.findUserById(anyLong()))
                .thenReturn(Optional.of(user1));
        Assert.assertEquals(user1, userService.getUser(1L));
    }

    @Test
    public void shouldReturnSingleUserByEmail() {
        when(userRepository.findUserByEmailContainingIgnoreCase(anyString()))
                .thenReturn(Optional.of(user1));
        Assert.assertSame(user1, userService.findUserByEmail("andrzejkowalski@wp.pl"));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testFindUserByEmail() {
        userService.findUserByEmail("andrzejkowalski@wp.pl");
    }

    @Test
    public void testFindUsersByLastName() {
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        when(userRepository.findUsersByLastNameContainingIgnoreCase(anyString()))
                .thenReturn(users);
        Assert.assertSame(userService.findByLastName("Kowalski"), users);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testFailGetUser() {
        userService.getUser(1L);
    }

    @Test
    public void testTrueAddUser() {
        when(userRepository.save(user1))
                .thenReturn(user1);
        userService.createUser(user1);
        verify(userRepository, times(1)).save(user1);
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testFalseAddUser() {
        when(userRepository.save(user1))
                .thenReturn(user1);
        when(userRepository.findUserByEmail(anyString()))
                .thenReturn(Optional.of(user1));
        userService.createUser(user2);
    }

    @Test
    public void testFindUserByUserName() {
        when(userRepository.findUserByUsername(anyString()))
                .thenReturn(Optional.of(user1));
        Assert.assertSame(user1, userService.getUserByUsername("Andrzej20"));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateUserWrongId() {
        userService.updateUser(anyLong(), user2);
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testUpdateUserWithSameEmail() {
        when(userRepository.findUserById(anyLong()))
                .thenReturn(Optional.of(user1));
        when(userRepository.findUserByEmailAndIdNot(anyString(), anyLong()))
                .thenReturn(Optional.of(user1));
        user1.setEmail("michalkowalski@wp.pl");
        userService.updateUser(1L, user1);
    }


    @Test
    public void testUpdateUser() {
        when(userRepository.findUserById(anyLong()))
                .thenReturn(Optional.of(user1));
        user1.setEmail("michalkowalski@wp.pl");
        userService.updateUser(1L, user1);
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    public void testAddVacationToUser() {
        user.getVacations().add(vacation);
        when(user.addVacation(vacation))
                .thenReturn(user);
        userService.addVacationToUser(user, vacation);
        verify(user, times(1)).addVacation(vacation);
    }

    @Test
    public void testDeleteUser() {
        when(userRepository.save(user1))
                .thenReturn(user1);
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        userService.delete(1L);
        Assert.assertFalse(userRepository.findUserById(1L).isPresent());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testFailDeleteUser() {
        userService.delete(1);
    }
}
