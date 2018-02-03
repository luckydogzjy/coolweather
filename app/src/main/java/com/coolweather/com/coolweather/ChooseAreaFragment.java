package com.coolweather.com.coolweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.com.coolweather.db.City;
import com.coolweather.com.coolweather.db.County;
import com.coolweather.com.coolweather.db.Province;
import com.coolweather.com.coolweather.util.HttpUtil;
import com.coolweather.com.coolweather.util.Utility;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 非皇 on 2018/1/13.
 */
/*用于遍历省市县数据的碎片*/
public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    //框架所提供的适配器
    public AlphaInAnimationAdapter animationAdapter;
    //ListView所需部件
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    //省列表
    private List<Province> provinceList;
    //市列表
    private List<City> cityList;
    //县列表
    private List<County>countyList;
    //选中的省份
    private Province selectedProvince;
    //选中的城市
    private  City selectedCity;
    //当前选中的级别
    private int currentLevel;

    //初始化Fragment的布局
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);
        titleText = (TextView)view.findViewById(R.id.title_text);
        backButton = (Button)view.findViewById(R.id.back_button);
        listView = (ListView)view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,dataList);
        //将自己写好的适配器放入AlphaInAnimationAdapter
        animationAdapter = new AlphaInAnimationAdapter(adapter);
        //设置精确监听
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);

        return view;
    }

    //在布局活动创建之时
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("a","----------------------------");
                if (currentLevel == LEVEL_PROVINCE){
                    //如果当前等级为省，则将省列表的位置赋给被选中的省份
                    selectedProvince = provinceList.get(position);
                    //遍历被选中的省份中的市
                    queryCities();
                }else if (currentLevel == LEVEL_CITY){
                    //如果当前等级为市，则将市列表的位置赋给被选中的市
                    selectedCity = cityList.get(position);
                    //遍历被选中的市里的县
                    queryCounties();
                }else if (currentLevel == LEVEL_COUNTY) {
                    //如果当前等级为县，则获取该县所在位置再获取其天气id赋给weatherId
                        String weatherId = countyList.get(position).getWeatherId();
                    if (getActivity() instanceof MainActivity){
                        //如果寄主活动是MainActivity，则跳转到WeatherActivity
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        //传递出weatherId
                        intent.putExtra("weather_id", weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    }else if (getActivity() instanceof WeatherActivity){
                        //如果寄主活动是WeatherActivity，则声明他的实例activity
                        WeatherActivity activity = (WeatherActivity)getActivity();
                        //通过实例activity控制侧滑关闭，下拉刷新
                        activity.drawerLayout.closeDrawers();
                        activity.swipeRefresh.setRefreshing(true);
                        activity.requestWeather(weatherId);
                    }
                }
            }
        });
        //返回按钮
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY){
                    queryCities();
                }else if (currentLevel == LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
        //默认遍历省列表
        queryProvinces();

    }


    //查询全国所有的省，优先从数据库查，如果没有查到再到数据库上查
    private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        //通过Litepal的DataSupport查询所有的省
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            //查到了先清理一遍，再遍历一遍城市名字
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            //动态刷新列表，重绘当前可见区域
            adapter.notifyDataSetChanged();
            //列表移动到指定的position处
            listView.setSelection(0);
            //修改当前等级
            currentLevel = LEVEL_PROVINCE;
        }else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }

    //查询省内所有的市，优先从数据库查，如果没有查到再到数据库上查
    private void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid=?",String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0){
            dataList.clear();
            for (City cities : cityList) {
                dataList.add(cities.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address,"city");
        }
    }

    //查询市内所有的县，优先从数据库查，如果没有查到再到数据库上查
    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid=?",String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0){
            dataList.clear();
            for (County counties : countyList){
                dataList.add(counties.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address,"county");
        }
    }
    //根据传入的地址和类型从服务器上查询省市县数据
    private void queryFromServer(String address, final String type) {
        //显示进度对话框
        showProgressDialog();
        //网络请求操作在runOnUiThread中进行
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                //请求的省市县两两匹配就解析所请求的地区
                if ("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);
                }else if ("city".equals(type)){
                    result = Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if ("county".equals(type)){
                    result = Utility.handleCountyResponse(responseText,selectedCity.getId());
                }
                //请求成功
                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //隐藏进度对话框
                            closeProgressDialog();
                            //若两两匹配就遍历省或市或县
                            if ("province".equals(type)){
                                queryProvinces();
                            }else if ("city".equals(type)){
                                queryCities();
                            }else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }
    //显示进度对话框
    private void showProgressDialog(){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载....");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    //关闭进度对话框
    private void closeProgressDialog(){
        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }


}
