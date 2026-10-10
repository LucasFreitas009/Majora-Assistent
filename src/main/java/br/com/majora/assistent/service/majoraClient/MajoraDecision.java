package br.com.majora.assistent.service.majoraClient;

import br.com.majora.assistent.model.request.commands.CommandInstruction;
import br.com.majora.assistent.model.request.commands.CommandResult;
import br.com.majora.assistent.service.CommandService;
import tools.jackson.databind.ObjectMapper;

public class MajoraDecision {
    private final MajoraService majoraService;
    private final CommandService commandService;
    private final ObjectMapper mapper;

    public MajoraDecision(MajoraService majoraService, CommandService commandService, ObjectMapper mapper) {
        this.majoraService = majoraService;
        this.commandService = commandService;
        this.mapper = mapper;
    }

    public String decisionLoop(String content, String modelLLM) {
        CommandInstruction instruction = majoraService.send(content, modelLLM);


        while(!instruction.getCommand().isEmpty()) {
            CommandResult resultLoop = commandService.execute(instruction);
            instruction = majoraService.send(resultLoop.toString(), modelLLM);

            System.out.println("LOG LOOP DE ENVIO");
            System.out.println(resultLoop.getStdout());
            System.out.println(instruction.getCommand());
            System.out.println(instruction.getContext());

        }

        return "Encerrado...";
    }
}
