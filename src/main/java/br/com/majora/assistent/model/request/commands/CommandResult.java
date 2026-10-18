package br.com.majora.assistent.model.request.commands;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CommandResult {
    private boolean success;
    private List<Map<String, String>> data;
    private String original_context;

    /* private List<String> command;
    private String context;
    private String stdout;
    private String stderr;
    private int status;*/
}
