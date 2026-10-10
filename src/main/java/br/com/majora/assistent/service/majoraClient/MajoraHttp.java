package br.com.majora.assistent.service.majoraClient;

import br.com.majora.assistent.model.request.MajoraRequest;
import br.com.majora.assistent.model.response.MajoraResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


public class MajoraHttp {

    private final RestTemplate rest;

    public MajoraHttp(RestTemplate rest) {
        this.rest = rest;
    }

    public String response(String content, String modelLLM) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        MajoraRequest request = new MajoraRequest();

        request.setModel(modelLLM);

        MajoraRequest.Messages message = new MajoraRequest.Messages();
        message.setContent(content);

        List<MajoraRequest.Messages> messages = new ArrayList<>();
        request.setMessages(messages);
        request.getMessages().add(message);

        HttpEntity<MajoraRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<MajoraResponse> response = rest.postForEntity("http://localhost:11434/api/chat", entity, MajoraResponse.class);

        return response.getBody().getMessage().getContent();
    }
}
