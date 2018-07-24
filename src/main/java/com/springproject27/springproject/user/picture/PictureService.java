package com.springproject27.springproject.user.picture;

import com.springproject27.springproject.config.configservice.PictureConfigService;
import com.springproject27.springproject.user.User;
import com.springproject27.springproject.user.UserService;
import com.springproject27.springproject.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class PictureService {

    private final PictureConfigService pictureConfig;
    private final ResourceLoader resourceLoader;
    private final PictureRepository pictureRepository;
    private final UserService userService;


    @Transactional
    public Picture createUsersPicture(@NotNull MultipartFile file, @NotNull Long id) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        String filename = String.valueOf(id) + ".png";

        User user = userService.getUser(id);
        Picture picture = pictureRepository.findByUser(user);

        deleteIfExists(filename);

        if (picture != null) {
            pictureRepository.delete(picture);
        }

        picture = Picture.builder()
                .filename(filename)
                .user(user)
                .build();

        InputStream convertedImage = ImageUtils.convertImageToFormat(
                ImageUtils.ImageFormat.PNG,
                file.getInputStream());

        Files.copy(convertedImage, Paths.get(pictureConfig.getUsersPictureRoot(), filename));

        return pictureRepository.save(picture);
    }

    public Resource getUsersPicture(@NotNull Long id) {
        User user = userService.getUser(id);
        Picture picture = pictureRepository.findByUser(user);
        return loadPictureResource(picture);
    }

    private Resource loadPictureResource(Picture picture) {
        if (picture == null) return loadDefaultPictureResource();
        return resourceLoader.getResource("file:" + pictureConfig.getUsersPictureRoot() + "/" + picture.getFilename());
    }

    private Resource loadDefaultPictureResource() {
        return resourceLoader.getResource("file:" + pictureConfig.getUsersPictureRoot() + "/default.png");
    }

    public Resource getUsersPicture(@NotNull Picture picture) {
        return loadPictureResource(picture);
    }

    @Transactional
    public void deleteUsersPicture(@NotNull Long id) throws IOException {
        User user = userService.getUser(id);
        Picture picture = pictureRepository.findByUser(user);
        if (picture != null) {
            deleteIfExists(picture.getFilename());
            pictureRepository.delete(picture);
        }
    }

    private void deleteIfExists(String filename) throws IOException {
        Files.deleteIfExists(Paths.get(pictureConfig.getUsersPictureRoot(), filename));
    }

}
