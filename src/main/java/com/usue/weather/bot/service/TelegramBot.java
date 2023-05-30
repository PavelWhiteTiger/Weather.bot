package com.usue.weather.bot.service;

import com.usue.weather.bot.config.BotConfig;
import com.usue.weather.bot.models.TelegramClient;
import com.usue.weather.bot.repo.TelegramPersonRepo;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final GismeteoService gismeteoService;
    private final TelegramPersonRepo repo;
    private final List<BotCommand> botCommands = new ArrayList<>();


    public TelegramBot(BotConfig config, GismeteoService gismeteoService, @Qualifier("personDAO2") TelegramPersonRepo repo) {
        super(config.getToken());
        this.config = config;
        this.gismeteoService = gismeteoService;
        this.repo = repo;
        botCommandsInit();
        try {
            this.execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), "ru"));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText())
            return;
        Message inputMessage = update.getMessage();
        SendMessage message = new SendMessage();
        String city = "";
        message.setChatId(inputMessage.getChatId());
        TelegramClient client = getClient(inputMessage.getChatId().toString());
        String text = switch (inputMessage.getText()) {
            case "/current_weather" -> gismeteoService.getCurrentWeather(city);
            case "/weather" -> gismeteoService.getWeather(city, 14);
            case "/subscribe_on" -> subscribe(client, true);
            case "/subscribe_off" -> subscribe(client, false);
            default -> "неизвестная команда";
        };
        message.setText(text);
        TestMethod(client.getId());
        execute(message);
    }

    private String subscribe(TelegramClient client, boolean subs) {
        client.setSubscribed(subs);
        return String.format("Вы успешено %s", subs ? "подписались" : "отписались");
    }

    private void TestMethod(String id) {
        var testClients = repo.getAllClients();
        repo.personRemove(testClients.stream().filter(x -> x.getId().equals(id)).findAny().get());
        testClients = repo.getAllClients();
    }

    private TelegramClient getClient(String id) {

        TelegramClient client = repo.getClient(id);
        if (client == null) {
            client = new TelegramClient() {{
                setId(id);
            }};
            repo.personSave(client);
        }
        return client;
    }

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    private void botCommandsInit() {
        botCommands.add(new BotCommand("/help", "Help"));
        botCommands.add(new BotCommand("/weather", "Погода на 2 недели"));
        botCommands.add(new BotCommand("/current_weather", "Текущая погода"));
        botCommands.add(new BotCommand("/subscribe_on", "Подписывает вас на все оповещения"));
        botCommands.add(new BotCommand("/subscribe_off", "Отписывает вас от всех оповещений"));
        botCommands.add(new BotCommand("/about_you", "Информация о вас"));
    }
}
