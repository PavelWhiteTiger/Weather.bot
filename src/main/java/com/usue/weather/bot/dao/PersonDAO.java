package com.usue.weather.bot.dao;

import com.usue.weather.bot.DBConnection;
import com.usue.weather.bot.models.TelegramClient;
import com.usue.weather.bot.repo.TelegramPersonRepo;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO implements TelegramPersonRepo {
    @SneakyThrows
    @Override
    public List<TelegramClient> getAllClients() {
        var persons = new ArrayList<TelegramClient>();
        var statement = getStatement();
        String sql = "Select * from Person";
        ResultSet set = statement.executeQuery(sql);
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
        var statement = getStatement();
        String sql = String.format("Select * from person where id = '%s'", id);
        ResultSet set = statement.executeQuery(sql);
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
        var statement = getStatement();
        String sql = String.format("INSERT INTO person VALUES ('%s', '%s')", client.getId(), client.isSubscribed());
        statement.execute(sql);
    }

    @SneakyThrows
    @Override
    public void personRemove(TelegramClient client) {
        var statement = getStatement();
        String sql = String.format("Delete from person where id = '%s'", client.getId());
        statement.execute(sql);
    }

    private Statement getStatement() {
        try {
            return DBConnection.getCONNECTION().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
