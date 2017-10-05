package com.coolweather.com.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 非皇 on 2017/10/4.
 */
public class Province extends DataSupport {
    //LitePal中的每一个实体类都要继承DataSupport
    private  int id;//每个实体类都有的字段
    private String provinceName;//省的名字
    private int provinceCode;//省的代号

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
