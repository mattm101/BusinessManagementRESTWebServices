package com.springproject27.springproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class InitialDataLoader implements ApplicationListener<ApplicationReadyEvent> {

    private PasswordEncoder passwordEncoder;

    @Autowired
    public InitialDataLoader(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println(passwordEncoder.encode("password"));
    }

}
