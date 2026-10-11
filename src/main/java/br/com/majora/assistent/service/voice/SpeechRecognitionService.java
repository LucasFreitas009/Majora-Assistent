package br.com.majora.assistent.service.voice;

import br.com.majora.assistent.service.majoraClient.MajoraDecision;
import br.com.majora.assistent.service.majoraClient.MajoraService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.vosk.Model;
import org.vosk.Recognizer;
import tools.jackson.databind.ObjectMapper;

import java.util.regex.Pattern;


@Service
public class SpeechRecognitionService implements AudioChunkHandler {

    @Value("${vosk-path}")
    private String modelPath;

    private Model model;
    private Recognizer recognizer;
    private final MajoraDecision decisionService;
    private final ObjectMapper mapper;
    private static final Pattern WAKE_WORD_PATTERN = Pattern.compile("^\\bcomputador\\b", Pattern.CASE_INSENSITIVE);

    public SpeechRecognitionService(MajoraDecision decisionService, ObjectMapper mapper) {
        this.mapper = mapper;
        this.decisionService = decisionService;
    }

    @PostConstruct
    public void initialize() throws Exception {
        model = new Model(modelPath);
        recognizer = new Recognizer(model, 16000);
    }

    @Override
    public void onAudioChunk(byte[] buffer, int bytesRead, String modelLLM) {
        if(!recognizer.acceptWaveForm(buffer, bytesRead)) {
            return;
            //System.out.println(recognizer.getFinalResult());
        }

        String resultJson = recognizer.getResult();

        try {
            String text = mapper
                    .readTree(resultJson)
                    .path("text")
                    .asString()
                    .trim();

            if(text.isBlank()) {
                return;
            }

            //System.out.println("Texto reconhecido: " + text);

            if (WAKE_WORD_PATTERN.matcher(text).find()) {
                System.out.println("Processando Comando...\n");

                decisionService.decisionLoop(text, modelLLM);

            }

        } catch (Exception e) {
            System.err.println("Erro ao interpretar o resultado do vosk: " + e.getMessage());
        }
    }

   // @PreDestroy
    //public void close() {
      //  if(recognizer != null) {
        //    recognizer.close();
        //}

        //if (model != null) {
          //  model.close();
        //}
    //}
}
