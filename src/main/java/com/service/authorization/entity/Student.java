package com.service.authorization.entity;

import com.service.authorization.dto.response.StudentResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "m_student")
public class Student extends Auditable<String> {
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "student_id", unique = true)
    private String studentId;
    private String campus;
    private String major;
    private String status;
    private String gender;
    private String level;
    @Column(name = "join_date")
    private String joinDate;
    @OneToOne(mappedBy = "student")
    @ToString.Exclude
    private ProfilePicture profilePicture;
    @OneToOne
    private EntityCredential credential;

    public StudentResponse toResponse() {
        return StudentResponse.builder()
                .fullName(fullName)
                .studentId(studentId)
                .campus(campus)
                .major(major)
                .status(status)
                .gender(gender)
                .level(level)
                .joinDate(joinDate)
                .build();
    }
}