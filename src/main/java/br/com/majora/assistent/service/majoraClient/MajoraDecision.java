package br.com.majora.assistent.service.majoraClient;

import br.com.majora.assistent.model.request.commands.CommandInstruction;
import br.com.majora.assistent.model.request.commands.CommandResult;
import br.com.majora.assistent.service.CommandService;
import br.com.majora.assistent.service.cliServices.animations.MajoraCommandEmptyLog;
import br.com.majora.assistent.service.cliServices.animations.MajoraUserLog;


public class MajoraDecision {
    private final MajoraService majoraService;
    private final CommandService commandService;

    public MajoraDecision(MajoraService majoraService, CommandService commandService) {
        this.majoraService = majoraService;
        this.commandService = commandService;
    }

    public void decisionLoop(String content, String modelLLM, MajoraUserLog log, MajoraCommandEmptyLog logCommandEmpty) {
        CommandInstruction instruction = majoraService.send(content, modelLLM);

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

            Thread response = log.userLog(resultLoop);

            try {
                response.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            instruction = majoraService.send(resultLoop.toMajora(), modelLLM);
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
