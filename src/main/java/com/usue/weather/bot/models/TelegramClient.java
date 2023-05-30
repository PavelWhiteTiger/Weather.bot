package com.usue.weather.bot.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

public class TelegramClient {

    @Getter
    @Setter
    String id;

    @Getter
    @Setter
    boolean isSubscribed;

}
