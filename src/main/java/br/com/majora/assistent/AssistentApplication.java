package br.com.majora.assistent;

import br.com.majora.assistent.model.request.commands.CommandInstruction;
import br.com.majora.assistent.model.request.commands.CommandResult;
import br.com.majora.assistent.service.CommandService;
import br.com.majora.assistent.service.majoraClient.MajoraDecision;
import br.com.majora.assistent.service.voice.VoiceService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class AssistentApplication implements CommandLineRunner {
	final VoiceService voiceService;
	final MajoraDecision decision;

	public AssistentApplication(VoiceService voiceService, MajoraDecision decision) {
		this.voiceService = voiceService;
		this.decision = decision;
	}

	public static void main(String[] args) {
		SpringApplication.run(AssistentApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//System.out.println(decision.decisionLoop("Crie dois arquivos para mim na pasta /home/lucas/projetos com os nomes teste1.txt e teste2.txt", "majora:latest"));

		voiceService.startListening();
	}
}
