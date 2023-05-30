package com.usue.weather.bot.service;

public interface WeatherService {
    String getCurrentWeather(String city);
    String getWeather(String city, int daysCount);
}
