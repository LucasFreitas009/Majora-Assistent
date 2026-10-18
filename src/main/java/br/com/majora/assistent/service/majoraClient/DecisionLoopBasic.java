package br.com.majora.assistent.service.majoraClient;

import br.com.majora.assistent.model.request.commands.CommandInstruction;
import br.com.majora.assistent.model.request.commands.CommandResult;
import br.com.majora.assistent.service.CommandService;
import br.com.majora.assistent.service.cliServices.animations.MajoraCommandEmptyLog;
import br.com.majora.assistent.service.cliServices.animations.MajoraUserLog;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@Profile("basic")
public class DecisionLoopBasic implements DecisionLoop{

    @Override
    public void decisionLoop(CommandInstruction instruction, String content, String modelLLM, CommandService commandService, MajoraService majoraService, ObjectMapper mapper, MajoraUserLog log, MajoraCommandEmptyLog logCommandEmpty) {
        if(instruction.getCommand().isEmpty()) {
            Thread response = logCommandEmpty.isEmpty(instruction.getContext());

            try {
                response.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            return;
        }

        while(!instruction.getCommand().isEmpty()) {
            CommandResult resultLoop = commandService.execute(instruction);

            //System.out.println(mapper.writeValueAsString(resultLoop));

            Thread response = log.userLog(resultLoop);

            try {
                response.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            instruction = majoraService.send(mapper.writeValueAsString(resultLoop), modelLLM);
            /*System.out.println("LOG LOOP DE ENVIO");
                 System.out.println(resultLoop.getStdout());
                 System.out.println(instruction.getCommand());
                 System.out.println(instruction.getContext());*/
        }

        Thread finalResponse = logCommandEmpty.isEmpty(instruction.getContext());

        try {
            finalResponse.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
