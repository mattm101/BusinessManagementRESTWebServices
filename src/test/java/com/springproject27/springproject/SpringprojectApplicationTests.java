package com.springproject27.springproject;

import com.springproject27.springproject.user.User;
import com.springproject27.springproject.user.UserController;
import com.springproject27.springproject.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpringprojectApplicationTests {

    @Autowired
    UserController userController;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MockMvc mockMvc;

    private User user1;
    private User user2;

    @Before
    public void loadContent() {
        userRepository.deleteAll();
        user1 = User.builder()
                .id(1L)
                .username("Andrzej20")
                .password("andrzejek210")
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
                .password("Michalek310")
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
    public void contextLoads() {
        assertThat(userController).isNotNull();
    }

    @Test
    public void shouldReturnListOf2Users() throws Exception {
        userRepository.save(user1);
        userRepository.save(user2);
        mockMvc.perform(get("/api/user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email", is("andrzejkowalski@wp.pl")));
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        userRepository.save(user1);
        String id = userRepository.findUserByEmail("andrzejkowalski@wp.pl").get().getId().toString();
        mockMvc.perform(delete("/api/user/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldCreateANewUser() throws Exception {
        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(user1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", is("andrzejkowalski@wp.pl")));
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        userRepository.save(user1);

        String id = userRepository.findUserByEmail("andrzejkowalski@wp.pl").get().getId().toString();

        mockMvc.perform(put("/api/user/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(user2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(user2.getEmail())));
    }

    @Test(expected = NestedServletException.class)
    public void shouldReturnConflictResponseStatusFailToUpdateUser() throws Exception {
        userRepository.save(user1);

        User user2 = User.builder()
                .firstName("test1")
                .lastName("test1")
                .email("test2@test")
                .phoneNumber("000000000")
                .build();
        userRepository.save(user2);

        User updatedUser1 = User.builder()
                .id(1L)
                .username("Andrzej20")
                .password("andrzejek210")
                .firstName("Andrzej")
                .lastName("Kowalski")
                .email(user2.getEmail())
                .phoneNumber("500300200")
                .vacationDaysForUseInYear(26)
                .availableVacationDays(26)
                .tokenExpired(true)
                .enabled(true)
                .build();

        String id = userRepository.findUserByEmail("andrzejkowalski@wp.pl").get().getId().toString();

        mockMvc.perform(put("/api/user/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(updatedUser1)))
                .andExpect(status().isConflict());
        mockMvc.perform(get("/api/user/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(user1.getEmail())));
    }

    @Test(expected = NestedServletException.class)
    public void shouldReturnUserNotFoundResponse() throws Exception {
        mockMvc.perform(get("/api/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mockMvc.perform(put("/api/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(user1)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnInvalidPhoneNumberArgumentResponse() throws Exception {
        user1.setPhoneNumber("00000");
        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(user1)))
                .andExpect(status().isBadRequest());
    }

}
