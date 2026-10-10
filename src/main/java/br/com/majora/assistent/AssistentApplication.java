package br.com.majora.assistent;

import br.com.majora.assistent.model.request.commands.CommandInstruction;
import br.com.majora.assistent.model.request.commands.CommandResult;
import br.com.majora.assistent.service.CommandService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class AssistentApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(AssistentApplication.class, args);
	}

	@Override
	public void run(String... args) {
		CommandInstruction instruction = new CommandInstruction();
		instruction.setCommand(List.of("mkdir", "-abcd"));

		CommandService commandService = new CommandService();

		CommandResult result = commandService.execute(instruction);

		System.out.println("Stdout: " + result.getStdout());
		System.out.println("Stderr: " + result.getStderr());
		System.out.println("Status: " + result.getStatus());
	}
}
