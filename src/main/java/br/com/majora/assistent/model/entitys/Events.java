package br.com.majora.assistent.model.entitys;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Events {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "command",
            nullable = false,
            length = 100
    )
    private String command;

    @Column(
            name = "standard_output",
            nullable = false,
            length = 200
    )
    private String stdout;

    @Column(
            name = "output_error",
            nullable = false,
            length = 200
    )
    private String stderr;

    @Column(
            name = "status",
            nullable = false
    )
    private Integer status;

    @Column(
            name = "date_time",
            nullable = false
    )
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(
            name = "interaction_id",
            nullable = false
    )
    private Interactions interactions;
}
