package br.com.majora.assistent.view;

import br.com.majora.assistent.service.majoraClient.MajoraService;
import br.com.majora.assistent.view.cliServices.ModelSelect;
import org.springframework.stereotype.Service;

@Service
public class MajoraCLI {
    private final ModelSelect modelSelect;
    private final MajoraService majoraService;

    public MajoraCLI(ModelSelect modelSelect, MajoraService majoraService) {
        this.modelSelect = modelSelect;
        this.majoraService = majoraService;
    }

    public String menu() {
        return modelSelect.selectModel(majoraService.getModels());
    }
}
