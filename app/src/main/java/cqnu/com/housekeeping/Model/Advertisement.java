package cqnu.com.housekeeping.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DSY on 2016/12/13.
 * Serializable便于传递对象
 */
public class Advertisement implements Serializable{
    private String code;
    private List<AdvertisementItem> ad;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<AdvertisementItem> getAd() {
        return ad;
    }

    public void setAd(List<AdvertisementItem> ad) {
        this.ad = ad;
    }
}
