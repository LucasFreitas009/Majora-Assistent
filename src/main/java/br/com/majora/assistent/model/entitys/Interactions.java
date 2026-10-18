package br.com.majora.assistent.model.entitys;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Interactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "user_context",
            nullable = false,
            length = 300
    )
    private String userContext;

    @Column(
            name = "date_time",
            nullable = false
    )
    private LocalDateTime time;

    public Interactions(String userContext, LocalDateTime time) {
        this.userContext = userContext;
        this.time = time;
    }

    public Interactions() {

    }
}
