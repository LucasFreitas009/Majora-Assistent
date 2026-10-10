package br.com.majora.assistent.model.request.commands;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommandInstruction {
    private List<String> command;
    private String context;
}
