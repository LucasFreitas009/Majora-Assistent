package br.com.majora.assistent.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OllamaModels {
    private List<Models> models;

    @Getter
    @Setter
    public static class Models {
        private String name;
        private String model;
        private String modified_at;
        private long size;
        private String digest;
        private Details details;
        private List<String> capabilities;

        @Getter
        @Setter
        public static class Details {
            private String parent_model;
            private String format;
            private String family;
            private List<String> families;
            private String parameter_size;
            private String quantization_level;
            private long context_length;
            private long embedding_length;
        }
    }
}
