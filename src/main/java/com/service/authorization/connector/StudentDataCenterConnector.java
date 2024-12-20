package com.service.authorization.connector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.authorization.config.HeaderConfig;
import com.service.authorization.dto.response.SdcDetailResponse;
import com.service.authorization.dto.response.SdcListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class StudentDataCenterConnector {

    private final HeaderConfig headerConfig;

    @Value("${dikti.url.base}")
    private String baseUrl;

    @Value("${dikti.url.search_student}")
    private String studentSearch;

    @Value("${dikti.url.student_detail}")
    private String studentDetail;

    private List<SdcListResponse> searchStudent(String studentId) {
        log.info("[SDC Connector] Try to search student with NIM: {}", studentId);

        String fullUrl = baseUrl + studentSearch + "/" + studentId;

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            // Membuat request GET
            HttpGet request = new HttpGet(fullUrl);

            // Menambahkan headers
            Map<String, String> headers = headerConfig.getHeaders().toSingleValueMap();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }

            log.info("[ARI] Try to search student with url: {}", fullUrl);
            log.info("[ARI] Current header entity: {}", headers);

            // Eksekusi request dan dapatkan response
            try (CloseableHttpResponse response = client.execute(request)) {
                String responseString = EntityUtils.toString(response.getEntity());
                if (response.getStatusLine().getStatusCode() != HttpStatus.OK.value()) {
                    log.error("[SDC Connector] Error: {}", response.getStatusLine());
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found!");
                }

                // Parse response menjadi objek
                SdcListResponse[] studentList = parseResponse(responseString);
                log.info("[SDC Connector] Success to search students with length: {}", studentList.length);

                return Arrays.asList(studentList);
            }
        } catch (IOException e) {
            log.error("[SDC Connector] Error: {}", e.getMessage());
            throw new RuntimeException("Error while searching student data", e);
        }
    }

    public SdcDetailResponse studentDetail(String studentId) {
        log.info("[SDC Connector] Try to get student detail with id: {}", studentId);

        try {
            SdcListResponse searchResult = searchFirst(studentId);
            String fullUrl = baseUrl + studentDetail + "/" + searchResult.getId();

            // Membuat request GET
            HttpGet request = new HttpGet(fullUrl);

            // Menambahkan headers
            Map<String, String> headers = headerConfig.getHeaders().toSingleValueMap();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }

            try (CloseableHttpClient client = HttpClients.createDefault()) {
                // Eksekusi request dan dapatkan response
                try (CloseableHttpResponse response = client.execute(request)) {
                    String responseString = EntityUtils.toString(response.getEntity());
                    if (response.getStatusLine().getStatusCode() != HttpStatus.OK.value()) {
                        log.error("[SDC Connector] Error: {}", response.getStatusLine());
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found!");
                    }

                    // Parse response menjadi objek SdcDetailResponse
                    SdcDetailResponse detailResponse = parseDetailResponse(responseString);
                    log.info("[SDC Connector] Success to get student detail for nim: {}", detailResponse.getNim());

                    return detailResponse;
                }
            }
        } catch (Exception e) {
            log.error("[SDC Connector] Error: {}", e.getMessage());
            throw new RuntimeException("Error while fetching student detail", e);
        }
    }

    private SdcListResponse searchFirst(String id) {
        List<SdcListResponse> searchResponse = this.searchStudent(id);
        return searchResponse.stream()
                .filter(student -> student.getNamaPt().equalsIgnoreCase("UNIVERSITAS SIBER ASIA"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    private SdcListResponse[] parseResponse(String responseString) throws JsonProcessingException {
        return new ObjectMapper().readValue(responseString, SdcListResponse[].class);
    }

    private SdcDetailResponse parseDetailResponse(String responseString) throws JsonProcessingException {
        return new ObjectMapper().readValue(responseString, SdcDetailResponse.class);
    }
}
