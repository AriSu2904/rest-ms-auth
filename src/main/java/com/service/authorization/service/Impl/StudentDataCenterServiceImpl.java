package com.service.authorization.service.Impl;

import com.service.authorization.connector.StudentDataCenterConnector;
import com.service.authorization.dto.response.SdcDetailResponse;
import com.service.authorization.entity.Student;
import com.service.authorization.service.StudentDataCenterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentDataCenterServiceImpl implements StudentDataCenterService {

    private final StudentDataCenterConnector dataCenterConnector;


    @Override
    public Student studentDetail(String studentId) {
        log.info("[SDC Service] Try to search student with NIM: {}", studentId);

        SdcDetailResponse studentDetail = dataCenterConnector.studentDetail(studentId);

        log.info("[SDC Service] Success to search students: {}", studentDetail.getNama());

        return Student.builder()
                .fullName(studentDetail.getNama())
                .studentId(studentDetail.getNim())
                .campus(studentDetail.getNamaPT())
                .major(studentDetail.getProdi())
                .status(studentDetail.getStatusSaatIni())
                .gender(studentDetail.getJenisKelamin())
                .level(studentDetail.getJenjang())
                .joinDate(studentDetail.getTanggalMasuk())
                .build();
    }
}
