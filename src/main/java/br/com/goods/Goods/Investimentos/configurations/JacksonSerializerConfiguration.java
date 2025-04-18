package br.com.goods.Goods.Investimentos.configurations;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.jackson.JsonComponentModule;
import org.springframework.cloud.openfeign.support.PageJacksonModule;
import org.springframework.cloud.openfeign.support.SortJacksonModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;

@Configuration
public class JacksonSerializerConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        builder.modules(new PageJacksonModule(),new SortJacksonModule(), new JavaTimeModule(), new JsonComponentModule());
        builder.dateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX"));
        builder.failOnUnknownProperties(false);
        return builder;
    }
}
