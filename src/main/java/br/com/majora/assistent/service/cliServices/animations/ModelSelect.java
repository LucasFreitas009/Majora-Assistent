package br.com.majora.assistent.service.cliServices.animations;

import br.com.majora.assistent.model.response.OllamaModels;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ModelSelect {
    public String selectModel(List<OllamaModels.Models> models) {
        try {
             Terminal terminal = TerminalBuilder.builder()
                    .system(true)
                    .build();

             terminal.enterRawMode();

            List<String> options = new ArrayList<>();

            for(OllamaModels.Models model : models) {
                options.add(model.getName());
            }

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
            terminal.writer().println(options.get(selected) + " selecionado.\n");

            terminal.writer().flush();

            terminal.close();
            return options.get(selected);

        } catch(IOException e) {
            e.printStackTrace();

        }

        return null;
    }

    private void menu(Terminal terminal, List<String> options, int selected) {


        terminal.writer().print("\033[2J\033[H");

        terminal.writer().println("""
				╔════════════════════════════╗
				║                            ║
				║     Seleção de modelo      ║
				║                            ║
				╚════════════════════════════╝
				""");

        terminal.writer().println("Selecione um modelo:");
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
