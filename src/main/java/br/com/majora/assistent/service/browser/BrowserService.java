package br.com.majora.assistent.service.browser;

import br.com.majora.assistent.model.request.commands.CommandResult;
import com.microsoft.playwright.*;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class BrowserService {
    private final ExecutorService threadExecutor = Executors.newSingleThreadExecutor();
    private Playwright playwright;
    private BrowserContext browser;
    private Page page;
    private List<Map<String, Object>> elements = new ArrayList<>();
    private final Map<String, Locator> elementLocators = new HashMap<>();

    public CompletableFuture<CommandResult> openBrowser() {

        return CompletableFuture.supplyAsync(() -> {
           try {

               if (browser != null && !browser.isClosed()) {
                   Map<String, Object> message = new HashMap<>();
                   message.put("message", "Navegador já está aberto.");

                   CommandResult result = new CommandResult();
                   result.setSuccess(true);
                   result.setData(List.of(message));

                   return result;
               }

               playwright = Playwright.create();

               Path profilePath = Paths.get(System.getProperty("user.home"), ".majora", "browser-profile");
               browser = playwright.firefox().launchPersistentContext(profilePath, new BrowserType.LaunchPersistentContextOptions().setHeadless(false));

               page = browser.pages().isEmpty() ? browser.newPage() : browser.pages().get(0);

               page.navigate("https://duckduckgo.com/?t=ffab");

               Map<String, Object> message = new HashMap<>();
               message.put("message", "Navegador Firefox aberto.");

               CommandResult result = new CommandResult();
               result.setSuccess(true);
               result.setData(List.of(message));

               return result;

           } catch (Exception e) {
               Map<String, Object> error = new HashMap<>();
               error.put("error", e.getMessage());

               CommandResult result = new CommandResult();
               result.setSuccess(false);
               result.setData(List.of(error));

               return result;
           }
        }, threadExecutor);
    }

    public CompletableFuture<CommandResult> inspectContent() {

        return CompletableFuture.supplyAsync(() -> {

            try {

                elements.clear();
                elementLocators.clear();

                Locator locator = page.locator("button, input, a, textarea, select");

                int quatity = locator.count();
                int elementIndex = 0;

                for (int i = 0; i < quatity; i++) {
                    Locator element = locator.nth(i);

                    if(!element.isVisible()) {
                        continue;
                    }

                    String tag = (String) element.evaluate("el => el.tagName.toLowerCase()");

                    String id = "E" + elementIndex++;
                    String text = element.innerText();
                    String ariaLabel = element.getAttribute("aria-label");
                    String title = element.getAttribute("title");
                    String placeholder = element.getAttribute("placeholder");
                    String href = element.getAttribute("href");

                    elementLocators.put(id, element);

                    Map<String, Object> data = new HashMap<>();


                    data.put("id", id);
                    data.put("tag", tag);
                    data.put("text", text);
                    data.put("ariaLabel", ariaLabel);
                    data.put("title", title);
                    data.put("placeholder", placeholder);
                    data.put("href", href);

                    elements.add(data);
                }

                Map<String, Object> infoPage = new HashMap<>();

                infoPage.put("url", page.url());
                infoPage.put("title", page.title());
                infoPage.put("content", page.locator("body").innerText());
                infoPage.put("elements", elements);

                CommandResult result = new CommandResult();
                result.setSuccess(true);
                result.setData(List.of(infoPage));

                return result;

            } catch (Exception e) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", e.getMessage());

                CommandResult result = new CommandResult();
                result.setSuccess(false);
                result.setData(List.of(error));

                return result;
            }

        }, threadExecutor);
    }

    public CompletableFuture<CommandResult> click(String id) {
        return CompletableFuture.supplyAsync(() -> {
           try {
               Locator element = elementLocators.get(id);

               if(element == null) {
                   Map<String, Object> error = new HashMap<>();
                   error.put("message", "Operação não ocorreu como esperado.");

                   CommandResult result = new CommandResult();
                   result.setSuccess(false);
                   result.setData(List.of(error));

                   return result;
               }

               element.click();

               Map<String, Object> message = new HashMap<>();
               message.put("message", "Ação de clicar bem sucedida.");

               CommandResult result = new CommandResult();
               result.setSuccess(true);
               result.setData(List.of(message));

               return result;
           } catch (Exception e) {
               Map<String, Object> error = new HashMap<>();
               error.put("message", e.getMessage());

               CommandResult result = new CommandResult();
               result.setSuccess(false);
               result.setData(List.of(error));

               return result;
           }
        }, threadExecutor);
    }

    public CompletableFuture<CommandResult> search(String id, String content) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Locator element = elementLocators.get(id);

                if(element == null) {
                    Map<String, Object> error = new HashMap<>();
                    error.put("message", "Esse id não existe, ou foi substituído.");

                    CommandResult result = new CommandResult();
                    result.setSuccess(false);
                    result.setData(List.of(error));

                    return result;
                }

                element.fill(content);

                Thread.sleep(1500);

                element.press("Enter");

                Map<String, Object> message = new HashMap<>();
                message.put("message", "Pesquisa realizada.");

                CommandResult result = new CommandResult();
                result.setSuccess(true);
                result.setData(List.of(message));

                return result;
            } catch (Exception e) {
                Map<String, Object> error = new HashMap<>();
                error.put("message", e.getMessage());

                CommandResult result = new CommandResult();
                result.setSuccess(false);
                result.setData(List.of(error));

                return result;
            }
        }, threadExecutor);
    }
}
