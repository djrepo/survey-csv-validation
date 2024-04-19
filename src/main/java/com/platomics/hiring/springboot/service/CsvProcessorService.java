package com.platomics.hiring.springboot.service;

import com.platomics.hiring.springboot.model.CsvRow;
import com.platomics.hiring.springboot.utils.CsvReaderWriter;
import com.platomics.hiring.springboot.web.errors.CsvSummaryValidationException;
import com.platomics.hiring.springboot.web.errors.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CsvProcessorService {

    @Autowired
    private SurveyDefBasedValidatorService surveyDefBasedValidatorService;

    public void process(MultipartFile csvFile) throws IOException {
        List<CsvRow> csvRows = CsvReaderWriter.load(csvFile.getInputStream(), CsvRow.class);
        List<String> validationErrors = new ArrayList<String>();
        for (int rowIndex=0;rowIndex<csvRows.size();rowIndex++) {
            try {
                surveyDefBasedValidatorService.validate(csvRows.get(rowIndex));
            }catch (CsvValidationException e){
                //first row in csv is header and real row number is not going from 0... but from 1...
                int csvRowIndex = rowIndex + 2;
                log.warn("Row "+csvRowIndex+" is invalid due to "+e.getMessage());
                validationErrors.add("Row "+csvRowIndex+" is invalid due to "+e.getMessage());
            }
        }
        if (validationErrors.size()>0){
            throw new CsvSummaryValidationException(validationErrors);
        }

    }



}
