package br.com.majora.assistent.service.majoraClient;

import br.com.majora.assistent.model.request.commands.CommandInstruction;
import br.com.majora.assistent.model.request.commands.CommandResult;
import br.com.majora.assistent.service.CommandService;
import br.com.majora.assistent.service.cliServices.animations.MajoraCommandEmptyLog;
import br.com.majora.assistent.service.cliServices.animations.MajoraUserLog;
import br.com.majora.assistent.service.db.DatabaseExecuteQuery;
import br.com.majora.assistent.service.executor.ExecutorService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Profile("basic")
public class DecisionLoopBasic implements DecisionLoop{
    private final ExecutorService executor;

    public DecisionLoopBasic(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void decisionLoop(CommandInstruction instruction, String content, String modelLLM, CommandService commandService, MajoraService majoraService, ObjectMapper mapper, MajoraUserLog log, MajoraCommandEmptyLog logCommandEmpty) {
        if(instruction.getAction().equalsIgnoreCase("noAction")) {
            Thread response = logCommandEmpty.isEmpty(instruction.getContext());

            try {
                response.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            return;
        }

        //System.out.println(instruction.getAction());

        while(!instruction.getAction().equalsIgnoreCase("noAction")) {
            System.out.println(instruction.getAction());
            instruction = executor.verifyActionBasic(instruction.getAction(), content, modelLLM, instruction, commandService, majoraService, mapper, log, logCommandEmpty);
        }

        Thread finalResponse = logCommandEmpty.isEmpty(instruction.getContext());

        try {
            finalResponse.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
