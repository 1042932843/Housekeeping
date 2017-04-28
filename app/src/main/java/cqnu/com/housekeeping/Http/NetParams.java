package cqnu.com.housekeeping.Http;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import cqnu.com.housekeeping.Utils.StringUtil;

/**
 * Created by DSY on 2016/12/6.
 * 持久化Cookie
 */
public class NetParams extends RequestParams {

    public NetParams(String url){
        super(StringUtil.BaseUrl+url);
        //setConnectTimeout(timeOut==0?30*1000:timeOut);
        if (!"/api/user/login".equals(url)&&!"/api/user/regist".equals(url)) { //登陆 和不需要cookie的接口
            SharedPreferences sharedPreferences = x.app().getSharedPreferences("user", Context.MODE_PRIVATE);
            String Cookie = sharedPreferences.getString("Cookie", "");
            addHeader("Cookie", "JSESSIONID="+Cookie);
            setUseCookie(false);
            addHeader("Content-Type", "application/json;charset=UTF-8");
        }else{
            setUseCookie(true);
        }

    }
}