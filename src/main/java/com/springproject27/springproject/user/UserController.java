package com.springproject27.springproject.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/users", produces = "application/json")
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers(){
        return userService.findAll();
    }

    @GetMapping("/lastName/{pattern}")
    public List<User> getUsersByLastName(@PathVariable String pattern){
        return userService.findByLastName(pattern);
    }

    @GetMapping("/email/{pattern}")
    public List<User> getUsersByEmail(@PathVariable String pattern){
        return userService.findUsersByEmail(pattern);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user){
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id){
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id,@Valid @RequestBody User user){
        user.setId(id);
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id){
        userService.delete(id);
    }
}
