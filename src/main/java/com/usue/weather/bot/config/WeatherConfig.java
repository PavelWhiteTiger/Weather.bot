package com.usue.weather.bot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("application.properties")
public class WeatherConfig {
    @Value("${gismeteo.link}")
    private String weatherLink;
    @Value("${gismeteo.currentWeather}")
    private String currenWeatherLink;
}
