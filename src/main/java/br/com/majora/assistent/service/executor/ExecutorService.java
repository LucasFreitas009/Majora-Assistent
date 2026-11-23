package br.com.majora.assistent.service.executor;

import br.com.majora.assistent.model.entitys.Events;
import br.com.majora.assistent.model.entitys.Interactions;
import br.com.majora.assistent.model.request.commands.CommandInstruction;
import br.com.majora.assistent.model.request.commands.CommandResult;
import br.com.majora.assistent.service.CommandService;
import br.com.majora.assistent.service.browser.BrowserService;
import br.com.majora.assistent.service.cliServices.animations.MajoraCommandEmptyLog;
import br.com.majora.assistent.service.cliServices.animations.MajoraUserLog;
import br.com.majora.assistent.service.db.DatabaseExecuteQuery;
import br.com.majora.assistent.service.majoraClient.MajoraService;
import br.com.majora.assistent.service.memory.MajoraMemory;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class ExecutorService {

    private final BrowserService browserService;

    public ExecutorService(BrowserService browserService) {
        this.browserService = browserService;
    }

    public CommandInstruction executeQuery(CommandInstruction instruction, DatabaseExecuteQuery databaseExecuteQuery,String content, MajoraService majoraService, ObjectMapper mapper, MajoraCommandEmptyLog emptyLog, String modelLLM) {
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

    public CommandInstruction executeBrowser(String action, String content, CommandInstruction instruction, MajoraService majoraService, MajoraCommandEmptyLog emptyLog, ObjectMapper mapper, String modelLLM) {

        if(action.equalsIgnoreCase("browser_open")) {
            //System.out.println("caiu browser open 2");
           return openBrowser(content, instruction, majoraService, emptyLog, mapper, modelLLM);
        }

        if(action.equalsIgnoreCase("browser_inspect")) {
           // System.out.println("Caiu inspect");
            return inspectBrowser(content, instruction, majoraService, emptyLog, mapper, modelLLM);
        }

        if(action.equalsIgnoreCase("browser_click")) {
            //System.out.println("Caiu click");
            return clickBrowser(content, instruction, majoraService, emptyLog, mapper, modelLLM);
        }

        if(action.equalsIgnoreCase("browser_search")) {
            //System.out.println("caiu search");
            return searchBrowser(content, instruction, majoraService, emptyLog, mapper, modelLLM);
        }

        return null;
    }

    public CommandInstruction executeCommand(CommandService commandService, CommandInstruction instruction, String content, MajoraMemory majoraMemory, Interactions interaction, MajoraService majoraService, MajoraUserLog log, ObjectMapper mapper, String modelLLM) {
        CommandResult resultLoop = commandService.execute(instruction, content);

        if(majoraMemory != null && interaction != null) {
            majoraMemory.saveEvents(new Events(
                    (String) resultLoop.getData().get(0).get("command"),
                    (String) resultLoop.getData().get(0).get("stdout"),
                    (String) resultLoop.getData().get(0).get("stderr"),
                    resultLoop.isSuccess(),
                    resultLoop.getOriginal_context(),
                    LocalDateTime.now(ZoneId.of("America/Sao_Paulo")),
                    interaction
            ));
        }


        //System.out.println(mapper.writeValueAsString(resultLoop));

        Thread response = log.userLog(resultLoop, instruction.getContext());

        try {
            response.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }

        instruction = majoraService.send(mapper.writeValueAsString(resultLoop), modelLLM);

        return instruction;
    }

    public CommandInstruction verifyActionMemory(String action, String content, DatabaseExecuteQuery databaseExecuteQuery,CommandService commandService, CommandInstruction instruction, MajoraMemory majoraMemory,Interactions interaction, MajoraService majoraService, MajoraUserLog log, MajoraCommandEmptyLog emptyLog, ObjectMapper mapper, String modelLLM) {
        if(action.equalsIgnoreCase("execute_command")) {
            //System.out.println("Caiu no execute_command" + instruction.getAction() + String.join(" ", instruction.getCommand()) + instruction.getContext());
            return executeCommand(commandService, instruction, content, majoraMemory,interaction, majoraService, log, mapper, modelLLM);
        }

        if(action.equalsIgnoreCase("query_select_all")) {
            //System.out.println("Caiu no query " + instruction.getAction() + String.join(" ", instruction.getCommand()) + instruction.getContext());
            return executeQuery(instruction, databaseExecuteQuery,content, majoraService, mapper, emptyLog,modelLLM);
        }

        if(action.startsWith("browser")) {
           // System.out.println("Caiu no browser_open");
            return executeBrowser(action, content, instruction, majoraService, emptyLog, mapper, modelLLM);
        }

        return null;
    }

    public CommandInstruction verifyActionBasic(String action, String content, String modelLLM, CommandInstruction instruction,CommandService commandService, MajoraService majoraService, ObjectMapper mapper, MajoraUserLog log, MajoraCommandEmptyLog commandEmptyLog) {

        if(action.equalsIgnoreCase("execute_command")) {
           //System.out.println("Caiu no execute command ");
            return executeCommand(commandService, instruction, content, null, null, majoraService, log, mapper, modelLLM);
        }

        if(action.startsWith("browser")) {
           // System.out.println("Caiu no execute browser_open ");
            return executeBrowser(action, content, instruction, majoraService, commandEmptyLog, mapper, modelLLM);
        }

        Map<String, Object> error = new HashMap<>();
        error.put("error", "Parece que essa ação não é suportada no modo basic.");

        CommandResult result = new CommandResult(false, List.of(error), content);

        instruction = majoraService.send(mapper.writeValueAsString(result), modelLLM);

        return instruction;

    }

    public CommandInstruction openBrowser(String content, CommandInstruction instruction, MajoraService majoraService, MajoraCommandEmptyLog emptyLog, ObjectMapper mapper, String modelLLM) {
        try {
            CommandResult result = browserService.openBrowser().get();
            result.setOriginal_context(content);

            Thread response = emptyLog.isEmpty(instruction.getContext());

            try {
                response.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }


            instruction = majoraService.send(mapper.writeValueAsString(result), modelLLM);

            //CommandResult resultTest = browserService.inspectContent().get();
            //CommandResult resultTest1 = browserService.search("E4", "Youtube").get();
            //System.out.println(mapper.writeValueAsString(resultTest));
            //System.out.println(mapper.writeValueAsString(resultTest1));

            return instruction;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CommandInstruction inspectBrowser(String content, CommandInstruction instruction, MajoraService majoraService, MajoraCommandEmptyLog emptyLog, ObjectMapper mapper, String modelLLM) {
        try {
            CommandResult result = browserService.inspectContent().get();
            result.setOriginal_context(content);

            //System.out.println(mapper.writeValueAsString(result));

            Thread response = emptyLog.isEmpty(instruction.getContext());

            try {
                response.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }

            instruction = majoraService.send(mapper.writeValueAsString(result), modelLLM);


            return instruction;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CommandInstruction clickBrowser(String content, CommandInstruction instruction, MajoraService majoraService, MajoraCommandEmptyLog emptyLog, ObjectMapper mapper, String modelLLM) {
        try {
            CommandResult result = browserService.click(instruction.getCommand().get(0)).get();
            result.setOriginal_context(content);

            //System.out.println(mapper.writeValueAsString(result));

            Thread response = emptyLog.isEmpty(instruction.getContext());

            try {
                response.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }

            instruction = majoraService.send(mapper.writeValueAsString(result), modelLLM);


            return instruction;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CommandInstruction searchBrowser(String content, CommandInstruction instruction, MajoraService majoraService, MajoraCommandEmptyLog emptyLog, ObjectMapper mapper, String modelLLM) {
        try {
            String id = instruction.getCommand().get(0);
            String text = instruction.getCommand().get(1);

            CommandResult result = browserService.search(id, text).get();
            result.setOriginal_context(content);

            System.out.println(mapper.writeValueAsString(result));

            Thread response = emptyLog.isEmpty(instruction.getContext());

            try {
                response.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }

            instruction = majoraService.send(mapper.writeValueAsString(result), modelLLM);

            return instruction;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
