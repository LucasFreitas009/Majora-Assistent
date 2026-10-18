package br.com.majora.assistent.service;

import br.com.majora.assistent.model.request.commands.CommandInstruction;
import br.com.majora.assistent.model.request.commands.CommandResult;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommandService {
    public CommandResult execute(CommandInstruction instruction) {

        String userHome = System.getProperty("user.home");

        List<String> command = instruction.getCommand();

        if(command != null) {
            try {

                for(int i = 0; i < command.size(); i++) {
                    String arg = command.get(i);

                    if(arg.startsWith("~")) {
                        command.set(i, userHome + arg.substring(1));
                    }
                }

                ProcessBuilder builder = new ProcessBuilder(command);
                Process process = builder.start();

                CommandResult commandResult = new CommandResult();
                List<Map<String, String>> data = new ArrayList<>();
                commandResult.setData(data);

                commandResult.getData().add(new HashMap<>());

                commandResult.getData().get(0).put("command", String.join(" ", command));

                Thread threadStdout = new Thread(() -> {
                    commandResult.getData().get(0).put("stdout", read(process.getInputStream()));

                });

                Thread threadStderr = new Thread(() -> {
                    commandResult.getData().get(0).put("stderr", read(process.getErrorStream()));
                });

                threadStdout.start();
                threadStderr.start();

                int status = process.waitFor();

                threadStdout.join();
                threadStderr.join();

                if(status != 0) {
                    commandResult.setSuccess(false);
                } else {
                    commandResult.setSuccess(true);
                }

                commandResult.setOriginal_context(instruction.getContext());

                /*commandResult.setCommand(instruction.getCommand());
                commandResult.setContext(instruction.getContext());
                commandResult.setStatus(status);*/

                return commandResult;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        return null;
    }

    public String read(InputStream flow) {
        try(
                BufferedReader reader = new BufferedReader(new InputStreamReader(flow));
        ) {

            StringBuilder text = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null) {
                text.append(line).append(System.lineSeparator());
            }

            return text.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
