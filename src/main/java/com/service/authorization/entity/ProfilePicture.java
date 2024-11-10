package com.service.authorization.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "m_profile_picture")
public class ProfilePicture extends File {
    @OneToOne
    @JoinColumn(name = "student_id", unique = true)
    private Student student;
}
