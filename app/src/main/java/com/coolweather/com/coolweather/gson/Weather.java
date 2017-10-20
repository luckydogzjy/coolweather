package com.coolweather.com.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 非皇 on 2017/10/6.
 */
//需要一个实体类来引用刚刚创建的各个实体类
public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;

    //因为daily_forecast是数组，要用List集合引用Forecast
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
