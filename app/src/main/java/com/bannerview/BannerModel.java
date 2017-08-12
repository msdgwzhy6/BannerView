package com.bannerview;

/**
 * Created by wujun on 2017/8/11.
 * banner的实体类，根据自己的实际项目去定制
 * @author madreain
 * @desc
 */

public class BannerModel {
    private int id;
    private String imgurl;

    public BannerModel() {
    }

    public BannerModel(int id, String imgurl) {
        this.id = id;
        this.imgurl = imgurl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    @Override
    public String toString() {
        return "BannerModel{" +
                "id=" + id +
                ", imgurl='" + imgurl + '\'' +
                '}';
    }
}
