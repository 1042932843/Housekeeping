package cqnu.com.housekeeping.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

import cqnu.com.housekeeping.Design.loadingView.Loading;
import cqnu.com.housekeeping.Http.NetParams;
import cqnu.com.housekeeping.Model.Advertisement;
import cqnu.com.housekeeping.Model.OrderReturn;
import cqnu.com.housekeeping.R;
import cqnu.com.housekeeping.Utils.GsonUtil;

public class Welcome extends Activity {
    private Advertisement data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏并全屏显示的欢迎界面
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        Goto_homeActivity();
    }
    /**
     * 1秒延时开始加载数据
     */
    private void Goto_homeActivity() {
        final Intent localIntent = new Intent(this, MainActivity.class);
        Timer timer = new Timer();
        TimerTask tast = new TimerTask() {
            @Override
            public void run() {
                get_Advertisement();
               // startActivity(localIntent);
               // finish();
                //如果finish，动画效果不完成
            }
        };
        timer.schedule(tast, 1000);
    }

    /**
     * 初始加载后跳转
     */
    private void get_Advertisement(){
        NetParams params = new NetParams( "/api/ad/getAll");

        x.http().get(params, new Callback.CommonCallback<JSONObject>() {

            @Override
            public void onCancelled(CancelledException arg0) {
                System.out.print("dsy");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(Welcome.this, "网络错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(JSONObject arg0) {
                data = GsonUtil.parseJsonWithGson(arg0.toString(), Advertisement.class);
                String code = data.getCode();
                if (code.equals("1")) {
                    Intent localIntent = new Intent(Welcome.this, MainActivity.class);
                    localIntent.putExtra("data",data);
                    startActivity(localIntent);
                    finish();
                } else {
                    Toast.makeText(Welcome.this, "数据加载失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
