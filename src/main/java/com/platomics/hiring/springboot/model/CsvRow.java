package com.platomics.hiring.springboot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.platomics.hiring.springboot.utils.PojoExtractor;
import com.platomics.hiring.springboot.web.errors.PlatomicsException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class CsvRow {

    @JsonProperty("component_name")
    private String componentName;
    @JsonProperty("component_language")
    private String componentLanguage;
    @JsonProperty("component_regulatory_clearance")
    private String componentRegulatoryClearance;
    @JsonProperty("component_list_IVDD")
    private String componentListIVDD;
    @JsonProperty("component_risk_class_IVDR")
    private String componentRiskClassIVDR;
    @JsonProperty("component_risk_class_MDR_MDD")
    private String componentRiskClassMDRMDD;
    @JsonProperty("component_risk_class_MDR")
    private String componentRiskClassMDR;
    @JsonProperty("component_type")
    private String componentType;

    @Override
    public String toString() {
        return "CsvRow{" +
                "componentName='" + componentName + '\'' +
                ", componentLanguage='" + componentLanguage + '\'' +
                ", componentRegulatoryClearance='" + componentRegulatoryClearance + '\'' +
                ", componentListIVDD='" + componentListIVDD + '\'' +
                ", componentRiskClassIVDR='" + componentRiskClassIVDR + '\'' +
                ", componentRiskClassMDRMDD='" + componentRiskClassMDRMDD + '\'' +
                ", componentRiskClassMDR='" + componentRiskClassMDR + '\'' +
                ", componentType='" + componentType + '\'' +
                '}';
    }

    public FieldAccess getFieldAccess(){
        return new FieldAccess(this);
    }


    public static class FieldAccess {

        private Map<String, PojoExtractor.FieldInfo> csvRowFieldInfoMap;
        private CsvRow row;

        public FieldAccess(CsvRow row) {
            this.csvRowFieldInfoMap = PojoExtractor.extractFieldInfosMap(CsvRow.class);
            this.row=row;
        }

        public Object getFieldValueByFieldName(String fieldName) {
            try {
                return csvRowFieldInfoMap.get(fieldName).getGetter().invoke(this.row);
            }catch (InvocationTargetException | IllegalAccessException e) {
                throw new PlatomicsException("Cannot read filed name "+fieldName+" in csv row "+this.row);
            }
        }
    }
}
