//package com.kakaobean.core.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.module.SimpleModule;
//import com.kakaobean.core.project.application.dto.response.FindProjectResponseDto;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class CustomObjectMapperConfig {
//
//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        // Create a module for custom serialization/deserialization
//        SimpleModule customModule = new SimpleModule();
//        customModule.addSerializer(FindProjectResponseDto.class, new FindProjectResponseDtoJsonModule.FindProjectResponseDtoSerializer());
//        customModule.addDeserializer(FindProjectResponseDto.class, new FindProjectResponseDtoJsonModule.FindProjectResponseDtoDeserializer());
//
//        // Register the custom module
//        objectMapper.registerModule(customModule);
//
//        return objectMapper;
//    }
//}
