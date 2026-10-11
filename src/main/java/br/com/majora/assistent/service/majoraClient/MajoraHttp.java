package br.com.majora.assistent.service.majoraClient;

import br.com.majora.assistent.model.request.MajoraRequest;
import br.com.majora.assistent.model.request.Messages;
import br.com.majora.assistent.model.response.MajoraResponse;
import br.com.majora.assistent.model.response.OllamaModels;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


public class MajoraHttp {

    private final RestTemplate rest;

    @Value("${ollama.url.chat}")
    private String urlChat;

    @Value("${ollama.url.tags}")
    private String urlTags;
    List<Messages> messages = new ArrayList<>();

    public MajoraHttp(RestTemplate rest) {
        this.rest = rest;
    }

    public String response(String content, String modelLLM) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        MajoraRequest request = new MajoraRequest();
        request.setModel(modelLLM);

        Messages message = new Messages();
        message.setRole("user");
        message.setContent(content);

        messages.add(message);

        request.setMessages(messages);

        HttpEntity<MajoraRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<MajoraResponse> response = rest.postForEntity(urlChat, entity, MajoraResponse.class);

        messages.add(response.getBody().getMessage());

        System.out.println("HISTORICO DE MENSAGENS");
        for(Messages message1 : messages) {
            System.out.println(message1.getRole());
            System.out.println(message1.getContent());
        }

        return response.getBody().getMessage().getContent();
    }

    public List<OllamaModels.Models> getModels() {
        ResponseEntity<OllamaModels> response = rest.getForEntity(urlTags, OllamaModels.class);

        return response.getBody().getModels();
    }
}
