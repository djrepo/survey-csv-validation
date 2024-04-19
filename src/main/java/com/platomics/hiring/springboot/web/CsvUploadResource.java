package com.platomics.hiring.springboot.web;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.platomics.hiring.springboot.service.CsvProcessorService;
import com.platomics.hiring.springboot.web.errors.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/csv")
public class CsvUploadResource {

    @Autowired
    private CsvProcessorService csvProcessorService;

    @PostMapping("/upload")
    public ResponseEntity<ObjectNode> uploadPredefinedPidFile(@RequestParam("file") MultipartFile uploadFile) throws Exception {
        log.info("Single file upload!");
        if (uploadFile.isEmpty()) {
            throw new CsvValidationException("Uploaded file is empty!");
        }
        csvProcessorService.process(uploadFile);
        return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.OK);
    }


}
