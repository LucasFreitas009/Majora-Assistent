package br.com.majora.assistent.service.cliServices;

import br.com.majora.assistent.model.request.commands.CommandResult;
import br.com.majora.assistent.service.cliServices.animations.ModelSelect;
import br.com.majora.assistent.service.cliServices.animations.LogAnimation;
import br.com.majora.assistent.service.majoraClient.MajoraService;
import org.springframework.stereotype.Service;

@Service
public class MajoraCLI {
    private final ModelSelect modelSelect;
    private final LogAnimation logAnimation;
    private final MajoraService majoraService;

    public MajoraCLI(ModelSelect modelSelect, MajoraService majoraService, LogAnimation textAnimation) {
        this.modelSelect = modelSelect;
        this.logAnimation = textAnimation;
        this.majoraService = majoraService;
    }

    public String menu() {
        return modelSelect.selectModel(majoraService.getModels());
    }

    public Thread userLog(CommandResult commandResult) {
        return logAnimation.userLog(commandResult);
    }

    public Thread isEmpty(String context) {
        return logAnimation.isEmpty(context);
    }


}
