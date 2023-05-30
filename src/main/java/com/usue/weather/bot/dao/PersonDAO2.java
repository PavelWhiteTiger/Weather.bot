package com.usue.weather.bot.dao;

import com.usue.weather.bot.DBConnection;
import com.usue.weather.bot.models.TelegramClient;
import com.usue.weather.bot.repo.TelegramPersonRepo;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO2 implements TelegramPersonRepo {
    @SneakyThrows
    @Override
    public List<TelegramClient> getAllClients() {
        var persons = new ArrayList<TelegramClient>();
        String sql = "Select * from person";
        PreparedStatement statement = DBConnection.getCONNECTION().prepareStatement(sql);
        ResultSet set = statement.executeQuery();
        while (set.next()) {
            TelegramClient client = new TelegramClient();
            client.setId(set.getObject(1, String.class));
            client.setSubscribed(set.getObject(2, Boolean.class));
            persons.add(client);
        }
        return persons;
    }

    @SneakyThrows
    @Override
    public TelegramClient getClient(String id) {
        var persons = new ArrayList<TelegramClient>();
        String sql = "Select * from person where id = ?";
        PreparedStatement statement = DBConnection.getCONNECTION().prepareStatement(sql);
        statement.setString(1, id);
        ResultSet set = statement.executeQuery();
        while (set.next()) {
            TelegramClient client = new TelegramClient();
            client.setId(set.getObject(1, String.class));
            client.setSubscribed(set.getObject(2, Boolean.class));
            persons.add(client);
        }
        return persons.stream().findAny().orElse(null);
    }

    @SneakyThrows
    @Override
    public void personSave(TelegramClient client) {
        String sql = "INSERT INTO person VALUES (?, ?)";
        PreparedStatement statement = DBConnection.getCONNECTION().prepareStatement(sql);
        statement.setString(1, client.getId());
        statement.setBoolean(2, client.isSubscribed());
        statement.execute();
    }

    @SneakyThrows
    @Override
    public void personRemove(TelegramClient client) {
        String sql = "Delete from person where id = ?";
        PreparedStatement statement = DBConnection.getCONNECTION().prepareStatement(sql);
        statement.setString(1, client.getId());
        statement.execute();
    }

}
