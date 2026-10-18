package br.com.majora.assistent.service.memory;

import br.com.majora.assistent.model.entitys.Events;
import br.com.majora.assistent.model.entitys.Interactions;

public interface MajoraMemory {

    void saveInteractions(Interactions interaction);

    void saveEvents(Events events);
}
