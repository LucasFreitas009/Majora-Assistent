package br.com.majora.assistent.service.voice;

import org.springframework.stereotype.Service;

@Service
public class VoiceService {
    private final AudioCaptureService audioCaptureService;
    private final SpeechRecognitionService speechRecognitionService;

    public VoiceService(AudioCaptureService audioCaptureService, SpeechRecognitionService speechRecognitionService) {
        this.audioCaptureService = audioCaptureService;
        this.speechRecognitionService = speechRecognitionService;
    }

    public void startListening() throws Exception {
        audioCaptureService.capture(speechRecognitionService);
    }

}
