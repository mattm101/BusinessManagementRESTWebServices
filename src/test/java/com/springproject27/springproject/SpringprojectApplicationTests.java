package com.springproject27.springproject;

import com.springproject27.springproject.user.User;
import com.springproject27.springproject.user.UserController;
import com.springproject27.springproject.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Test
    public void contextLoads() {
        assertThat(userController).isNotNull();
    }

    @Test
    public void shouldReturnListOf2Users() throws Exception {
        userRepository.deleteAll();
        User user = User.builder()
                .firstName("test1")
                .lastName("test1")
                .email("test1@test")
                .phoneNumber("000000000")
                .build();
        userRepository.save(user);
        User user2 = User.builder()
                .firstName("test2")
                .lastName("test2")
                .email("test2@test")
                .phoneNumber("000000000")
                .build();
        userRepository.save(user2);
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email", is("test1@test")));
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        userRepository.deleteAll();
        User user = User.builder()
                .firstName("test1")
                .lastName("test1")
                .email("test1@test")
                .phoneNumber("000000000")
                .build();
        userRepository.save(user);
        String id = userRepository.findUserByEmail("test1@test").get().getId().toString();
        mockMvc.perform(delete("/api/users/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldCreateANewUser() throws Exception {
        userRepository.deleteAll();
        User user = User.builder()
                .firstName("test1")
                .lastName("test1")
                .email("test1@test")
                .phoneNumber("000000000")
                .build();
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", is("test1@test")));
    }

    @Test
    public void shouldUpdateUser() throws Exception{
        userRepository.deleteAll();
        User user = User.builder()
                .firstName("test1")
                .lastName("test1")
                .email("test1@test")
                .phoneNumber("000000000")
                .build();
        userRepository.save(user);

        User updatedUser = User.builder()
                .firstName("test1")
                .lastName("test1")
                .email("test2@test")
                .phoneNumber("000000000")
                .build();

        String id = userRepository.findUserByEmail("test1@test").get().getId().toString();

        mockMvc.perform(put("/api/users/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(updatedUser.getEmail())));
    }

    @Test
    public void shouldReturnConflictResponseStatusFailToUpdateUser() throws Exception{
        userRepository.deleteAll();
        User user1 = User.builder()
                .firstName("test1")
                .lastName("test1")
                .email("test1@test")
                .phoneNumber("000000000")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .firstName("test1")
                .lastName("test1")
                .email("test2@test")
                .phoneNumber("000000000")
                .build();
        userRepository.save(user2);

        User updatedUser1 = User.builder()
                .firstName("test1")
                .lastName("test1")
                .email(user2.getEmail())
                .phoneNumber("000000000")
                .build();

        String id = userRepository.findUserByEmail("test1@test").get().getId().toString();

        mockMvc.perform(put("/api/users/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(updatedUser1)))
                .andExpect(status().isConflict());
        mockMvc.perform(get("/api/users/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(user1.getEmail())));
    }

    @Test
    public void shouldReturnUserNotFoundResponse() throws Exception{
        userRepository.deleteAll();
        mockMvc.perform(get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        User user1 = User.builder()
                .firstName("test1")
                .lastName("test1")
                .email("test1@test")
                .phoneNumber("000000000")
                .build();

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(user1)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnInvalidPhoneNumberArgumentResponse() throws Exception{
        User user1 = User.builder()
                .firstName("test1")
                .lastName("test1")
                .email("test1@test")
                .phoneNumber("000")
                .build();

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(user1)))
                .andExpect(status().isBadRequest());
    }

}
