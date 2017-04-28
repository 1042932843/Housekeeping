package layout.cycleView;

import android.content.Context;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 2016/5/17.
 */
public class Initialize {
    private List<ImageView> views = new ArrayList<ImageView>();
    private List<ADInfo> infos = new ArrayList<ADInfo>();
    /**
     * View.cycleView
     * 初始配置轮播页面
     */
    public void initialize(CycleViewPager cycleViewPager,String[] imageUrls,CycleViewPager.ImageCycleViewListener CycleViewListener,Context context) {
        views.clear();
        infos.clear();
        for(int i = 0; i < imageUrls.length; i ++){
            ADInfo info = new ADInfo();
            info.setUrl(imageUrls[i]);
            info.setContent("图片-->" + i );
            infos.add(info);
        }
        // 将最后一个ImageView添加进来
        views.add(ViewFactory.getImageView(context, infos.get(infos.size() - 1).getUrl()));
        for (int i = 0; i < infos.size(); i++) {
            views.add(ViewFactory.getImageView(context, infos.get(i).getUrl()));
        }
        // 将第一个ImageView添加进来
        views.add(ViewFactory.getImageView(context, infos.get(0).getUrl()));

        // 设置循环，在调用setData方法前调用
        cycleViewPager.setCycle(true);

        // 在加载数据前设置是否循环
        cycleViewPager.setData(views, infos, CycleViewListener);
        //设置轮播
        cycleViewPager.setWheel(true);
        // 设置轮播时间，默认5000ms
        cycleViewPager.setTime(10000);
        //设置圆点指示图标组居中显示，默认靠右
        //cycleViewPager.setIndicatorCenter();
    }
}
