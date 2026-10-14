package br.com.majora.assistent.service.cliServices.animations;

import br.com.majora.assistent.model.request.commands.CommandResult;
import org.springframework.stereotype.Service;

@Service
public class LogAnimation implements MajoraUserLog, MajoraCommandEmptyLog {

    @Override
    public Thread userLog(CommandResult commandResult) {
        Thread log = new Thread(() -> {
            String command = String.join(" ", commandResult.getCommand());
            String context = commandResult.getContext();
            String stdout = commandResult.getStdout();
            String stderr = commandResult.getStderr();
            int status = commandResult.getStatus();

            System.out.println("=".repeat(context.length() + 11));
            System.out.println("[MAJORA]: ");
            System.out.println("=".repeat(context.length() + 11));

            System.out.print("\u001B[2A");
            System.out.print("\r[MAJORA]: ");

            for(char character : context.toCharArray()) {
                System.out.print(character);

                try {
                    Thread.sleep(30);
                }catch(InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

            }

            System.out.print("\u001B[2B");
            System.out.println();

            System.out.println("\n[COMANDO EXECUTADO]: " + command);


            System.out.println();

            System.out.println("[SAÍDA NO TERMINAL]: " + stdout);
            System.out.println("[ERROR]: " + stderr);
            System.out.println("[CÓDIGO DE STATUS]: " + status);
        });

        log.start();

        return log;
    }

    @Override
    public Thread isEmpty(String context) {
        Thread log = new Thread(() -> {

            System.out.println("=".repeat(context.length() + 11));
            System.out.println("[MAJORA]: ");
            System.out.println("=".repeat(context.length() + 11));

            System.out.print("\u001B[2A");
            System.out.print("\r[MAJORA]: ");

            for(char character : context.toCharArray()) {
                System.out.print(character);
                
                try {
                    Thread.sleep(30);
                }catch(InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            System.out.print("\u001B[2B");
            System.out.println();
        });

        log.start();

        return log;
    }
}
