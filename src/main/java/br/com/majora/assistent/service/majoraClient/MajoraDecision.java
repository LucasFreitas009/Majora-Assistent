package br.com.majora.assistent.service.majoraClient;

import br.com.majora.assistent.model.entitys.Events;
import br.com.majora.assistent.model.entitys.Interactions;
import br.com.majora.assistent.model.request.commands.CommandInstruction;
import br.com.majora.assistent.model.request.commands.CommandResult;
import br.com.majora.assistent.service.CommandService;
import br.com.majora.assistent.service.cliServices.animations.MajoraCommandEmptyLog;
import br.com.majora.assistent.service.cliServices.animations.MajoraUserLog;
import br.com.majora.assistent.service.memory.MajoraMemory;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class MajoraDecision {
    private final MajoraService majoraService;
    private final CommandService commandService;
    private final ObjectMapper mapper;
    private final DecisionLoop decisionLoop;

    public MajoraDecision(MajoraService majoraService, CommandService commandService, ObjectMapper mapper, DecisionLoop decisionLoop) {
        this.majoraService = majoraService;
        this.commandService = commandService;
        this.mapper = mapper;
        this.decisionLoop = decisionLoop;
    }

    public void decision(String content, String modelLLM, MajoraUserLog log, MajoraCommandEmptyLog logCommandEmpty) {
        CommandInstruction instruction = majoraService.send(content, modelLLM);

        decisionLoop.decisionLoop(instruction, content, modelLLM, commandService, majoraService, mapper, log, logCommandEmpty);
    }

    private boolean verifyAction(String action) {
        if(action.equalsIgnoreCase("execute_command")) {
            return true;

        } else if(action.equalsIgnoreCase("database_query")) {
            return false;

        } else {
            return true;
        }
    }
}
