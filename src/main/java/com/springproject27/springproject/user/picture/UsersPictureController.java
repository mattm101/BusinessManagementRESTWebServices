package com.springproject27.springproject.user.picture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequestMapping("/api/user/{id}/picture")
public class UsersPictureController {

    private PictureService pictureService;

    @Autowired
    public UsersPictureController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @GetMapping
    public ResponseEntity<?> getUsersPicture(@PathVariable Long id) throws IOException {
        Resource file = pictureService.getUsersPicture(id);
        return createResponseWithPngResourceAndStatus(file, HttpStatus.OK);
    }

    private ResponseEntity<?> createResponseWithPngResourceAndStatus(Resource resource, HttpStatus status) throws IOException {
        return ResponseEntity.status(status)
                .contentLength(resource.contentLength())
                .contentType(MediaType.IMAGE_PNG)
                .body(new InputStreamResource(resource.getInputStream()));
    }

    @PostMapping
    @PreAuthorize("hasId(#id) or hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addPicture(@PathVariable Long id, @RequestParam("image") @NotNull MultipartFile file) throws IOException {
        Picture picture = pictureService.createUsersPicture(file, id);
        Resource pictureResource = pictureService.getUsersPicture(picture);
        return createResponseWithPngResourceAndStatus(pictureResource, HttpStatus.CREATED);
    }

    @DeleteMapping
    @PreAuthorize("hasId(#id) or hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePicture(@PathVariable Long id) throws IOException {
        pictureService.deleteUsersPicture(id);
    }
}
