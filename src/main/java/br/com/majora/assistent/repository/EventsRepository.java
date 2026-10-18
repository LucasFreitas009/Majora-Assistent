package br.com.majora.assistent.repository;

import br.com.majora.assistent.model.entitys.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsRepository extends JpaRepository<Events, Long> {
}
