package com.kakaobean.core.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kakaobean.core.project.application.dto.response.FindProjectResponseDto;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class FindProjectResponseDtoJsonModule extends SimpleModule {

    public FindProjectResponseDtoJsonModule() {
        addSerializer(FindProjectResponseDto.class, new FindProjectResponseDtoSerializer());
        addDeserializer(FindProjectResponseDto.class, new FindProjectResponseDtoDeserializer());
    }

    public static class FindProjectResponseDtoSerializer extends JsonSerializer<FindProjectResponseDto> {
        @Override
        public void serialize(FindProjectResponseDto value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            // Implement your custom serialization logic here
            gen.writeStartObject();
            gen.writeNumberField("projectId", value.getProjectId());
            gen.writeStringField("projectTitle", value.getProjectTitle());
            gen.writeStringField("projectContent", value.getProjectContent());
            gen.writeEndObject();
        }
    }

    public static class FindProjectResponseDtoDeserializer extends StdDeserializer<FindProjectResponseDto> {
        protected FindProjectResponseDtoDeserializer() {
            super(FindProjectResponseDto.class);
        }

        @Override
        public FindProjectResponseDto deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            // Implement your custom deserialization logic here
            JsonNode node = p.getCodec().readTree(p);
            Long projectId = node.get("projectId").asLong();
            String projectTitle = node.get("projectTitle").asText();
            String projectContent = node.get("projectContent").asText();
            return new FindProjectResponseDto(projectId, projectTitle, projectContent);
        }
    }
}
