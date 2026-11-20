package br.com.majora.assistent.service.chat;

import br.com.majora.assistent.service.cliServices.animations.MajoraCommandEmptyLog;
import br.com.majora.assistent.service.cliServices.animations.MajoraUserLog;
import br.com.majora.assistent.service.majoraClient.MajoraDecision;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class ChatService {
    private final MajoraDecision majoraDecision;

    public ChatService(MajoraDecision majoraDecision) {
        this.majoraDecision = majoraDecision;
    }

    public void startChat(String modelLLM, MajoraUserLog log, MajoraCommandEmptyLog logCommandEmpty) {

        try(
           BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ) {
            String content = "";

            while(!content.equalsIgnoreCase("/exit")) {
                System.out.print("User -> ");
                content = reader.readLine();

                majoraDecision.decision(content, modelLLM, log, logCommandEmpty);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
