package com.platomics.hiring.springboot.utils.validator;

import com.platomics.hiring.springboot.model.ElementSurveyDef;
import com.platomics.hiring.springboot.web.errors.CsvValidationException;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class DropDownValidator extends TextValidator {

    public DropDownValidator(ElementSurveyDef element) {
        super(element);
    }

    @Override
    public void validateValue(boolean isFieldMandatory, Object value) {
        super.validateValue(isFieldMandatory, value);
        if (element.getChoices()!=null && !element.getChoices().contains(value)){
            if (!StringUtils.isEmpty((String)value)) {
                throw new CsvValidationException("Unexpected value (" + value + ") found in column " + element.getName());
            }
        }
    }
}
