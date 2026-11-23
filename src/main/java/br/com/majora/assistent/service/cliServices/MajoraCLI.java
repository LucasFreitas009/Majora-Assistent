package br.com.majora.assistent.service.cliServices;

import br.com.majora.assistent.model.request.commands.CommandResult;
import br.com.majora.assistent.service.cliServices.animations.ChatOrVoiceSelect;
import br.com.majora.assistent.service.cliServices.animations.ModelSelect;
import br.com.majora.assistent.service.cliServices.animations.LogAnimation;
import br.com.majora.assistent.service.majoraClient.MajoraService;
import org.springframework.stereotype.Service;

@Service
public class MajoraCLI {
    private final ModelSelect modelSelect;
    private final LogAnimation logAnimation;
    private final MajoraService majoraService;
    private final ChatOrVoiceSelect chatOrVoiceSelect;

    public MajoraCLI(ModelSelect modelSelect, ChatOrVoiceSelect chatOrVoiceSelect, MajoraService majoraService, LogAnimation textAnimation) {
        this.modelSelect = modelSelect;
        this.chatOrVoiceSelect = chatOrVoiceSelect;
        this.logAnimation = textAnimation;
        this.majoraService = majoraService;
    }

    public String menu() {
        return modelSelect.selectModel(majoraService.getModels());
    }

    public boolean chatOrVoice() {
        return chatOrVoiceSelect.selectChatOrVoice();
    }

    public Thread userLog(CommandResult commandResult, String context) {
        return logAnimation.userLog(commandResult, context);
    }

    public Thread isEmpty(String context) {
        return logAnimation.isEmpty(context);
    }


}
