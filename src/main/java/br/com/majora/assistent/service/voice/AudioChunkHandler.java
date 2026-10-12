package br.com.majora.assistent.service.voice;

import br.com.majora.assistent.service.cliServices.animations.MajoraCommandEmptyLog;
import br.com.majora.assistent.service.cliServices.animations.MajoraUserLog;

@FunctionalInterface
public interface AudioChunkHandler {

    void onAudioChunk(byte[] buffer, int bytesRead, String modelLLM, MajoraUserLog log, MajoraCommandEmptyLog logCommandEmpty);
}
