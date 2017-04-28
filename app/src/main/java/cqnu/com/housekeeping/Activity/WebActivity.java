package cqnu.com.housekeeping.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wang.avi.AVLoadingIndicatorView;

import cqnu.com.housekeeping.Design.loadingView.Loading;
import cqnu.com.housekeeping.R;

public class WebActivity extends AppCompatActivity {
    private AVLoadingIndicatorView avloadingIndicatorView_BallClipRotatePulse;
    private WebView webview;
    private Intent it;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置窗口风格为进度条
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_web);
        avloadingIndicatorView_BallClipRotatePulse=(AVLoadingIndicatorView)findViewById(R.id.avloadingIndicatorView_BallClipRotatePulse);
        Loading.hideProgress(avloadingIndicatorView_BallClipRotatePulse);
        TextView universal_title=(TextView)findViewById(R.id.universal_title);
        it=getIntent();
        universal_title.setText(it.getStringExtra("context"));
        webview=(WebView)findViewById(R.id.webView);
        //cachetext.setText(ImageLoader.getInstance().getMemoryCache().toString());
        loadweb(it.getStringExtra("context"));

    }
    public void loadweb(String string){


		webview.getSettings().setJavaScriptEnabled(true);

		// 设置可以支持缩放
		webview.getSettings().setSupportZoom(true);
		// 设置出现缩放工具
		webview.getSettings().setBuiltInZoomControls(false);
		//扩大比例的缩放
		webview.getSettings().setUseWideViewPort(true);

        webview.getSettings().setDomStorageEnabled(true);
		//自适应屏幕
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		webview.getSettings().setLoadWithOverviewMode(true);
		webview.getSettings().setBlockNetworkImage(false);
		webview.loadUrl("https://www.baidu.com/");
        webview.setWebViewClient(new WebViewClient());
    }
}
