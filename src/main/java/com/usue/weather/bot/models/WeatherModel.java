package com.usue.weather.bot.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherModel {
    String wind;
    String day;
    String date;
    String minT;
    String maxT;
    String precipitation;
}
