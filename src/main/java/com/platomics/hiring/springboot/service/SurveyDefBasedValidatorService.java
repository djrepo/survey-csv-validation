package com.platomics.hiring.springboot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platomics.hiring.springboot.model.CsvRow;
import com.platomics.hiring.springboot.model.ElementSurveyDef;
import com.platomics.hiring.springboot.model.PageSurveyDef;
import com.platomics.hiring.springboot.utils.validator.ValidatorBase;
import com.platomics.hiring.springboot.utils.validator.ValidatorFactory;
import com.platomics.hiring.springboot.web.errors.PlatomicsException;
import com.surveyjs.model.Page;
import com.surveyjs.model.SurveyjsDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SurveyDefBasedValidatorService {


    @Value("${platomics.survey_def:#{null}}") //classpath:survey.json
    private Resource surveyValidatorDefinition;

    @Autowired
    private ObjectMapper objectMapper;

    private List<ValidatorBase> validators;

    @PostConstruct
    public void init() throws IOException {
        PageSurveyDef pageSchemaDef = loadSurveyValidatorDefinition();
        this.validators = createAllValidators(pageSchemaDef);

    }

    public void validate(CsvRow row) {
        for (ValidatorBase validator : this.validators) {
            validator.validate(row);
        }
    }

    private PageSurveyDef loadSurveyValidatorDefinition() throws IOException {
        SurveyjsDefinition surveyModel = objectMapper.readValue(surveyValidatorDefinition.getInputStream(), SurveyjsDefinition.class);
        List<Page> pages = surveyModel.getPages();
        if (pages == null || pages.size() == 0) {
            throw new PlatomicsException("Cannot parse survey.json");
        }
        Page mainPage = pages.get(0);
        return PageSurveyDef.createFromElement(mainPage);
    }

    private List<ValidatorBase> createAllValidators(PageSurveyDef pageSchemaDef) {
        List<ElementSurveyDef> elements = pageSchemaDef.getElements();
        List<ValidatorBase> validators = new ArrayList<>();
        for (ElementSurveyDef element : elements) {
            ValidatorBase genericValidator = ValidatorFactory.createValidator(element);
            validators.add(genericValidator);
        }
        return validators;
    }


}
