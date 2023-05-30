package com.usue.weather.bot.repo;

import com.usue.weather.bot.models.TelegramClient;

import java.sql.SQLException;
import java.util.List;


public interface TelegramPersonRepo {

    List<TelegramClient> getAllClients() ;
    TelegramClient getClient(String id);
    void personRemove(TelegramClient client);
    void personSave(TelegramClient client);
}
