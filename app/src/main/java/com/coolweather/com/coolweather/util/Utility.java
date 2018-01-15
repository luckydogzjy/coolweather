package com.coolweather.com.coolweather.util;

import android.text.TextUtils;

import com.coolweather.com.coolweather.db.City;
import com.coolweather.com.coolweather.db.County;
import com.coolweather.com.coolweather.db.Province;
import com.coolweather.com.coolweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 非皇 on 2018/1/15.
 */
public class Utility {
    //解析和处理服务器返回的省级数据
    //json={"id":1,"name":"北京"}
    public static boolean handleProvinceResponse(String response){
        //判断服务器返回的数据是否为空
        if (!TextUtils.isEmpty(response)){
            try {
                //先使用JSONArray和JSONObject解析数据
                //即接受返回的数据用数组装起来
                JSONArray allProvinces = new JSONArray(response);
                for (int i=0; i<allProvinces.length(); i++){
                    //再逐个拆解
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    //然后组装成实体类对象
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    //在调用save（）方法将数据存储到数据库里
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //解析和处理服务器返回的市级数据
    public static boolean handleCityResponse(String response, int provinceId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i=0; i<allCities.length(); i++){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //解析和处理服务器返回的县级数据
    //json={"id":937,"name":"苏州","weather_id":"CN10110401"}
    public static boolean handleCountyResponse(String response, int cityId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i=0; i<allCounties.length(); i++){
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //将返回的JSON数据解析成Weather实体
    public static Weather handleWeatherResponse(String response){
        try {
            //将其拆分成{"HeWeather": [{}]}看待
            JSONObject jsonObject = new JSONObject(response);
            //按和风天气的摆放方式解析
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            //拆分成{“HeWeather”: [{}]}看待，weatherContent就等价于下面的内容，也就是数组中第0个元素值。
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
