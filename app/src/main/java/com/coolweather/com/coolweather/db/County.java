package com.coolweather.com.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 非皇 on 2018/1/13.
 */
public class County extends DataSupport {
    private int id;
    private String countyName;//县的名字
    private String weatherId;//县所对应的天气id
    private int cityId;//当前所属市的id

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCityId() {
        return cityId;
    }
}
