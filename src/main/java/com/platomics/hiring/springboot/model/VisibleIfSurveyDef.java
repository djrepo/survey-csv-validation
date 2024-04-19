package com.platomics.hiring.springboot.model;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class VisibleIfSurveyDef {

    private static char FIELD_NAME_START_CHARACTER = '{';
    private static char FIELD_NAME_END_CHARACTER = '}';
    private static char FIELD_VALUE_START_CHARACTER = '\'';
    private static char FIELD_VALUE_END_CHARACTER = '\'';

        private String referenceFieldName;
        private String referenceFieldValue;

        public static VisibleIfSurveyDef createFromElement(Object element) {
            VisibleIfSurveyDef visibleIfSurveyDef = new VisibleIfSurveyDef();
            if (element instanceof String){
                visibleIfSurveyDef.referenceFieldName = parseUsingStartEndCharacters((String)element,FIELD_NAME_START_CHARACTER,FIELD_NAME_END_CHARACTER);
                visibleIfSurveyDef.referenceFieldValue = parseUsingStartEndCharacters((String)element,FIELD_VALUE_START_CHARACTER,FIELD_VALUE_END_CHARACTER);
                if (visibleIfSurveyDef.referenceFieldName !=null && visibleIfSurveyDef.referenceFieldValue!=null)
                return visibleIfSurveyDef;
            }
            return null;
        }

    private static String parseUsingStartEndCharacters(String expression,char startChar,char endChar) {
        if (!StringUtils.isEmpty(expression)) {
            int startIndex = expression.indexOf(startChar);
            int endIndex = expression.lastIndexOf(endChar);
            if (startIndex>=0 && endIndex>=0 && startIndex+1<expression.length()) {
                String parsedValue = expression.substring(startIndex + 1, endIndex).trim();
                if (!StringUtils.isEmpty(parsedValue)) {
                    return parsedValue.trim();
                }
            }
        }
        return null;
    }
}
