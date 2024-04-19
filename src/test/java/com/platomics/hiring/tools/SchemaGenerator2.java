package com.platomics.hiring.tools;

import com.sun.codemodel.JCodeModel;
import lombok.extern.slf4j.Slf4j;
import org.jsonschema2pojo.DefaultGenerationConfig;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.Jackson2Annotator;
import org.jsonschema2pojo.SchemaMapper;
import org.jsonschema2pojo.SchemaStore;
import org.jsonschema2pojo.SourceType;
import org.jsonschema2pojo.rules.RuleFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class SchemaGenerator2 {


    public static void main(String[] args) throws IOException {
        SchemaGenerator2 schemaGenerator = new SchemaGenerator2();
        final Path jsonFile = Paths.get("src","main","resources", "schema_full/surveyjs_definition.json");
        final Path outputPath = Paths.get("target","generated-sources");
        createDictionary(outputPath);
        schemaGenerator.convertJsonToJavaClass(jsonFile.toUri().toURL(),outputPath.toFile(),"com.surveyjs.model","SurveyModel");
    }

    private static void createDictionary(Path filePath) {
        if (!Files.exists(filePath)){
            try {
                Files.createDirectories(filePath);
            }catch (Exception e){
                log.error("Directory does not exist and could not be created",e);
            }
        }
    }

    public void convertJsonToJavaClass(URL inputJsonUrl, File outputJavaClassDirectory, String packageName, String javaClassName)
            throws IOException {
        JCodeModel jcodeModel = new JCodeModel();

        GenerationConfig config = new DefaultGenerationConfig() {
            @Override
            public boolean isGenerateBuilders() {
                return true;
            }

            @Override
            public SourceType getSourceType() {
                return SourceType.JSONSCHEMA;
            }
        };

        SchemaMapper mapper = new SchemaMapper(new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore()), new org.jsonschema2pojo.SchemaGenerator());
        mapper.generate(jcodeModel, javaClassName, packageName, inputJsonUrl);

        jcodeModel.build(outputJavaClassDirectory);
    }


}
