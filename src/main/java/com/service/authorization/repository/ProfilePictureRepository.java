package com.service.authorization.repository;

import com.service.authorization.entity.ProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, String> {
}
