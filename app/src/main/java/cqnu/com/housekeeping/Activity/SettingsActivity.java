package cqnu.com.housekeeping.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wang.avi.AVLoadingIndicatorView;

import cqnu.com.housekeeping.Design.loadingView.Loading;
import cqnu.com.housekeeping.R;

public class SettingsActivity extends AppCompatActivity {
    private AVLoadingIndicatorView avloadingIndicatorView_BallClipRotatePulse;
    private LinearLayout cache;
    private TextView cachetext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        avloadingIndicatorView_BallClipRotatePulse=(AVLoadingIndicatorView)findViewById(R.id.avloadingIndicatorView_BallClipRotatePulse);
        cachetext=(TextView)findViewById(R.id.cache);
        Loading.hideProgress(avloadingIndicatorView_BallClipRotatePulse);
        TextView universal_title=(TextView)findViewById(R.id.universal_title);
        universal_title.setText("设置");
        //cachetext.setText(ImageLoader.getInstance().getMemoryCache().toString());
        cache=(LinearLayout)findViewById(R.id.setting_cache);
        cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageLoader.getInstance().clearMemoryCache();
                ImageLoader.getInstance().clearDiskCache();
                Toast.makeText(SettingsActivity.this, "缓存清理完毕", Toast.LENGTH_SHORT).show();
                //cachetext.setText(ImageLoader.getInstance().getMemoryCache().toString());
            }
        });
    }
}
