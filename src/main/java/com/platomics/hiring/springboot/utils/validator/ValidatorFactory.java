package com.platomics.hiring.springboot.utils.validator;

import com.platomics.hiring.springboot.model.ElementSurveyDef;
import com.platomics.hiring.springboot.web.errors.PlatomicsException;

public class ValidatorFactory {

    public static ValidatorBase createValidator(ElementSurveyDef element){
        if ("text".equals(element.getType())){
            return new TextValidator(element);
        }else if ("radiogroup".equals(element.getType())){
            return new RadioGroupValidator(element);
        }else if ("dropdown".equals(element.getType())){
            return new DropDownValidator(element);
        }
        throw new PlatomicsException("Validator type "+element.getType()+" not implemented");
    }

}
