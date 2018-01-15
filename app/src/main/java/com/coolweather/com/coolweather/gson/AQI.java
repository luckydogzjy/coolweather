package com.coolweather.com.coolweather.gson;

/**
 * Created by 非皇 on 2018/1/13.
 */
/*"aqi": { //空气质量，仅限国内城市
        "city": {
        "aqi": "30",  //空气质量指数
        "pm25": "7",  //PM2.5 1小时平均值(ug/m³)
        }*/
public class AQI {
    public AQICity city;

    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
