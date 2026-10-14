package br.com.majora.assistent.model.entitys;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Interactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "user_context",
            nullable = false,
            length = 100
    )
    private String userContext;

    @Column(
            name = "majora_response",
            nullable = false,
            length = 100
    )
    private String majoraResponse;

    @Column(
            name = "date_time",
            nullable = false
    )
    private LocalDateTime time;
}
