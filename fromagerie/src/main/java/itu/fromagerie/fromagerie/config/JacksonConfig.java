package itu.fromagerie.fromagerie.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
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
        
        // Module pour gérer les proxies Hibernate
        Hibernate6Module hibernate6Module = new Hibernate6Module();
        hibernate6Module.disable(Hibernate6Module.Feature.USE_TRANSIENT_ANNOTATION);
        hibernate6Module.configure(Hibernate6Module.Feature.FORCE_LAZY_LOADING, false);
        hibernate6Module.configure(Hibernate6Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS, true);
        
        mapper.registerModule(hibernate6Module);
        
        // Module pour gérer les types de date Java 8 (LocalDate, LocalDateTime, etc.)
        mapper.registerModule(new JavaTimeModule());
        
        return mapper;
    }
}
