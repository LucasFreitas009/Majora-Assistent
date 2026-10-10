package br.com.majora.assistent.model.request.commands;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommandResult {
    private String stdout;
    private String stderr;
    private int status;

    @Override
    public String toString() {
        return String.format("""
                [STDOUT]: %s
                [STDERR]: %s
                [STATUS]: %d
                """,stdout, stderr, status);
    }
}
