package br.com.majora.assistent.service.cliServices.animations;

import br.com.majora.assistent.model.response.OllamaModels;
import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ModelSelect {

    public String selectModel(List<OllamaModels.Models> models) {

        List<String> options = new ArrayList<>();

        for (OllamaModels.Models model : models) {
            options.add(model.getName());
        }

        if (options.isEmpty()) {
            return null;
        }

        try (Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build()) {

            Attributes originalAttributes = terminal.enterRawMode();

            try {

                terminal.puts(InfoCmp.Capability.keypad_xmit);
                terminal.flush();

                BindingReader bindingReader = new BindingReader(terminal.reader());

                KeyMap<String> keyMap = new KeyMap<>();

                keyMap.bind("UP", KeyMap.key(terminal, InfoCmp.Capability.key_up));

                keyMap.bind("DOWN", KeyMap.key(terminal, InfoCmp.Capability.key_down));

                keyMap.bind("ENTER", "\r", "\n");


                keyMap.setNomatch("OTHER");
                keyMap.setUnicode("OTHER");

                int selected = 0;

                while (true) {

                    menu(terminal, options, selected);

                    String key = bindingReader.readBinding(keyMap);

                    if (key == null) {
                        return null;
                    }

                    switch (key) {

                        case "UP":
                            if (selected > 0) {
                                selected--;
                            }
                            break;

                        case "DOWN":
                            if (selected < options.size() - 1) {
                                selected++;
                            }
                            break;

                        case "ENTER":
                            terminal.writer().println();
                            terminal.writer().println(
                                    options.get(selected) + " selecionado.\n"
                            );

                            terminal.flush();

                            return options.get(selected);
                    }
                }

            } finally {

                terminal.setAttributes(originalAttributes);

                terminal.puts(InfoCmp.Capability.keypad_local);
                terminal.flush();
            }

        } catch (IOException e) {
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