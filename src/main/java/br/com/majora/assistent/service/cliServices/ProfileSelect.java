package br.com.majora.assistent.service.cliServices;

import br.com.majora.assistent.service.cliServices.cliEnum.Profiles;
import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.util.List;

public class ProfileSelect {

    public String selectProfile() {

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

                List<String> options = List.of(Profiles.MEMORY.getMessage(), Profiles.BASIC.getMessage());

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

    public void menu(Terminal terminal, List<String> options, int selected) {
        terminal.writer().print("\033[2J\033[H");

        terminal.writer().println("""
				╔════════════════════════════╗
				║                            ║
				║     Seleção de perfil      ║
				║                            ║
				╚════════════════════════════╝
				""");

        terminal.writer().println("Selecione um perfil:");
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
