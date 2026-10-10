package br.com.majora.assistent.model.request.commands;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommandResult {
    private String stdout;
    private String stderr;
    private int status;
}
