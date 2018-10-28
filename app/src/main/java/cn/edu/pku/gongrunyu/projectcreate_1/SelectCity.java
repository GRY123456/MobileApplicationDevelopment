package cn.edu.pku.gongrunyu.projectcreate_1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import cn.edu.pku.gongrunyu.app.MyApplication;
import cn.edu.pku.gongrunyu.bean.City;

public class SelectCity extends Activity implements View.OnClickListener{

    private ImageView mBackBtn;
    private ListView mList;
    private List<City> cityList;
    private String[] data;
    private ArrayAdapter<String> myadapter;

    //一些初始化
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

        initViews();
    }

    //初始化，并显示ListView,并且是在监听事件
    private void initViews(){
        //为mBackBtn设置监听事件
        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);

        //mClearEditText = (ClearEditText) findViewById(R.id.search_city);

        mList = (ListView) findViewById(R.id.title_list);
        MyApplication myApplication = (MyApplication) getApplication();
        cityList = myApplication.getCityList();

        data = new String[cityList.size()];
        for (int i=0;i<cityList.size();i++){
            data[i] = cityList.get(i).getCity()
                    + " " + cityList.get(i).getNumber();
        }

        myadapter = new ArrayAdapter<String>(SelectCity.this,
                android.R.layout.simple_list_item_1,data);
        mList.setAdapter(myadapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(SelectCity.this,
                        "CityCode"+ cityList.get(position).getNumber(),
                        Toast.LENGTH_SHORT).show();
                        */
                Intent i = new Intent();
                String str = cityList.get(position).getNumber();
                i.putExtra("cityCode",str);
                setResult(RESULT_OK,i);
                finish();
            }
        });
    }

    //响应点击返回按钮的监听事件
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.title_back:
                Intent i = new Intent();
                i.putExtra("cityCode","101160101");
                setResult(RESULT_OK,i);
                finish();
                break;
            default:
                break;
        }
    }
}
