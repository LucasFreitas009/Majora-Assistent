package br.com.majora.assistent.service.cliServices.cliEnum;

public enum Profiles {
    MEMORY("memory"),
    BASIC("basic");

    private final String message;

    Profiles(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
