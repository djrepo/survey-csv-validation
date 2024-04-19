package com.platomics.hiring.springboot.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.platomics.hiring.springboot.web.errors.PlatomicsException;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PojoExtractor {

    public static Collection<FieldInfo> extractFieldInfos(Class<?> clazz) {
        return extractFieldInfosMap(clazz).values();
    }

    public static Map<String,FieldInfo> extractFieldInfosMap(Class<?> clazz) {
        Map<String, FieldInfo> ret = new HashMap();
        try {
            Map<String,Field> fieldNames = getDeclaredFieldsSamePackage(clazz);
            BeanInfo info = Introspector.getBeanInfo(clazz);
            for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
                String propertyName = pd.getName();
                if (fieldNames.containsKey(propertyName)) {
                    Class<?> type = pd.getPropertyType();
                    Method getter = pd.getReadMethod();
                    Method setter = pd.getWriteMethod();
                    Field field = fieldNames.get(propertyName);
                    if (field.isAnnotationPresent(JsonProperty.class)) {
                        JsonProperty jsonPropertyAnnotation = field.getAnnotation(JsonProperty.class);
                        FieldInfo fieldInfo = new FieldInfo();
                        fieldInfo.name=propertyName;
                        fieldInfo.type=type;
                        fieldInfo.getter=getter;
                        fieldInfo.setter=setter;
                        fieldInfo.jsonProperty = jsonPropertyAnnotation.value();
                        ret.put(fieldInfo.jsonProperty,fieldInfo);
                    }
                }
            }
        } catch (IntrospectionException e) {
            throw new PlatomicsException("Problem getting beanInfo from class :" + clazz.getName());
        }
        return ret;
    }

    private static  Map<String,Field> getDeclaredFieldsSamePackage(Class<?> clazz) {
        Map<String,Field> ret = List.of(clazz.getDeclaredFields()).stream().collect(Collectors.toMap(Field::getName, Function.identity()));
        if (clazz.getSuperclass()!=null && clazz.getSuperclass().getPackageName().equals(clazz.getPackageName())){
            Map<String,Field> superClassDeclaredFields = getDeclaredFieldsSamePackage(clazz.getSuperclass());
            ret.putAll(superClassDeclaredFields);
        }
        return ret;
    }

    public static class FieldInfo {
        private String name;
        private Class<?> type;
        private Method getter;
        private Method setter;
        private String jsonProperty;

        public String getName() {
            return name;
        }

        public Class<?> getType() {
            return type;
        }

        public Method getGetter() {
            return getter;
        }

        public Method getSetter() {
            return setter;
        }

        public String getJsonProperty() {
            return jsonProperty;
        }
    }

}
