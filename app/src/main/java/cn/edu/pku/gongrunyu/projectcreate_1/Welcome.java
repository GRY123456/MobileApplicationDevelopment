package cn.edu.pku.gongrunyu.projectcreate_1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class Welcome extends AppCompatActivity{

    ViewPager pager = null;
    ArrayList<View> viewContainter = new ArrayList<View>();

    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty);

        pager = (ViewPager)this.findViewById(R.id.empty_vp);
        View view1 = LayoutInflater.from(this).inflate(R.layout.welcome, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.welcome2, null);
        viewContainter.add(view1);
        viewContainter.add(view2);
        pager.setAdapter(new MyPagerAdapters());
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 1){
                    button = (Button)findViewById(R.id.welcome2_button);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Welcome.this,
                                    MainActivity.class));
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    class MyPagerAdapters extends PagerAdapter{
        @Override
        public int getCount() {
            return viewContainter.size();
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container,
                                int position, @NonNull Object object) {
            ((ViewPager) container).removeView(viewContainter.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ((ViewPager) container).addView(viewContainter.get(position));
            return viewContainter.get(position);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }
    }
}
