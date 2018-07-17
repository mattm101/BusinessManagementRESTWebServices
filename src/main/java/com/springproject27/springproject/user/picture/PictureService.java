package com.springproject27.springproject.user.picture;

import com.springproject27.springproject.user.User;
import com.springproject27.springproject.user.UserService;
import com.springproject27.springproject.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class PictureService {

    private static final String PICTURES_ROOT = "pictures";

    private ResourceLoader resourceLoader;
    private PictureRepository pictureRepository;
    private UserService userService;

    @Autowired
    public PictureService(ResourceLoader resourceLoader, PictureRepository pictureRepository, UserService userService){
        this.resourceLoader = resourceLoader;
        this.pictureRepository = pictureRepository;
        this.userService = userService;
    }

    @Transactional
    public Picture createUsersPicture(@NotNull MultipartFile file,@NotNull Long id) throws IOException {
        if(file.isEmpty()){return null;}
        String filename = String.valueOf(id) + ".png";
        User user = userService.getUser(id);
        Picture picture = pictureRepository.findByUser(user);
        if(picture != null){
            delete(picture.getFilename());
        }else{
            picture = Picture.builder()
                    .filename(filename)
                    .user(user)
                    .build();
        }
        InputStream convertedImage = ImageUtils.getByteArrayInputStreamOfImageConvertedTo(
                ImageUtils.ImageFormat.PNG,
                file.getInputStream());
        Files.copy(convertedImage, Paths.get(PICTURES_ROOT, filename));
        return pictureRepository.save(picture);
    }

    public Resource getUsersPicture(@NotNull Long id){
        User user = userService.getUser(id);
        Picture picture = pictureRepository.findByUser(user);
        if(picture == null) return resourceLoader.getResource("file:" + PICTURES_ROOT + "/default.png");
        return resourceLoader.getResource("file:" + PICTURES_ROOT + "/" + picture.getFilename());
    }

    @Transactional
    public void deleteUsersPicture(@NotNull Long id) throws IOException {
        User user = userService.getUser(id);
        Picture picture = pictureRepository.findByUser(user);
        if(picture != null) {
            delete(picture.getFilename());
            pictureRepository.delete(picture);
        }
    }

    private void delete(String filename) throws IOException {
        Files.deleteIfExists(Paths.get(PICTURES_ROOT, filename));
    }

}
