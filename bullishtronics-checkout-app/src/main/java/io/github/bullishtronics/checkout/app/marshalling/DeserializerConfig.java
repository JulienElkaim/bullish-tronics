package io.github.bullishtronics.checkout.app.marshalling;

import io.github.bullishtronics.checkout.serialization.MixinsConfig;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeserializerConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.mixIns(MixinsConfig.JACKSON_MIXINS);
    }
}
