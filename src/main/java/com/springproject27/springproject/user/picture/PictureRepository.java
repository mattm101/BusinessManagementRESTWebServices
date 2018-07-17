package com.springproject27.springproject.user.picture;

import com.springproject27.springproject.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture, Long> {
    Picture findByUser(User user);
}
