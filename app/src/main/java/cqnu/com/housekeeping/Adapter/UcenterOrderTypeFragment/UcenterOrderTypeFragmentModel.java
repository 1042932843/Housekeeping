package cqnu.com.housekeeping.Adapter.UcenterOrderTypeFragment;

import java.util.List;

/**
 * Created by DSY on 2016/12/15.
 */
public class UcenterOrderTypeFragmentModel {
    private String code;
    private List<Bean> goods;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Bean> getGoods() {
        return goods;
    }

    public void setGoods(List<Bean> goods) {
        this.goods = goods;
    }
}
