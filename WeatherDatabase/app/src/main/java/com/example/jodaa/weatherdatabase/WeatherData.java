/**
 * Created by Joachim on 05-05-2017.
 */

package com.example.jodaa.weatherdatabase;

public class WeatherData {

    public Coordinates gpsCoordinates;
    public Weather weather;
    public String base;
    public Main main;
    public Wind wind;
    public Clouds clouds;
    public Rain rain;
    public long dt;
    public Sys sys;
    public long id;
    public String name;
    public int cod;


}

class Coordinates{
    public long longtitude;
    public long latitude;
}

class Weather{
    public int id;
    public String main;
    public String description;
    public String icon;
}


class Main{
    public long temperature;
    public long pressure;
    public long humidity;
    public long tempMinimum;
    public long tempMaximum;
}

class Wind{
    public long speed;
    public long degree;
}

class Clouds{
    //Cloudiness in percent
    public int all;
}

class Rain{
    //rain volume for last 3 hours
    public int threeHours;
}

class Sys{
    public int type;
    public int id;
    public String message;
    public String country;
    public long sunrise;
    public long sunset;
}