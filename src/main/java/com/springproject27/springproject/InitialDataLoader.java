package com.springproject27.springproject;

import com.springproject27.springproject.user.User;
import com.springproject27.springproject.user.UserService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


@Component
public class InitialDataLoader implements ApplicationListener<ApplicationReadyEvent> {

    private final UserService userService;


    public InitialDataLoader(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event){
        addUser("Jan", "Kowalski", "jan@kowalski.com", "222331344");
        addUser("Paweł", "Nowak", "pawnow@gmail.com", "545777456");
    }

    private void addUser(String imie, String nazwisko, String email, String telefon) {
        User user = User.builder()
                .firstName(imie)
                .lastName(nazwisko)
                .email(email)
                .phoneNumber(telefon)
                .build();
        userService.saveOrUpdate(user);
    }
}
