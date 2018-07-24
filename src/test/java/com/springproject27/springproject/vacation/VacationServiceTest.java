package com.springproject27.springproject.vacation;

import com.springproject27.springproject.exception.EntityNotFoundException;
import com.springproject27.springproject.user.User;
import com.springproject27.springproject.user.UserRepository;
import com.springproject27.springproject.user.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
public class VacationServiceTest {

    @Mock
    private VacationRepository vacationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private VacationService vacationService;

    private User user;

    private Vacation vacation1;

    @Before
    public void setUp() {
        user = User.builder()
                .id(10L)
                .firstName("first_name_admin")
                .lastName("last_name_admin")
                .username("username_admin")
                .password("$2a$04$WWIt4w1IO4u9CZEALuz/DeBNerXm8TPDOSb9qM1PmeMSn3nskehoW")
                .email("adminek@gmail.com")
                .phoneNumber("500500200")
                .vacationDaysForUseInYear(26)
                .availableVacationDays(26)
                .tokenExpired(true)
                .enabled(true)
                .build();
        vacation1 = Vacation.builder()
                .id(1L)
                .beginDateOfVacation(LocalDate.of(2018, 9, 7))
                .endDateOfVacation(LocalDate.of(2018, 9, 13))
                .reasonOfVacation("Wakacje")
                .typeOfVacation("Urlop")
                .build();
        vacationRepository.deleteAll();
    }

    @Test
    public void shouldFindOneByUserId() {
        Vacation vacation = Vacation.builder()
                .id(1L)
                .beginDateOfVacation(LocalDate.of(2018, 9, 7))
                .endDateOfVacation(LocalDate.of(2018, 9, 13))
                .reasonOfVacation("Wakacje")
                .typeOfVacation("Urlop")
                .user(user)
                .build();
        vacationRepository.save(vacation);
        when(vacationRepository.findByUserId(anyLong()))
                .thenReturn(Collections.singletonList(vacation));
        Assert.assertTrue(vacationService.findAllByUserId(10L).contains(vacation));
    }

    @Test
    public void shouldFindTwoByUserId() {
        Vacation vacation1 = Vacation.builder()
                .id(1L)
                .beginDateOfVacation(LocalDate.of(2018, 9, 7))
                .endDateOfVacation(LocalDate.of(2018, 9, 13))
                .reasonOfVacation("Wakacje")
                .typeOfVacation("Urlop płatny")
                .user(user)
                .build();
        Vacation vacation2 = Vacation.builder()
                .id(2L)
                .beginDateOfVacation(LocalDate.of(2018, 9, 7))
                .endDateOfVacation(LocalDate.of(2018, 9, 13))
                .reasonOfVacation("Choroba")
                .typeOfVacation("Urlop bezpłatny")
                .user(user)
                .build();
        List<Vacation> vacations = new ArrayList<>();
        vacations.add(vacation1);
        vacations.add(vacation2);
        when(vacationRepository.findByUserId(anyLong()))
                .thenReturn(vacations);
        Assert.assertSame(vacations, vacationRepository.findByUserId(10L));
    }

    @Test
    public void testSuccessGetVacation() {
        when(vacationRepository.findById(anyLong()))
                .thenReturn(Optional.of(vacation1));
        Assert.assertSame(vacation1, vacationService.getVacation(1L));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testFailGetVacation() {
        vacationService.getVacation(1L);
    }

    @Test
    public void shouldReturnEmptyList() {
        when(vacationRepository.findByUserId(1L))
                .thenReturn(new ArrayList<>());
        Assert.assertTrue(vacationRepository.findByUserId(1L).isEmpty());
    }

    @Test
    public void testCreateVacationWithUser() {
        when(userRepository.findUserById(anyLong()))
                .thenReturn(Optional.of(user));
        user.getVacations().add(vacation1);
        when(userService.addVacationToUser(user, vacation1))
                .thenReturn(user);
        vacationService.createVacation(vacation1, user.getId());
        Assert.assertTrue(vacation1.getUser().getId().equals(user.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testCreateVacationWithNoUser() {
        vacationService.createVacation(vacation1, 13L);
    }

    @Test
    public void shouldGetVacationWithUser() {
        when(userRepository.findUserById(anyLong()))
                .thenReturn(Optional.of(user));
        user.getVacations().add(vacation1);
        when(userService.addVacationToUser(user, vacation1))
                .thenReturn(user);
        vacationService.createVacation(vacation1, user.getId());
        Assert.assertSame(user, vacation1.getUser());
    }

    @Test
    public void testUpdateVacationWithId() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(vacationRepository.save(vacation1))
                .thenReturn(vacation1);
        when(vacationRepository.existsById(anyLong()))
                .thenReturn(true);
        vacation1.setTypeOfVacation("Brak typu");
        Assert.assertSame(vacation1, vacationService.updateVacation(10L, 1L, vacation1));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testFailUpdateVacation() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(vacationRepository.save(vacation1))
                .thenReturn(vacation1);
        vacationService.updateVacation(10L, 1L, vacation1);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testFailUpdateVacationNoUser() {
        when(vacationRepository.save(vacation1))
                .thenReturn(vacation1);
        vacationService.updateVacation(13L, 1L, vacation1);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteVacationFailUser() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(false);
        vacationService.deleteVacation(10L, 1L);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteVacationFailVacation() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(vacationRepository.existsById(anyLong()))
                .thenReturn(false);
        vacationService.deleteVacation(10L, 1L);
    }

    @Test
    public void testDeleteVacationTrue() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(vacationRepository.existsById(anyLong()))
                .thenReturn(true);
        vacationService.deleteVacation(10L, 1L);
    }
}
