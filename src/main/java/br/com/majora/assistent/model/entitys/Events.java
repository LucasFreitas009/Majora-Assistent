package br.com.majora.assistent.model.entitys;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Events {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "command",
            length = 100
    )
    private String command;

    @Column(
            name = "standard_output",
            columnDefinition = "TEXT"
    )
    private String stdout;

    @Column(
            name = "output_error",
            columnDefinition = "TEXT"
    )
    private String stderr;

    @Column(
            name = "status",
            nullable = false
    )
    private Boolean success;

    @Column(
            name = "majora_response",
            nullable = false,
            length = 200
    )
    private String majoraResponse;

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
    private Interactions interaction;

    public Events(String command, String stdout, String stderr, Boolean success, String majoraResponse,LocalDateTime time, Interactions interaction) {
        this.command = command;
        this.stdout = stdout;
        this.stderr = stderr;
        this.success = success;
        this.majoraResponse = majoraResponse;
        this.time = time;
        this.interaction = interaction;
    }

    public Events() {

    }
}
