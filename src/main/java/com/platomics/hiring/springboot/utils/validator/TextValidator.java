package com.platomics.hiring.springboot.utils.validator;

import com.platomics.hiring.springboot.model.ElementSurveyDef;
import com.platomics.hiring.springboot.web.errors.CsvValidationException;
import org.apache.commons.lang3.StringUtils;

public class TextValidator extends ValidatorBase {

    public TextValidator(ElementSurveyDef element) {
        super(element);
    }

    @Override
    public void validateValue(boolean isFieldMandatory, Object value) {
        if (value != null && !(value instanceof String)) {
            throw new CsvValidationException("Not a text");
        }
        String valueAsString = (String) value;
        if (isFieldMandatory && StringUtils.isEmpty(valueAsString)) {
            throw new CsvValidationException("Value is required for column "+element.getName());
        }
    }
}
