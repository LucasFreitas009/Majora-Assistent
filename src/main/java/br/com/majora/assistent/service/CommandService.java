package br.com.majora.assistent.service;

import br.com.majora.assistent.model.request.commands.CommandInstruction;
import br.com.majora.assistent.model.request.commands.CommandResult;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class CommandService {
    public CommandResult execute(CommandInstruction instruction) {

        List<String> command = instruction.getCommand();

        if(command != null) {

            try {
                ProcessBuilder builder = new ProcessBuilder(command);
                Process process = builder.start();

                CommandResult commandResult = new CommandResult();

                Thread threadStdout = new Thread(() -> {
                    commandResult.setStdout(read(process.getInputStream()));

                });

                Thread threadStderr = new Thread(() -> {
                    commandResult.setStderr(read(process.getErrorStream()));
                });

                threadStdout.start();
                threadStderr.start();

                int status = process.waitFor();

                threadStdout.join();
                threadStderr.join();

                commandResult.setStatus(status);

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
