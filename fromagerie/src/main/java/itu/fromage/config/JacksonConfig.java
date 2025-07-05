package itu.fromage.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Support des LocalDate, LocalDateTime, etc.
        
        // Désactiver la sérialisation des beans vides
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        
        // Ignorer les propriétés inconnues
        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Dates en format ISO
        
        return mapper;
    }
} 