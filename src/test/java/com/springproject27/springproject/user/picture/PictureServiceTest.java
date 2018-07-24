package com.springproject27.springproject.user.picture;

import com.springproject27.springproject.config.configservice.PictureConfigService;
import com.springproject27.springproject.user.User;
import com.springproject27.springproject.user.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PictureServiceTest {

    @Autowired
    private PictureConfigService pictureConfigService;
    @Mock
    private UserService userService;
    @Mock
    private PictureRepository pictureRepository;
    @Autowired
    private ResourceLoader resourceLoader;

    private PictureService pictureService;

    private User user;

    private Picture picture;

    private Picture savedPicture;

    private MockMultipartFile file;

    public PictureServiceTest() {
    }

    @Before
    public void setUp() throws IOException {
        pictureService = new PictureService(pictureConfigService,resourceLoader, pictureRepository, userService);

        user = User.builder()
                .id(1L)
                .username("user")
                .email("user@user")
                .firstName("usersFirstName")
                .lastName("usersLastName")
                .build();
        Mockito.when(userService.getUser(ArgumentMatchers.anyLong())).thenReturn(user);

        picture = Picture.builder()
                .id(1L)
                .filename(user.getId() + ".png")
                .user(user)
                .build();

        Mockito.when(pictureRepository.findByUser(ArgumentMatchers.any(User.class))).thenReturn(picture);

        byte[] bytes = Files.readAllBytes(Paths.get(pictureConfigService.getUsersPictureRoot(), "test.jpg"));

        file = new MockMultipartFile("data", "filename.png", "image/jpg", bytes);

    }

    @Test
    public void shouldCreateUsersPicture() throws IOException {
        savedPicture = Picture.builder()
                .id(1L)
                .filename(user.getId() + ".png")
                .user(user)
                .build();
        Mockito.when(pictureRepository.save(ArgumentMatchers.any(Picture.class))).thenReturn(savedPicture);

        Picture createdPicture = pictureService.createUsersPicture(file, user.getId());
        Assert.assertSame(savedPicture, createdPicture);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePictureWithIllegalFileFormat() throws IOException {
        file = new MockMultipartFile("data", "filename.png", "image/png", "illegal data".getBytes());

        pictureService.createUsersPicture(file, user.getId());
    }

    @Test
    public void shouldDeleteUsersPicture() throws IOException {
        Files.deleteIfExists(Paths.get(pictureConfigService.getUsersPictureRoot(), picture.getFilename()));
        Files.copy(file.getInputStream(), Paths.get(pictureConfigService.getUsersPictureRoot(), picture.getFilename()));

        pictureService.deleteUsersPicture(user.getId());
        Assert.assertFalse(Files.exists(Paths.get(pictureConfigService.getUsersPictureRoot(), picture.getFilename())));

    }

    @Test
    public void shouldGetDefaultPicture() throws IOException {
        Mockito.when(pictureRepository.findByUser(ArgumentMatchers.any(User.class))).thenReturn(null);

        Resource usersPicture = pictureService.getUsersPicture(user.getId());
        Assert.assertTrue(usersPicture.getFilename().equals("default.png"));
    }

    @Test
    public void shouldGetUsersPicture() throws IOException {
        Resource usersPicture = pictureService.getUsersPicture(user.getId());
        Assert.assertTrue(usersPicture.getFilename().equals(picture.getFilename()));
    }

    @org.junit.After
    public void deleteImage() throws IOException {
        Files.deleteIfExists(Paths.get(pictureConfigService.getUsersPictureRoot(), user.getId() + ".png"));
    }


}