package com.service.authorization.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {
    private String fullName;
    private String studentId;
    private String campus;
    private String major;
    private String status;
    private String gender;
    private String level;
    private String joinDate;
    private FileResponse profilePicture;
}
