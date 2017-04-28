package cqnu.com.housekeeping.Model;

import java.io.Serializable;

/**
 * Created by DSY on 2016/12/13.
 */
public class AdvertisementItem implements Serializable{
    private String msg;
    private String img;
    private String goodsid;
    private String url;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
