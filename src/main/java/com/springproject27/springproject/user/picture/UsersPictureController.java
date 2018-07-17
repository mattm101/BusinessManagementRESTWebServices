package com.springproject27.springproject.user.picture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
public class UsersPictureController {

    private PictureService pictureService;

    @Autowired
    public UsersPictureController(PictureService pictureService){
        this.pictureService = pictureService;
    }

    @GetMapping("/api/users/{id}/picture")
    public ResponseEntity<?> getUsersPicture(@PathVariable Long id) throws IOException {
        org.springframework.core.io.Resource file = pictureService.getUsersPicture(id);
        return ResponseEntity.ok()
                .contentLength(file.contentLength())
                .contentType(MediaType.IMAGE_PNG)
                .body(new InputStreamResource(file.getInputStream()));
    }

    @PostMapping(value = "/api/users/{id}/picture")
    @PreAuthorize("hasId(#id) or hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addPicture(@PathVariable Long id, @RequestParam("image") @NotNull MultipartFile file) throws IOException {
        pictureService.createUsersPicture(file, id);
    }

    @DeleteMapping("/api/users/{id}/picture")
    @PreAuthorize("hasId(#id) or hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePicture(@PathVariable Long id) throws IOException {
        pictureService.deleteUsersPicture(id);
    }
}
