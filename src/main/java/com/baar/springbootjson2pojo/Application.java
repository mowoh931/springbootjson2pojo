package com.baar.springbootjson2pojo;

import com.baar.springbootjson2pojo.model.Teacher;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.codemodel.JCodeModel;
import org.jsonschema2pojo.*;
import org.jsonschema2pojo.rules.RuleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@RestController
@RequestMapping
@SpringBootApplication
public class Application {
    private static final
    Logger logger = LoggerFactory.getLogger(Application.class);

    /**
     * Converts the json to java class.
     *
     * @param inputJsonUrl             The input json url.
     * @param outputJavaClassDirectory The output java class directory.
     * @param packageName              The package name.
     * @param javaClassName            The java class name.
     * @throws IOException             The io exception
     */
    public static void convertJsonToJavaClass(URL inputJsonUrl, File outputJavaClassDirectory, String packageName, String javaClassName)
            throws IOException {
        JCodeModel jcodeModel = new JCodeModel();

        GenerationConfig config = new DefaultGenerationConfig() {
            @Override
            public boolean isGenerateBuilders() {
                return true;
            }

            @Override
            public SourceType getSourceType() {
                return SourceType.JSON;
            }
        };

        // Generates the java class model.
        SchemaMapper mapper = new SchemaMapper(new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore()), new SchemaGenerator());

        mapper.generate(jcodeModel, javaClassName, packageName, inputJsonUrl);

        // Generates the java class file.
        jcodeModel.build(outputJavaClassDirectory);


    }


    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class, args);
        // The input json url.
        URL inputJsonUrl = Application.class.getClassLoader().getResource("data/inputJsonUrl.json");
        // The output java class directory.
        File outputJavaClassDirectory = new File("src/main/java/com/baar/springbootjson2pojo/");
        // The package name and the java class name.
        String packageName = "model";
        // The java class name.
        String javaClassName = "Teacher";

        // Convert the json to java class.
        logger.info("========================================");
        logger.info("Starting to convert json to java class.");
        logger.info("========================================");
     //   Application.convertJsonToJavaClass(inputJsonUrl, outputJavaClassDirectory, packageName, javaClassName);
        logger.info("========================================");
        logger.info("Successfully converted json to java class.");
        logger.info("========================================");
    }

    @GetMapping(value = "/")
    public Teacher getTeacher() throws IOException, URISyntaxException {

        ObjectMapper mapper = new ObjectMapper();

        URI uri = new URI("src/main/resources/data/inputJsonUrl.json");
        File file = new File(uri.getPath());

        // Convert JSON string from file to Object
        return mapper.readValue(file, Teacher.class);
    }

}
