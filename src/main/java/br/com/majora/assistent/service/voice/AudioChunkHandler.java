package br.com.majora.assistent.service.voice;

@FunctionalInterface
public interface AudioChunkHandler {

    void onAudioChunk(byte[] buffer, int bytesRead, String modelLLM);
}
