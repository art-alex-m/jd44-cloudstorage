package ru.netology.cloudstorage.webapp.boundary;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@AllArgsConstructor
@Getter
@JsonDeserialize(using = AppUpdateCloudFileInputRequest.Deserializer.class)
public class AppUpdateCloudFileInputRequest {
    @NotEmpty
    private String newFileName;

    @Component
    public static class Deserializer extends StdDeserializer<AppUpdateCloudFileInputRequest> {

        public Deserializer() {
            super(AppUpdateCloudFileInputRequest.class);
        }

        @Override
        public AppUpdateCloudFileInputRequest deserialize(JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException, JacksonException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            String newFileName = node.get("filename").asText();

            return new AppUpdateCloudFileInputRequest(newFileName);
        }
    }
}
