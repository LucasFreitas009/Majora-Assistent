package br.com.majora.assistent.service.majoraClient;


import br.com.majora.assistent.model.request.commands.CommandInstruction;
import br.com.majora.assistent.service.CommandService;
import br.com.majora.assistent.service.cliServices.animations.MajoraCommandEmptyLog;
import br.com.majora.assistent.service.cliServices.animations.MajoraUserLog;
import tools.jackson.databind.ObjectMapper;

public interface DecisionLoop {

    void decisionLoop(CommandInstruction instruction, String content, String modelLLM, CommandService commandService, MajoraService majoraService, ObjectMapper mapper, MajoraUserLog log, MajoraCommandEmptyLog logCommandEmpty);

}
