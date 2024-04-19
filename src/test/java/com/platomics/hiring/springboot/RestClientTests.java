package com.platomics.hiring.springboot;

import com.platomics.hiring.springboot.web.errors.CsvSummaryValidationErrorResponse;
import com.platomics.hiring.springboot.web.errors.CsvSummaryValidationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestClientTests {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;

    private final String URL = "http://localhost:%s/csv/upload";

    @Test
    void uploadValid() {
        ClassPathResource classPathResource = new ClassPathResource("valid/valid.csv");
        String uriBase = String.format(URL, port);
        log.info(String.format("Port: %s for URL-Base: %s", port, uriBase));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(createBody(classPathResource), createHttpHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange(uriBase, HttpMethod.POST, requestEntity, String.class);
        Assertions.assertTrue(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() == null);
    }

    @Test
    void uploadInValid() {
        ClassPathResource classPathResource = new ClassPathResource("invalid/ce-ivdd-missing-fields.csv");
        String uriBase = String.format(URL, port);
        log.info(String.format("Port: %s for URL-Base: %s", port, uriBase));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(createBody(classPathResource), createHttpHeaders());
        ResponseEntity<CsvSummaryValidationErrorResponse> response = testRestTemplate.exchange(uriBase, HttpMethod.POST, requestEntity, CsvSummaryValidationErrorResponse.class);
        Assertions.assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
        Assertions.assertTrue(response.getBody().getErrors().size()>0);
        Assertions.assertTrue(response.getBody().getErrors().get(0).contains("Row 23 is invalid due to Value is required for column component_list_IVDD"));

    }

    private MultiValueMap createBody(Resource resource) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource);
        return body;
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return headers;
    }

}
