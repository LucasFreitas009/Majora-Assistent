package br.com.majora.assistent.service.majoraClient;

import br.com.majora.assistent.model.entitys.Events;
import br.com.majora.assistent.model.entitys.Interactions;
import br.com.majora.assistent.model.request.commands.CommandInstruction;
import br.com.majora.assistent.service.CommandService;
import br.com.majora.assistent.service.cliServices.animations.MajoraCommandEmptyLog;
import br.com.majora.assistent.service.cliServices.animations.MajoraUserLog;
import br.com.majora.assistent.service.db.DatabaseExecuteQuery;
import br.com.majora.assistent.service.executor.ExecutorService;
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
    private final ExecutorService executor;
    private final DatabaseExecuteQuery databaseExecuteQuery;

    public DecisionLoopMemory(MajoraMemory majoraMemory, ExecutorService executor, DatabaseExecuteQuery databaseExecuteQuery) {
        this.majoraMemory = majoraMemory;
        this.executor = executor;
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
            instruction = executor.verifyActionMemory(instruction.getAction(), content, databaseExecuteQuery,commandService, instruction, majoraMemory, interaction, majoraService, log, logCommandEmpty,mapper, modelLLM);
        }

        Thread finalResponse = logCommandEmpty.isEmpty(instruction.getContext());

        try {
            finalResponse.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
