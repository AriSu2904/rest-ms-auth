package com.service.authorization.connector;

import com.service.authorization.dto.response.SdcDetailResponse;
import com.service.authorization.dto.response.SdcListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class StudentDataCenterConnector {

    private final RestTemplate restTemplate;

    @Value("${dikti.url.base}")
    private String baseUrl;
    @Value("${dikti.url.search_student}")
    private String studentSearch;
    @Value("${dikti.url.student_detail}")
    private String studentDetail;
    @Value("${dikti.api_key}")
    private String xApiKey;

    private List<SdcListResponse> searchStudent(String studentId) {
        log.info("[SDC Connector] Try to search student with NIM: {}", studentId);

        String fullUrl = baseUrl + studentSearch + "/" + studentId;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set("X-Api-Key", xApiKey);

        try {
            HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

            ResponseEntity<SdcListResponse[]> response = restTemplate
                    .exchange(fullUrl, HttpMethod.GET, entity, SdcListResponse[].class);

            if(response.getStatusCode() != HttpStatus.OK) {
                log.error("[SDC Connector] Error: {}", response.getStatusCode());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found!");
            }

            log.info("[SDC Connector] Success to search students with length: {}", response.getBody().length);

            return Arrays.asList(response.getBody());
        }catch (Exception e){
            log.error("[SDC Connector] Error: {}", e.getMessage());

            throw new RuntimeException("Error while searching student data");
        }
    }

    public SdcDetailResponse studentDetail(String studentId) {
        log.info("[SDC Connector] Try to get student detail with id: {}", studentId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set("X-Api-Key", xApiKey);

        try {
            SdcListResponse searchResult = searchFirst(studentId);
            String fullUrl = baseUrl + studentDetail + "/" + searchResult.getId();

            HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

            ResponseEntity<SdcDetailResponse> response = restTemplate
                    .exchange(fullUrl, HttpMethod.GET, entity, SdcDetailResponse.class);

            log.info("[SDC Connector] Success to get student detail for nim: {}", response.getBody().getNim());

            return response.getBody();
        }catch (Exception e){
            log.error("[SDC Connector] Error: {}", e.getMessage());

            throw new RuntimeException("Error while searching student data");
        }
    }

    private SdcListResponse searchFirst(String id) {
        List<SdcListResponse> searchResponse = this.searchStudent(id);
        return searchResponse.stream().filter(student -> student.getNamaPt()
                        .equalsIgnoreCase("UNIVERSITAS SIBER ASIA")).findFirst()
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

}
