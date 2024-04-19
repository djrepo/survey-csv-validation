package com.platomics.hiring.springboot.utils;

import com.platomics.hiring.springboot.model.CsvRow;
import com.platomics.hiring.springboot.web.errors.PlatomicsException;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class CsvRowFieldAccess {

    private Map<String, PojoExtractor.FieldInfo> csvRowFieldInfoMap;
    private CsvRow row;

    public CsvRowFieldAccess(CsvRow row) {
        Map<String, PojoExtractor.FieldInfo> csvRowFieldInfoMap = PojoExtractor.extractFieldInfosMap(CsvRow.class);
        this.row=row;
    }

    public Object getFieldValueByFieldName(String fieldName) {
        try {
            return csvRowFieldInfoMap.get(fieldName).getGetter().invoke(this.row);
        }catch (InvocationTargetException e) {
            throw new PlatomicsException("Cannot read filed name "+fieldName+" in csv row "+this.row);
        }catch (IllegalAccessException e){
            throw new PlatomicsException("Cannot read filed name "+fieldName+" in csv row "+this.row);
        }
    }
}
