package cqnu.com.housekeeping.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DSY on 2016/12/5.
 */
public class JudgeUtil {
    /**
    *金额正则（保留小数后两位）
     * @return
    */
    public static boolean isMoney(String str){
        Pattern pattern= Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match=pattern.matcher(str);
        if(match.matches()==false){
            return false;
        }else{
            return true;
        }
    }
    /**
     * 判断是否登录
     * @return
     */
    public static boolean isLogin(Context context){
            SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
            String user_name = preferences.getString("user_name", "");
            String user_id = preferences.getString("user_id", "");
            if (user_id != null && !user_id.equals("") && user_name != null&& !user_name.equals("")) {
                return true;
            }
            return false;
    }
    /**
     * 判断密码长度
     * @return
     */
    public static boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }
    /**
     * 判断是否手机号
     * @return
     */
    public static boolean isPhoneValid(String phone) {
        String regExp = "^[1]([3][0-9]{1}|59|58|88|89|87)[0-9]{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        return m.find();
    }
    /**
     * 判断用户名
     * @return
     */
    public static boolean isUsernameValid(String username) {
        return username.length()>3;
    }
    /**
     * 判断密码重复是否正确
     * @return
     */
    public static boolean isRePasswordValid(String password,String repassword) {
        return password.equals(repassword);
    }
}
