package br.com.majora.assistent.model.response;

import br.com.majora.assistent.model.request.Messages;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MajoraResponse {
    private String model;
    private String created_at;

    @JsonProperty("message")
    private Messages message;
    private boolean done;
    private String done_reason;
    private long total_duration;
    private long load_duration;
    private int prompt_eval_count;
    private long prompt_eval_duration;
    private int eval_count;
    private long eval_duration;
}
