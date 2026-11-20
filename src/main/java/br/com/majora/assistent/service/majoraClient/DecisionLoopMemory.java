package br.com.majora.assistent.service.majoraClient;

import br.com.majora.assistent.model.entitys.Events;
import br.com.majora.assistent.model.entitys.Interactions;
import br.com.majora.assistent.model.request.commands.CommandInstruction;
import br.com.majora.assistent.model.request.commands.CommandResult;
import br.com.majora.assistent.service.CommandService;
import br.com.majora.assistent.service.cliServices.animations.MajoraCommandEmptyLog;
import br.com.majora.assistent.service.cliServices.animations.MajoraUserLog;
import br.com.majora.assistent.service.db.DatabaseExecuteQuery;
import br.com.majora.assistent.service.memory.MajoraMemory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@Profile("memory")
public class DecisionLoopMemory implements DecisionLoop{

    private final MajoraMemory majoraMemory;
    private final DatabaseExecuteQuery databaseExecuteQuery;

    public DecisionLoopMemory(MajoraMemory majoraMemory, DatabaseExecuteQuery databaseExecuteQuery) {
        this.majoraMemory = majoraMemory;
        this.databaseExecuteQuery = databaseExecuteQuery;
    }

    @Override
    public void decisionLoop(CommandInstruction instruction, String content, String modelLLM, CommandService commandService, MajoraService majoraService, ObjectMapper mapper, MajoraUserLog log, MajoraCommandEmptyLog logCommandEmpty) {
        Interactions interaction = new Interactions();


        if(instruction.getAction().equalsIgnoreCase("noAction")) {
            Thread response = logCommandEmpty.isEmpty(instruction.getContext());

            try {
                response.join();

                interaction.setUserContext(content);
                interaction.setTime(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));

                majoraMemory.saveInteractions(interaction);
                majoraMemory.saveEvents(new Events("Nenhum comando executado.", null, null, true, instruction.getContext(),LocalDateTime.now(ZoneId.of("America/Sao_Paulo")),interaction));

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            return;
        }

        interaction.setUserContext(content);
        interaction.setTime(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));

        majoraMemory.saveInteractions(interaction);

        while(!instruction.getCommand().isEmpty()) {
            instruction = verifyAction(instruction.getAction(), content,commandService, instruction, interaction, majoraService, log, logCommandEmpty,mapper, modelLLM);
        }

        Thread finalResponse = logCommandEmpty.isEmpty(instruction.getContext());

        try {
            finalResponse.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    //Vai precisar criar uma função nova para gerenciar o banco de dados
    public CommandInstruction exuteCommand(CommandService commandService, CommandInstruction instruction, String content,Interactions interaction, MajoraService majoraService, MajoraUserLog log, ObjectMapper mapper, String modelLLM) {
        CommandResult resultLoop = commandService.execute(instruction, content);

        majoraMemory.saveEvents(new Events(
                (String) resultLoop.getData().get(0).get("command"),
                (String) resultLoop.getData().get(0).get("stdout"),
                (String) resultLoop.getData().get(0).get("stderr"),
                resultLoop.isSuccess(),
                resultLoop.getOriginal_context(),
                LocalDateTime.now(ZoneId.of("America/Sao_Paulo")),
                interaction
        ));


        //System.out.println(mapper.writeValueAsString(resultLoop));

        Thread response = log.userLog(resultLoop);

        try {
            response.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }

        instruction = majoraService.send(mapper.writeValueAsString(resultLoop), modelLLM);

        return instruction;
    }

    public CommandInstruction executeQuery(CommandInstruction instruction, String content, MajoraService majoraService, ObjectMapper mapper, MajoraCommandEmptyLog emptyLog, String modelLLM) {
        CommandResult resultLoop = databaseExecuteQuery.getEventAndInteraction(instruction.getCommand());
        resultLoop.setOriginal_context(content);
        System.out.println(mapper.writeValueAsString(resultLoop));

        Thread response = emptyLog.isEmpty(instruction.getContext());

        try{
            response.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }

        instruction = majoraService.send(mapper.writeValueAsString(resultLoop), modelLLM);

        return instruction;
    }

    public CommandInstruction verifyAction(String action, String content ,CommandService commandService, CommandInstruction instruction, Interactions interaction, MajoraService majoraService, MajoraUserLog log, MajoraCommandEmptyLog emptyLog,ObjectMapper mapper, String modelLLM) {
        if(action.equalsIgnoreCase("execute_command")) {
            System.out.println("Caiu no execute_command" + instruction.getAction() + String.join(" ", instruction.getCommand()) + instruction.getContext());
           return exuteCommand(commandService, instruction, content, interaction, majoraService, log, mapper, modelLLM);
        }

        if(action.equalsIgnoreCase("query_select_all")) {
            System.out.println("Caiu no query " + instruction.getAction() + String.join(" ", instruction.getCommand()) + instruction.getContext());
            return executeQuery(instruction, content, majoraService, mapper, emptyLog,modelLLM);
        }

        return null;
    }
}
