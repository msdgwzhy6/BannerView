package com.bannerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BannerView bannerView;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn = (Button) findViewById(R.id.btn);
        bannerView = (BannerView) findViewById(R.id.bannerView);

        final List<BannerModel> bannerModelList = new ArrayList<>();
        bannerModelList.add(new BannerModel(1, "http://bmob-cdn-10899.b0.upaiyun.com/2017/05/09/1add12e3407aa2ac80899838f5e5a097.jpg"));
        bannerModelList.add(new BannerModel(2, "http://bmob-cdn-10899.b0.upaiyun.com/2017/05/09/34b6d85c406894f3803d949a78c4546e.jpg"));
        bannerModelList.add(new BannerModel(3, "http://bmob-cdn-10899.b0.upaiyun.com/2017/05/09/1664c954400bf4d880fdd4d70b31ff2c.jpg"));
        //第一次设置数据
        bannerView.setBannerModelList(bannerModelList);
        //模拟执行刷新
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bannerView.refreshBannerModelList(bannerModelList);
            }
        });
        /**
         * 点击事件的数据回调
         * 实际项目中根据数据挑战到相应的界面
         */
        bannerView.setsetSelectItemClickstener(new BannerView.OnSelectItemClickstener() {
            @Override
            public void onSelectItem(int position, BannerModel bannerModel) {
                Toast.makeText(MainActivity.this, "第"+position +"个"+ bannerModel.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
