package com.platomics.hiring.springboot.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class ElementSurveyDef {

    private static String TYPE = "type";
    private static String NAME = "name";
    private static String IS_REQUIRED = "isRequired";
    private static String VISIBLE_IF = "visibleIf";
    private static String CHOICES = "choices";
    private static String CHOICES_VALUE = "value";

    private String type;
    private String name;
    private VisibleIfSurveyDef visibleIfSurveyDef;
    private boolean isRequired;
    private List<String> choices;




    public static ElementSurveyDef createFromElement(Object element) {
        ElementSurveyDef elementSchemaDef = new ElementSurveyDef();
        if (element instanceof Map){
            Map elementPropertyMap = (Map)element;
            elementSchemaDef.type = safeGetString(elementPropertyMap, TYPE);
            elementSchemaDef.name = safeGetString(elementPropertyMap, NAME);
            String visibleIfContent = safeGetString(elementPropertyMap, VISIBLE_IF);
            elementSchemaDef.visibleIfSurveyDef = VisibleIfSurveyDef.createFromElement(visibleIfContent);
            elementSchemaDef.isRequired = safeGetBoolean(elementPropertyMap, IS_REQUIRED);
            elementSchemaDef.choices = convert(safeGetList(elementPropertyMap, CHOICES));
        }
        return elementSchemaDef;
    }

    // choices is defined as allOf, we simplify here and merging two types(array of strings, array of custom objects) to simple string array
    private static List<String> convert(List safeGetList) {
        if (safeGetList==null){
            return null;
        }
        List<String> ret = new ArrayList<>();
        for (Object o : safeGetList) {
            if (o instanceof String) {
                ret.add((String) o);
            } else if (o instanceof Map) {
                ret.add(safeGetString((Map) o, CHOICES_VALUE));
            }
        }
        return ret;
    }

    private static String safeGetString(Map map, String propertyName){
        Object propertyValue = map.get(propertyName);
        if (propertyValue instanceof String){
            return (String) propertyValue;
        }
        return null;
    }

    private static boolean safeGetBoolean(Map map, String propertyName){
        Object propertyValue = map.get(propertyName);
        if (propertyValue instanceof Boolean){
            return (Boolean) propertyValue;
        }
        return false;
    }

    private static List safeGetList(Map map, String propertyName){
        Object propertyValue = map.get(propertyName);
        if (propertyValue instanceof List){
            return (List) propertyValue;
        }
        return null;
    }
}
