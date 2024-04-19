package com.platomics.hiring.springboot.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvReaderWriter {

    public static ObjectWriter createWriter(Class def, boolean header) {
        CsvSchema schema = createSchema(def, header);
        CsvMapper mapper = createBasicMapper();
        return mapper.writerFor(def).with(schema);
    }

    private static ObjectReader createReader(Class def) {
        CsvSchema schema = createSchema(def, true);
        CsvMapper mapper = createBasicMapper();
        ObjectReader reader = mapper.readerFor(def).with(schema);
        reader = reader.without(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
        return reader;
    }

    public static <T> void store(File dataFile, Class<T> clazz, List<T> list) throws IOException {
        ObjectWriter writer = createWriter(clazz, true);
        if (list.size() > 0) {
            try (OutputStream outstream = new FileOutputStream(dataFile)) {
                writer.writeValues(outstream).writeAll(list);
            }
        }
    }

    public static <T> List<T> load(InputStream inputStream, Class<T> clazz) throws IOException {
        ObjectReader reader = createReader(clazz);
        MappingIterator<T> readValues = reader.readValues(inputStream);
        List<T> rows = readValues.readAll();
        return rows;
    }

    public static <T> List<T> load(File dataFile, Class<T> clazz) throws IOException {
        ObjectReader reader = createReader(clazz);
        MappingIterator<T> readValues = reader.readValues(dataFile);
        List<T> rows = readValues.readAll();
        return rows;
    }

    private static CsvMapper createBasicMapper() {
        CsvMapper mapper = new CsvMapper();
        mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    private static CsvSchema createSchema(Class def, boolean header) {
        Class superClass = def.getSuperclass();
        Field[] fieldsInParent = new Field[0];
        if (superClass !=null) {
            fieldsInParent = superClass.getDeclaredFields();
        }
        List<String> fieldNameList = Stream.of(fieldsInParent,def.getDeclaredFields()).flatMap(Stream::of).filter(f -> f.getAnnotation(JsonProperty.class)!=null).map(f -> f.getAnnotation(JsonProperty.class).value()).collect(Collectors.toList());
        CsvSchema schema = CsvSchema.builder().setUseHeader(header)
                .setColumnSeparator(',')
                //.disableQuoteChar()
                .addColumns(fieldNameList, CsvSchema.ColumnType.STRING)
                .build();
        return schema;
    }

    public static long size(Path dataFile) throws IOException {
        List<String> lines = Files.readAllLines(dataFile);
        return lines.size();
    }

}
