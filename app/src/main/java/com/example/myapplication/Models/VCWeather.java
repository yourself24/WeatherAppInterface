package com.example.myapplication.Models;

public class VCWeather {
    private String address;
    private String description;
    private double temperature;
    private double feelsLike;

    public VCWeather() {
    }

    private double precip;
    private int humidity;
    private double pressure;

    public VCWeather(String address, String description, double temperature, double feelsLike, double precip, int humidity, double pressure) {
        this.address = address;
        this.description = description;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.precip = precip;
        this.humidity = humidity;
        this.pressure = pressure;
    }
    //getters and setters

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public double getPrecip() {
        return precip;
    }

    public void setPrecip(double precip) {
        this.precip = precip;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

}
