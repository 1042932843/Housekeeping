package cqnu.com.housekeeping.Model;

/**
 * Created by HP on 2016/12/15.
 */
public class UserModel {
    private String code;
    private String img;
    private String money;
    private String id;
    private String username;
    private String isSetPay;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIsSetPay() {
        return isSetPay;
    }

    public void setIsSetPay(String isSetPay) {
        this.isSetPay = isSetPay;
    }
}
