package br.com.majora.assistent.config;

import br.com.majora.assistent.service.CommandService;
import br.com.majora.assistent.service.majoraClient.MajoraDecision;
import br.com.majora.assistent.service.majoraClient.MajoraHttp;
import br.com.majora.assistent.service.majoraClient.MajoraService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class Configurations {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
