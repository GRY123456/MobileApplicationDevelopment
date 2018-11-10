package cn.edu.pku.gongrunyu.projectcreate_1;

import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import java.util.List;

import cn.edu.pku.gongrunyu.app.MyApplication;
import cn.edu.pku.gongrunyu.bean.City;

public class MyLocationListerner extends BDAbstractLocationListener {
    public String recity;
    public String cityCode;
    @Override
    public void onReceiveLocation(BDLocation location) {
        String addr = location.getAddrStr();
        String country = location.getCountry();
        String province = location.getProvince();
        String city = location.getCity();
        String district = location.getDistrict();
        String street = location.getStreet();
        recity = city.replace("市","");
    }
}
