package br.com.majora.assistent.service.cliServices.animations;

import br.com.majora.assistent.service.cliServices.cliEnum.Profiles;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ChatOrVoiceSelect {

    public boolean selectChatOrVoice() {
        try {
            Terminal terminal = TerminalBuilder.builder()
                    .system(true)
                    .build();

            terminal.enterRawMode();

            List<String> options = List.of("Chat", "Voice");

            int selected = 0;

            while (true) {

                menu(terminal, options, selected);

                int key = terminal.reader().read();

                // Enter
                if (key == '\n' || key == '\r') {
                    break;
                }

                // ESC
                if (key == 27) {

                    int second = terminal.reader().read();

                    if (second == '[') {

                        int third = terminal.reader().read();

                        // seta pra cima
                        if (third == 'A' && selected > 0) {
                            selected--;
                        }

                        // seta pra baixo
                        if (third == 'B' && selected < options.size() - 1) {
                            selected++;
                        }
                    }
                }
            }

            terminal.writer().println();
            terminal.writer().println("Versão " + options.get(selected) + " selecionado.\n");

            terminal.writer().flush();

            terminal.close();

            return options.get(selected).equalsIgnoreCase("Chat");

        } catch(IOException e) {
            e.printStackTrace();

        }

        return true;
    }

    public void menu(Terminal terminal, List<String> options, int selected) {
        terminal.writer().print("\033[2J\033[H");

        terminal.writer().println("""
				╔════════════════════════════╗
				║                            ║
				║     Seleção de Versão      ║
				║                            ║
				╚════════════════════════════╝
				""");

        terminal.writer().println("Selecione uma versão:");
        terminal.writer().println();

        for (int i = 0; i < options.size(); i++) {

            if (i == selected) {
                terminal.writer().println("> " + options.get(i));
            } else {
                terminal.writer().println("  " + options.get(i));
            }
        }

        terminal.writer().println();
        terminal.writer().println("↑ ↓ navegar | Enter selecionar");

        terminal.writer().flush();
    }
}
