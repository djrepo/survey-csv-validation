package com.platomics.hiring.springboot.utils.validator;


import com.platomics.hiring.springboot.model.CsvRow;
import com.platomics.hiring.springboot.model.ElementSurveyDef;

public abstract class ValidatorBase {

    protected ElementSurveyDef element;

    public ValidatorBase(ElementSurveyDef element){
        this.element = element;
    }

    public void validate(CsvRow csvRow){
        boolean isFieldMandatory = isFieldMandatory(csvRow);
        Object valueOfColumnThisValidatorDefine = csvRow.getFieldAccess().getFieldValueByFieldName(element.getName());
        validateValue(isFieldMandatory, valueOfColumnThisValidatorDefine);
    }

    public abstract void validateValue(boolean isFieldMandatory, Object value);

    public boolean isFieldMandatory(CsvRow row) {
        if (element.isRequired()){
            if (element.getVisibleIfSurveyDef()!=null){
                String referenceFieldName = element.getVisibleIfSurveyDef().getReferenceFieldName();
                Object columnValue = row.getFieldAccess().getFieldValueByFieldName(referenceFieldName);
                return element.getVisibleIfSurveyDef().getReferenceFieldValue().equals(columnValue);
            }
            return true;
        }
        return false;
    }
}
