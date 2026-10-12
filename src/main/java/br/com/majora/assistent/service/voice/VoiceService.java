package br.com.majora.assistent.service.voice;

import br.com.majora.assistent.service.cliServices.animations.MajoraCommandEmptyLog;
import br.com.majora.assistent.service.cliServices.animations.MajoraUserLog;
import org.springframework.stereotype.Service;

@Service
public class VoiceService {
    private final AudioCaptureService audioCaptureService;
    private final SpeechRecognitionService speechRecognitionService;

    public VoiceService(AudioCaptureService audioCaptureService, SpeechRecognitionService speechRecognitionService) {
        this.audioCaptureService = audioCaptureService;
        this.speechRecognitionService = speechRecognitionService;
    }

    public void startListening(String modelLLM, MajoraUserLog log, MajoraCommandEmptyLog logCommandEmpty) throws Exception {
        audioCaptureService.capture(speechRecognitionService, modelLLM, log, logCommandEmpty);
    }

}
