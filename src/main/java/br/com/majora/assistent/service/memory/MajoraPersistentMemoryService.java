package br.com.majora.assistent.service.memory;

import br.com.majora.assistent.model.entitys.Events;
import br.com.majora.assistent.model.entitys.Interactions;
import br.com.majora.assistent.repository.EventsRepository;
import br.com.majora.assistent.repository.InteractionsRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("memory")
public class MajoraPersistentMemoryService implements MajoraMemory{
    private final EventsRepository eventsRepository;
    private final InteractionsRepository interactionsRepository;

    public MajoraPersistentMemoryService(EventsRepository eventsRepository, InteractionsRepository interactionsRepository) {
        this.eventsRepository = eventsRepository;
        this.interactionsRepository = interactionsRepository;
    }

    @Override
    public void saveEvents(Events events) {
        eventsRepository.save(events);
    }

    @Override
    public void saveInteractions(Interactions interactions) {
        interactionsRepository.save(interactions);
    }


}
