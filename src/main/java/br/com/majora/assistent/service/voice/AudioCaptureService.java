package br.com.majora.assistent.service.voice;

import org.springframework.stereotype.Service;

import javax.sound.sampled.*;

@Service
public class AudioCaptureService {
    private volatile TargetDataLine microphone;

    private final AudioFormat audioFormat = new AudioFormat(
            16000,
            16,
            1,
            true,
            false
    );

    public void capture(AudioChunkHandler handler, String modelLLM) throws LineUnavailableException {
        microphone = getMicrophone();

        try {
            microphone.open(audioFormat);
            microphone.start();

            byte[] buffer = new byte[4096];

            System.out.println("Diga [COMPUTADOR] para começar conversa.\n");

            while(!Thread.currentThread().isInterrupted()) {

                int bytesRead = microphone.read(buffer, 0, buffer.length);

                if(bytesRead > 0) {
                    handler.onAudioChunk(buffer, bytesRead, modelLLM);
                }
            }
        } finally {
            microphone.stop();
            microphone.close();
        }
    }

    private TargetDataLine getMicrophone() throws LineUnavailableException {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);

        return (TargetDataLine) AudioSystem.getLine(info);
    }
}
