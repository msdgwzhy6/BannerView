package com.bannerview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wujun on 2017/8/11.
 * banner
 *
 * @author madreain
 * @desc
 */

public class BannerView extends FrameLayout {

    Context mContext;
    private ViewPager viewpager_banner;
    private List<View> bannerviewsList;
    private BannerViewPagerAdapter bannerViewPagerAdapter;
    //这里的数据model仅供参考
    private List<BannerModel> bannerModelList;
    int bannerModelListSize;

    //将小圆点的图片用数组表示
    private ImageView[] imageViews;
    //包裹小圆点的LinearLayout
    private LinearLayout layout_page;
    private ImageView imageView;
    //自动播放
    private final int AUTO_MSG = 1;
    private final int HANDLE_MSG = AUTO_MSG + 1;
    private static final int PHOTO_CHANGE_TIME = 2000;//定时变量
    private int index = 0;

    public BannerView(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = LayoutInflater.from(mContext).inflate(R.layout.banner_layout, null);
        viewpager_banner = (ViewPager) view.findViewById(R.id.viewpager);
        layout_page = (LinearLayout) view.findViewById(R.id.layout_page);
        addView(view);

        bannerModelList = new ArrayList<>();
        bannerviewsList = new ArrayList<>();
    }

    /**
     * 第一次设置数据的方法
     *
     * @param mbannerModelList
     */
    public void setBannerModelList(List<BannerModel> mbannerModelList) {
        this.bannerModelList = mbannerModelList;
        // 本地
        if (bannerModelList != null) {
            if (bannerModelList.size() > 0) {
                //本地存储
                bannerModelListSize = bannerModelList.size();

                for (BannerModel bannerModel : bannerModelList) {
                    ImageView imageView = new ImageView(mContext);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    // + "!thumb"  缩略图
                    Glide.with(mContext).load(bannerModel.getImgurl()).into(imageView);
                    bannerviewsList.add(imageView);
                }
            }
        }
        //添加小圆点的图片
        if (bannerModelList != null) {
            imageViews = new ImageView[bannerModelListSize];
            for (int i = 0; i < bannerviewsList.size(); i++) {
                imageView = new ImageView(mContext);
                //设置小圆点imageview的参数
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
                layoutParams.setMargins(10, 0, 10, 0);
//            imageView.setLayoutParams(new ViewGroup.LayoutParams(20,20));//创建一个宽高均为20 的布局
                imageView.setLayoutParams(layoutParams);//创建一个宽高均为20 的布局
                imageView.setPadding(20, 0, 20, 0);
                //默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
                if (i == 0) {
                    imageView.setBackgroundResource(R.drawable.shape_intro);
                } else {
                    imageView.setBackgroundResource(R.drawable.shape_intro_nor);
                }
                //将imageviews添加到小圆点视图组
                layout_page.addView(imageView);
                imageViews[i] = imageView;

            }
            bannerViewPagerAdapter = new BannerViewPagerAdapter(bannerviewsList);
            viewpager_banner.setAdapter(bannerViewPagerAdapter);
            viewpager_banner.addOnPageChangeListener(new GuidePageChangeListener());
//            viewpager_banner.setOnPageChangeListener(new GuidePageChangeListener());
        }
        //设置自动播放
        mHandler.sendEmptyMessageDelayed(AUTO_MSG, PHOTO_CHANGE_TIME);
    }

    /**
     * 刷新的方法
     *
     * @param mbannerModelList
     */
    public void refreshBannerModelList(List<BannerModel> mbannerModelList) {
        this.bannerModelList = mbannerModelList;
        if (bannerModelList.size() > 0) {
            bannerModelListSize = bannerModelList.size();

            //刷新时上一次的数据要进行清除
            if (bannerviewsList != null) {
                bannerviewsList.clear();
            } else {
                bannerviewsList = new ArrayList<View>();
            }

            for (BannerModel bannerModel : bannerModelList) {

                ImageView imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(mContext).load(bannerModel.getImgurl()).into(imageView);

                bannerviewsList.add(imageView);

            }
        }

        //添加小圆点的图片
        if (bannerModelList != null) {

            //照片轮播下方的小圆点刷新时需要重新加载  并将上次的进行remove
            if (layout_page != null) {
                layout_page.removeAllViews();
            }
            //小圆点显示的个数就是照片轮播的实体个数
            imageViews = new ImageView[bannerModelListSize];
            for (int i = 0; i < bannerviewsList.size(); i++) {
                imageView = new ImageView(mContext);
                //设置小圆点imageview的参数
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
                layoutParams.setMargins(10, 0, 10, 0);
//            imageView.setLayoutParams(new ViewGroup.LayoutParams(20,20));//创建一个宽高均为20 的布局
                imageView.setLayoutParams(layoutParams);//创建一个宽高均为20 的布局
                imageView.setPadding(20, 0, 20, 0);
                //默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
                if (i == 0) {
                    imageView.setBackgroundResource(R.drawable.shape_intro);
                } else {
                    imageView.setBackgroundResource(R.drawable.shape_intro_nor);
                }
                //将imageviews添加到小圆点视图组
                layout_page.addView(imageView);
                imageViews[i] = imageView;

            }

            bannerViewPagerAdapter = new BannerViewPagerAdapter(bannerviewsList);
            viewpager_banner.setAdapter(bannerViewPagerAdapter);
            viewpager_banner.addOnPageChangeListener(new GuidePageChangeListener());
//            viewpager_banner.setOnPageChangeListener(new GuidePageChangeListener());

        }
    }

    //照片轮播的适配器
    private class BannerViewPagerAdapter extends PagerAdapter {
        private List<View> views;

        private BannerViewPagerAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public void finishUpdate(ViewGroup container) {
        }

        @Override
        public int getCount() {
            if (views != null) {
                return views.size();
            }
            return 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            container.addView(views.get(position));

            //单独的点击事件
            views.get(position).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (onSelectItemClickstener != null) {
                        BannerModel bannerModel = bannerModelList.get(position);
                        onSelectItemClickstener.onSelectItem(position, bannerModel);
                    }
                }
            });

            return views.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }


    //viewpager 监听   照片轮播
    private class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < imageViews.length; i++) {
                //设置index 防止下次自动播放顺序出错
                index = viewpager_banner.getCurrentItem();
                imageViews[position].setBackgroundResource(R.drawable.shape_intro);
                //不是当前选中的page，其小圆点设置为未选中的状态
                if (position != i) {
                    imageViews[i].setBackgroundResource(R.drawable.shape_intro_nor);
                }
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    //照片轮播设置无限循环播放
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AUTO_MSG:
                    //无线循环播放
                    if (index >= bannerModelListSize) {
                        index = 0;
                    }
                    viewpager_banner.setCurrentItem(index++);//收到消息后设置当前要显示的图片
                    mHandler.sendEmptyMessageDelayed(AUTO_MSG, PHOTO_CHANGE_TIME);
                    break;
                case HANDLE_MSG:
                    mHandler.sendEmptyMessageDelayed(AUTO_MSG, PHOTO_CHANGE_TIME);
                    break;
                default:
                    break;
            }
        }
    };


    private OnSelectItemClickstener onSelectItemClickstener;

    /***
     * bannerview点击监听方法
     * @param onSelectItemClickstener
     */
    public void setsetSelectItemClickstener(OnSelectItemClickstener onSelectItemClickstener) {
        this.onSelectItemClickstener = onSelectItemClickstener;
    }

    /**
     * 接受回调参数
     */
    interface OnSelectItemClickstener {
        void onSelectItem(int position, BannerModel bannerModel);
    }

}
