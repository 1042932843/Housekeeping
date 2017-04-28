package cqnu.com.housekeeping.Activity;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


import cqnu.com.housekeeping.Design.loadingView.Loading;
import cqnu.com.housekeeping.R;
import cqnu.com.housekeeping.Utils.JudgeUtil;
import cqnu.com.housekeeping.Utils.StringUtil;


/**
 * Created by DSY on 2016/12/2.
 * 用户注册嘻嘻
 */
public class RegistActivity extends AppCompatActivity{
    private AutoCompleteTextView mUsernameView,mPasswordView,mRePasswordView,mCode;
    private boolean mAuthTask = true;
    private AVLoadingIndicatorView avloadingIndicatorView_BallClipRotatePulse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);
        mPasswordView = (AutoCompleteTextView) findViewById(R.id.password);
        mRePasswordView = (AutoCompleteTextView) findViewById(R.id.repassword);
        mCode = (AutoCompleteTextView) findViewById(R.id.code);
        TextView universal_title=(TextView)findViewById(R.id.universal_title);
        universal_title.setText("用户注册");
        Button getcode =(Button)findViewById(R.id.getcode);
        getcode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegistActivity.this, "项目没有对接短信业务，所以你爱填不填~", Toast.LENGTH_SHORT).show();
            }
        });
        avloadingIndicatorView_BallClipRotatePulse=(AVLoadingIndicatorView)findViewById(R.id.avloadingIndicatorView_BallClipRotatePulse);
        Loading.hideProgress(avloadingIndicatorView_BallClipRotatePulse);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }


    private void attemptLogin() {
        if (mAuthTask != true) {
            return;
        }

        mUsernameView.setError(null);
        mPasswordView.setError(null);
        mRePasswordView.setError(null);
        mCode.setError(null);

        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String repassword = mRePasswordView.getText().toString();
        String code= mCode.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!JudgeUtil.isPhoneValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_email));
            focusView = mUsernameView;
            cancel = true;
        }
        if (TextUtils.isEmpty(password) || !JudgeUtil.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(repassword)||!JudgeUtil.isRePasswordValid(password, repassword)){
            mRePasswordView.setError(getString(R.string.error_incorrect_repassword));
            focusView = mRePasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Loading.showProgress(avloadingIndicatorView_BallClipRotatePulse);
            mAuthTask=false;
            doRegist(username, password);

        }
    }

    public void doRegist(final String username,String password){
        RequestParams params = new RequestParams(StringUtil.BaseUrl+"/api/user/regist");
        params.addBodyParameter("username", username);
        params.addBodyParameter("password", password);
        params.addBodyParameter("img", "");
        x.http().get(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onCancelled(CancelledException arg0) {
                System.out.print("dsy");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Loading.hideProgress(avloadingIndicatorView_BallClipRotatePulse);
                Toast.makeText(RegistActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
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
                    if (code.equals("1")) {
                        Toast.makeText(RegistActivity.this, msg,Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Loading.hideProgress(avloadingIndicatorView_BallClipRotatePulse);
                        Toast.makeText(RegistActivity.this, msg,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }


}

