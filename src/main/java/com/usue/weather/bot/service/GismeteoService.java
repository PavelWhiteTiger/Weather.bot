package com.usue.weather.bot.service;

import com.usue.weather.bot.config.WeatherConfig;
import com.usue.weather.bot.models.WeatherModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;


@Service
public class GismeteoService implements WeatherService {
    private WeatherConfig config;

    public GismeteoService(WeatherConfig config) {
        this.config = config;
    }

    @Override
    public String getCurrentWeather(String city) {
        Document doc;
        try {
            doc = getHtmlDoc(config.getCurrenWeatherLink());
        } catch (IOException e) {
            throw new RuntimeException("Error getting weather");
        }
        var el = doc.select("div[class=now-weather]").first();
        var tmp = el.select("span[class=unit unit_temperature_c]").first().text();
        var wind = doc.select("div[class=unit unit_wind_m_s]").first().text();
        var pressure = doc.select("div[class=unit unit_pressure_mm_hg_atm]").first().text();
        var humidity = doc.select("div[class=now-info-item humidity]").first().select("div[class=item-value]").text();
        return String.format("""
                Текущая погода: %s
                Скорость ветра: %s
                Давление: %s
                Влажность: %s%%""", tmp, wind, pressure,humidity);
    }

    private Document getHtmlDoc(String link) throws IOException {
        return Jsoup.parse(URI.create(link).toURL(), 3000);
    }

    @Override
    public String getWeather(String city, int daysCount) {
        Document doc;
        try {
            doc = getHtmlDoc(config.getWeatherLink());
        } catch (IOException e) {
            throw new RuntimeException("Error getting weather");
        }
        var el = doc.select("div[class=widget-items]").first();
        var days = el.select("div[class=day]").stream().map(Element::text).toList();
        var maxT = el.select("div[class=maxT]").select("span[class=unit unit_temperature_c]").stream().map(Element::text).toList();
        var minT = el.select("div[class=minT]").select("span[class=unit unit_temperature_c]").stream().map(Element::text).toList();
        var dates = el.select("div[class=date]").stream().map(Element::text).toList();
        var wind = el.select("span[class=wind-unit unit unit_wind_m_s]").stream().map(Element::text).toList();
        var precipitation = el.select("div[class=item-unit],div[class=item-unit unit-blue]").stream().map(Element::text).toList();
        var weatherList = new ArrayList<WeatherModel>();
        if (daysCount < 1 || daysCount > 14) {
            return "Введите значение в диапазоне от 1 до 14 дней";
        }
        for (int i = 0; i < daysCount; i++) {
            weatherList.add(new WeatherModel(wind.get(i), days.get(i), dates.get(i), minT.get(i), maxT.get(i), precipitation.get(i)));
        }
        StringBuilder textResult = new StringBuilder();
        for (WeatherModel model : weatherList) {
            textResult.append(String.format("%s %s Погода: от %s до %s градусов по цельсию. Ветер %sмс. Осадки %sмм\n", model.getDay(), model.getDate(), model.getMinT(), model.getMaxT(), model.getWind(), model.getPrecipitation()));
        }
        return textResult.toString();
    }
}
