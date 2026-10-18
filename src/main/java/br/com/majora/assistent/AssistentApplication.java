package br.com.majora.assistent;

import br.com.majora.assistent.service.cliServices.ProfileSelect;
import br.com.majora.assistent.service.voice.VoiceService;
import br.com.majora.assistent.service.cliServices.MajoraCLI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AssistentApplication implements CommandLineRunner {
	final VoiceService voiceService;
	final MajoraCLI cli;

	public AssistentApplication(VoiceService voiceService, MajoraCLI cli) {
		this.voiceService = voiceService;
		this.cli = cli;
	}

	public static void main(String[] args) {
		ProfileSelect start = new ProfileSelect();
		String profile = start.selectProfile();

		SpringApplication application = new SpringApplication(AssistentApplication.class);
		application.setAdditionalProfiles(profile);

		application.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		//System.out.println(decision.decisionLoop("Crie dois arquivos para mim na pasta /home/lucas/projetos com os nomes teste1.txt e teste2.txt", "majora:latest"));

		String model = cli.menu();

		voiceService.startListening(model, cli::userLog, cli::isEmpty);
	}
}
