package br.com.majora.assistent.service.majoraClient;

import br.com.majora.assistent.model.entitys.Events;
import br.com.majora.assistent.model.entitys.Interactions;
import br.com.majora.assistent.model.request.commands.CommandInstruction;
import br.com.majora.assistent.model.request.commands.CommandResult;
import br.com.majora.assistent.service.CommandService;
import br.com.majora.assistent.service.cliServices.animations.MajoraCommandEmptyLog;
import br.com.majora.assistent.service.cliServices.animations.MajoraUserLog;
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

    public DecisionLoopMemory(MajoraMemory majoraMemory) {
        this.majoraMemory = majoraMemory;
    }

    @Override
    public void decisionLoop(CommandInstruction instruction, String content, String modelLLM, CommandService commandService, MajoraService majoraService, ObjectMapper mapper, MajoraUserLog log, MajoraCommandEmptyLog logCommandEmpty) {
        Interactions interaction = new Interactions();


        if(instruction.getCommand().isEmpty()) {
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
            CommandResult resultLoop = commandService.execute(instruction);

            majoraMemory.saveEvents(new Events(
                    resultLoop.getData().get(0).get("command"),
                    resultLoop.getData().get(0).get("stdout"),
                    resultLoop.getData().get(0).get("stderr"),
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
                return;
            }

            instruction = majoraService.send(mapper.writeValueAsString(resultLoop), modelLLM);
        }

        Thread finalResponse = logCommandEmpty.isEmpty(instruction.getContext());

        try {
            finalResponse.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
