package cn.edu.pku.gongrunyu.projectcreate_1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.pku.gongrunyu.app.MyApplication;
import cn.edu.pku.gongrunyu.bean.City;

public class SelectCity extends Activity implements View.OnClickListener{

    private ImageView mBackBtn;
    private ListView mList;
    private List<City> cityList;
    private ArrayAdapter<String> myadapter;
    private SearchView searchView;

    private ArrayList<String> mSearchResult = new ArrayList<>(); //搜索结果，只放城市名
    private Map<String,String> nameToCode = new HashMap<>(); //城市名到编码
    private Map<String,String> nameToPinyin = new HashMap<>(); //城市名到拼音

    //一些初始化
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);


        searchView = (SearchView) findViewById(R.id.search);
        searchView.setIconified(true); //需要点击搜索图标，才展开搜索框
        searchView.setQueryHint("请输入城市名称或拼音");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) { //搜索栏不空时，执行搜索
                    if (mSearchResult != null) //清空上次搜索结果
                        mSearchResult.clear();
                    //遍历 nameToPinyin 的键值（它包含所有城市名）
                    for (String str : nameToPinyin.keySet()) {
                        newText = newText.toUpperCase();
                        if
                                (str.contains(newText)||nameToPinyin.get(str).contains(newText)) {
                            mSearchResult.add(str);
                        }
                    }
                    myadapter.notifyDataSetChanged();
                }
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                //实际不执行，文本框一变化就自动执行搜索
                Toast.makeText(SelectCity.this, "检索中", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

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

        String strName;
        String strCode;
        String strNamePinyin;
        for (int i=0;i<cityList.size();i++){
            strName = cityList.get(i).getCity();
            strCode = cityList.get(i).getNumber();
            strNamePinyin = cityList.get(i).getAllPY();
            nameToCode.put(strName,strCode);
            nameToPinyin.put(strName,strNamePinyin);
            mSearchResult.add(strName + " " + strCode);
        }

        myadapter = new ArrayAdapter<String>(SelectCity.this,
                android.R.layout.simple_list_item_1,mSearchResult);
        mList.setAdapter(myadapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(SelectCity.this,
                        "CityCode"+ cityList.get(position).getNumber(),
                        Toast.LENGTH_SHORT).show();
                        */
                Intent i = new Intent();
                String returnCityName = mSearchResult.get(position).split(" ")[0];
                String returnCode = nameToCode.get(returnCityName);
                i.putExtra("cityCode",returnCode);
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
