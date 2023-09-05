package se.webservices.WeatherForecast.services.smhi;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.webservices.WeatherForecast.services.smhi.data.Root;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


@Service
public class SmhiService {
    private static Root root;
    @Autowired
    private ForeCastService forecastService;

    public SmhiService(){
        try {
            root = readFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Root readFromFile() throws IOException {
        if(!Files.exists(Path.of("smhipredictions.json"))) return new Root();
        ObjectMapper objectMapper = getObjectMapper();
        var jsonStr = Files.readString(Path.of("smhipredictions.json"));
        return  objectMapper.readValue(jsonStr, Root.class );
    }

    private void writeAllToFile(Root smhiPredictions) throws IOException {
        ObjectMapper objectMapper = getObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);


        StringWriter stringWriter = new StringWriter();
        objectMapper.writeValue(stringWriter, smhiPredictions);

        Files.writeString(Path.of("smhipredictions.json"), stringWriter.toString());
    }

    private static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        //mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    public Root getsmhiPredictions(){
        return root;
    }

    public Root getRoot() {
        return root;
    }

    public void setRoot(Root root) throws IOException {
        SmhiService.root = root;
        writeAllToFile(root);
    }


}
