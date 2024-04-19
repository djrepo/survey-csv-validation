package com.platomics.hiring.springboot.model;

import com.surveyjs.model.Page;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PageSurveyDef {
    private static String ELEMENTS = "elements";
    private List<ElementSurveyDef> elements;

    public static PageSurveyDef createFromElement(Page page) {
        Object elements = page.getAdditionalProperties().get(ELEMENTS);
        PageSurveyDef pageSchemaDef = new PageSurveyDef();
        pageSchemaDef.elements = new ArrayList<>();
        if (elements instanceof List){
            List elementList = (List)elements;
            for (Object element : elementList) {
                pageSchemaDef.elements.add(ElementSurveyDef.createFromElement(element));
            }
        }
        return pageSchemaDef;
    }
}
