package br.com.majora.assistent.service.memory;

import br.com.majora.assistent.model.entitys.Events;
import br.com.majora.assistent.model.entitys.Interactions;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("basic")
public class MajoraNoPersistentMemoryService implements MajoraMemory{

    @Override
    public void saveEvents(Events events) {

    }

    @Override
    public void saveInteractions(Interactions interactions) {

    }
}
