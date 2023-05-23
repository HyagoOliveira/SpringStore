package com.api.springstore.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.format.DateTimeFormatter;

@Configuration
public class DateConfigurator {
    @Bean
    @Primary
    public ObjectMapper getMapperForUTCLocalDateTime() {
        final String UTCTimeFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        final LocalDateTimeSerializer serializer = new LocalDateTimeSerializer(
                DateTimeFormatter.ofPattern(UTCTimeFormat)
        );

        var module = new JavaTimeModule();
        module.addSerializer(serializer);

        return new ObjectMapper().registerModule(module);
    }
}
