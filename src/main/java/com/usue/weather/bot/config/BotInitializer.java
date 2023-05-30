package com.usue.weather.bot.config;


import com.usue.weather.bot.service.TelegramBot;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotInitializer {
    private final TelegramBot bot;

    public BotInitializer(TelegramBot bot) {
        this.bot = bot;
    }


    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        try {
            api.registerBot(bot);
        } catch (TelegramApiException e) {
            // ignore
        }
    }

}
