package br.com.majora.assistent.model.request.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommandResult {
    private boolean success;
    private List<Map<String, Object>> data;
    private String original_context;

    /* private List<String> command;
    private String context;
    private String stdout;
    private String stderr;
    private int status;*/
}
