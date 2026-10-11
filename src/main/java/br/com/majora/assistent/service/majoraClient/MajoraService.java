package br.com.majora.assistent.service.majoraClient;

import br.com.majora.assistent.model.request.commands.CommandInstruction;
import br.com.majora.assistent.model.request.commands.CommandResult;
import br.com.majora.assistent.model.response.OllamaModels;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class MajoraService {
    private final MajoraHttp majoraHttp;
    private final ObjectMapper mapper;

    public MajoraService(MajoraHttp majoraHttp, ObjectMapper mapper) {
        this.majoraHttp = majoraHttp;
        this.mapper = mapper;
    }

    public CommandInstruction send(String content, String modelLLM) {

        return mapper.readValue(majoraHttp.response(content, modelLLM), CommandInstruction.class);
    }

    public List<OllamaModels.Models> getModels() {
        return majoraHttp.getModels();
    }
}
