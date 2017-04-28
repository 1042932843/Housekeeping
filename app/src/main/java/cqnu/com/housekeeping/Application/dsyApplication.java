package cqnu.com.housekeeping.Application;

import android.app.Application;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.xutils.x;

import cqnu.com.housekeeping.Http.AuthImageDownloader;
import cqnu.com.housekeeping.Utils.StringUtil;


/**
 * Created by HP on 2016/9/1.
 * For xUtils & Init More
 */
public class dsyApplication extends Application {
    private ImageLoader imageLoader=ImageLoader.getInstance();
    DisplayImageOptions options;
    public static dsyApplication instance = null;
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);//Xutils初始化
        //x.Ext.setDebug(true); // 是否输出debug日志
        configImageLoader();
        instance=this;
    }
    /**
     * 配置ImageLoder
     */
    private void configImageLoader() {
        // 初始化ImageLoader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(options)
               // .imageDownloader(new AuthImageDownloader(this))
                .memoryCacheExtraOptions(800, 800) // maxwidth, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) //你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);

        options = new DisplayImageOptions.Builder()
                //.showStubImage(R.mipmap.loading2) // 设置图片下载期间显示的图片
                //.showImageForEmptyUri(R.mipmap.loading2) // 设置图片Uri为空或是错误的时候显示的图片
                //.showImageOnFail(R.mipmap.loading2) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                .resetViewBeforeLoading(false)  // default
                        // .delayBeforeLoading(100)
                .bitmapConfig(Bitmap.Config.RGB_565)
                        //.displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象
    }
    public void displayImage(String url,ImageView img){
        imageLoader.displayImage(StringUtil.IMGBaseUrl+url, img, options);
    }

    public void clearCache(){
        imageLoader.clearMemoryCache();
        imageLoader.clearDiskCache();
    }
}