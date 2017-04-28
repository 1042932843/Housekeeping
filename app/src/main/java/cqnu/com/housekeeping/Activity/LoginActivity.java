package cqnu.com.housekeeping.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.utils.L;
import com.wang.avi.AVLoadingIndicatorView;


import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import cqnu.com.housekeeping.Design.loadingView.Loading;
import cqnu.com.housekeeping.Fragment.UcenterIndexFragment;
import cqnu.com.housekeeping.Http.NetParams;
import cqnu.com.housekeeping.R;
import cqnu.com.housekeeping.Utils.JudgeUtil;
import cqnu.com.housekeeping.Utils.StringUtil;

import org.xutils.http.cookie.DbCookieStore;
import org.xutils.x;

import java.net.HttpCookie;
import java.util.List;

/**
 * Created by DSY on 2016/12/2.
 * 用户登录哈哈
 */
public class LoginActivity extends AppCompatActivity {


    private boolean mAuthTask = true;
    private boolean flag;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private TextView cannotsign,goregist;
    private AVLoadingIndicatorView avloadingIndicatorView_BallClipRotatePulse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent=getIntent();
        flag =intent.getBooleanExtra("flag",true);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.phone);
        cannotsign = (TextView)findViewById(R.id.textView2);
        goregist=(TextView)findViewById(R.id.textView3);
        mPasswordView = (EditText) findViewById(R.id.password);
        avloadingIndicatorView_BallClipRotatePulse=(AVLoadingIndicatorView)findViewById(R.id.avloadingIndicatorView_BallClipRotatePulse);
        Loading.hideProgress(avloadingIndicatorView_BallClipRotatePulse);
        TextView universal_title=(TextView)findViewById(R.id.universal_title);
        universal_title.setText("用户登录");
        Button mEmailSignInButton = (Button) findViewById(R.id.sign);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        goregist.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegistActivity.class);
                startActivity(intent);
            }
        });
    }

    private void attemptLogin() {
        if (mAuthTask != true) {
            return;
        }

        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password) || !JudgeUtil.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!JudgeUtil.isPhoneValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Loading.showProgress(avloadingIndicatorView_BallClipRotatePulse);
            mAuthTask=false;
            doLogin(email,password);

        }
    }

    //登录
    public void doLogin(final String username,String password){
        //RequestParams params = new RequestParams(StringUtil.BaseUrl+"/api/user/login");
        NetParams params = new NetParams( "/api/user/login");
        params.addBodyParameter("username", username);
        params.addBodyParameter("password", password);
        x.http().get(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onCancelled(CancelledException arg0) {
                System.out.print("dsy");
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Loading.hideProgress(avloadingIndicatorView_BallClipRotatePulse);
                Toast.makeText(LoginActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFinished() {
                mAuthTask = true;
            }

            @Override
            public void onSuccess(JSONObject arg0) {
                try {
                    String code = arg0.getString("code");
                    String msg = arg0.getString("msg");
                    //String pay_code=arg0.getString("pay");
                    if (code.equals("1")) {
                        String userid = arg0.getString("userid");
                        SharedPreferences preferences = getSharedPreferences(
                                "user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("user_name",username);
                        editor.putString("user_id", userid);
                        //editor.putString("pay_code", pay_code);
                        DbCookieStore instance = DbCookieStore.INSTANCE;
                        List<HttpCookie> cookies = instance.getCookies();
                        for (HttpCookie cookie:cookies) {
                            String name = cookie.getName();
                            String value = cookie.getValue();
                            L.d(name + " " + value);
                            if ("JSESSIONID".equals(name)) {
                                String myCookie = value;
                                editor.putString("Cookie", myCookie);
                            }
                        }
                        editor.apply();
                        Loading.hideProgress(avloadingIndicatorView_BallClipRotatePulse);
                        if(flag){
                            Intent intent = new Intent(LoginActivity.this, UserCenterActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    }else{
                        Loading.hideProgress(avloadingIndicatorView_BallClipRotatePulse);
                    }
                    Toast.makeText(LoginActivity.this, msg,Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

}

