package com.platomics.hiring.springboot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platomics.hiring.springboot.service.SurveyDefBasedValidatorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.List;

@Configuration
public class AppBeanConfiguration {




/*
    @Bean
    @Scope(value = WebApplicationContext.SCOPE_APPLICATION)
    public SurveyDefBasedValidatorService preloadedCountryCodesCache() throws IOException {
        SurveyModel surveyModel = objectMapper.readValue(surveyValidatorDefinition.getInputStream(), SurveyModel.class);
        SurveyDefBasedValidatorService surveyDefBasedValidatorService = new SurveyDefBasedValidatorService();
        surveyDefBasedValidatorService.setSurveyModel(surveyModel);
        return surveyDefBasedValidatorService;
    }*/

}
