package com.platomics.hiring.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.platomics.hiring.springboot.web.CsvUploadResource;
import com.platomics.hiring.springboot.web.errors.CsvSummaryValidationException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class ComponentImporterApplicationTests {

	@Autowired
	private CsvUploadResource csvUploadResource;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void uploadValid() throws Exception {
		//final Path resourcePath = Paths.get("src","test","resources","valid","valid.csv");
		ClassPathResource classPathResource = new ClassPathResource("valid/valid.csv");
		MultipartFile multipartFile = new MockMultipartFile("file", classPathResource.getFilename(), "text/plain",
				IOUtils.toByteArray(classPathResource.getInputStream()));
		csvUploadResource.uploadPredefinedPidFile(multipartFile);
	}

	@Test
	void uploadInValid1() throws IOException {
		ClassPathResource classPathResource = new ClassPathResource("invalid/ce-ivdd-missing-fields.csv");
		MultipartFile multipartFile = new MockMultipartFile("file", classPathResource.getFilename(), "text/plain",
				IOUtils.toByteArray(classPathResource.getInputStream()));
		try {
			ResponseEntity<ObjectNode> responseEntity = csvUploadResource.uploadPredefinedPidFile(multipartFile);
		} catch (Exception e){
			Assertions.assertTrue(e instanceof CsvSummaryValidationException);
		}
		//CsvSummaryValidationErrorResponse cvSummaryValidationErrorResponse = objectMapper.treeToValue(responseEntity.getBody(),CsvSummaryValidationErrorResponse.class);
		//Assertions.assertTrue(cvSummaryValidationErrorResponse.getErrors().get(0).contains("Row 23 is invalid due to Value is required for column component_list_IVDD"));
	}

	@Test
	void uploadAllInValid2() throws Exception {
		final Path invalidDir = Paths.get("src","test","resources","invalid");
		List<Path> invalidCsvs = Files.list(invalidDir).collect(Collectors.toList());
		for (Path invalidCsv : invalidCsvs){
			ClassPathResource classPathResource = new ClassPathResource("invalid/" + invalidCsv.getFileName());
			MultipartFile multipartFile = new MockMultipartFile("file", classPathResource.getFilename(), "text/plain",
					IOUtils.toByteArray(classPathResource.getInputStream()));

			try {
				ResponseEntity<ObjectNode> responseEntity = csvUploadResource.uploadPredefinedPidFile(multipartFile);
			} catch (Exception e) {
				Assertions.assertTrue(e instanceof CsvSummaryValidationException);
			}
		}

	}

}
