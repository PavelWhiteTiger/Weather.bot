package com.usue.weather.bot.repo;

import com.usue.weather.bot.models.TelegramClient;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TelegramPersonRepoImpl implements TelegramPersonRepo {

    final Map<String, TelegramClient> allPersons = new TreeMap<>(Comparator.comparingInt(String::hashCode));

    @Override
    public List<TelegramClient> getAllClients() {
        return allPersons.values().stream().toList();
    }

    @Override
    public TelegramClient getClient(String id) {
        return allPersons.get(id);
    }

    @Override
    public void personRemove(TelegramClient client) {
        allPersons.remove(client);

    }

    @Override
    public void personSave(TelegramClient client) {
        allPersons.put(client.getId(), client);

    }


}
