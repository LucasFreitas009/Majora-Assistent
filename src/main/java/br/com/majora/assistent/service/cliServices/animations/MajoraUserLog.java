package br.com.majora.assistent.service.cliServices.animations;

import br.com.majora.assistent.model.request.commands.CommandResult;

@FunctionalInterface
public interface MajoraUserLog {

    Thread userLog(CommandResult commandResult);
}
