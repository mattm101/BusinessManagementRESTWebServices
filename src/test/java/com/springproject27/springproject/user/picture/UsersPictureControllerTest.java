package com.springproject27.springproject.user.picture;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class UsersPictureControllerTest {

    @Autowired
    UsersPictureController usersPictureController;

    MockMultipartFile image = new MockMultipartFile("data", "filename.jpg", "image/jpg", "a".getBytes());

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void contextLoads() {
        assertThat(usersPictureController).isNotNull();
    }

    @Test
    public void getUsersPicture() throws Exception {

    }

    @Test
    public void addPicture() throws Exception {
    }

    @Test
    public void deletePicture() throws Exception {
    }

}