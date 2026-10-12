package br.com.majora.assistent.model.request.commands;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommandResult {
    private List<String> command;
    private String context;
    private String stdout;
    private String stderr;
    private int status;


    public String toMajora() {
        return String.format("""
                [STDOUT]: %s
                [STDERR]: %s
                [STATUS]: %d
                """,stdout, stderr, status);
    }
}
