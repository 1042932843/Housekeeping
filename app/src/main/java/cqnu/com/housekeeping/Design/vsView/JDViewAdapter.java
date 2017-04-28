package cqnu.com.housekeeping.Design.vsView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cqnu.com.housekeeping.R;


/**
 * Created by Administrator on 2016/3/20.
 * 广告栏数据适配器
 *
 */

public class JDViewAdapter {
    private List<AdverNotice> mDatas;

    public JDViewAdapter(List<AdverNotice> mDatas) {
        this.mDatas = mDatas;

        if (mDatas == null || mDatas.isEmpty()) {
            throw new RuntimeException("nothing to show");
        }
    }
    /**
     * 获取数据的条数
     * @return
     */
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    /**
     * 获取摸个数据
     * @param position
     * @return
     */
    public AdverNotice getItem(int position) {
        return mDatas.get(position);
    }
    /**
     * 获取条目布局
     * @param parent
     * @return
     */
    public View getView(JDAdverView parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_page_wishyoulike_item, null);
    }

    /**
     * 条目数据适配
     * @param view
     * @param data
     */
    public void setItem(final View view, final AdverNotice data) {
       // TextView tv = (TextView) view.findViewById(R.id.title);
        //tv.setText(data.getTitle());
        //TextView txt = (TextView) view.findViewById(R.id.txt);
       // txt.setText(data.getTxt());
        ImageView tagView = (ImageView) view.findViewById(R.id.img);
        ImageLoader.getInstance().displayImage(data.getUrl(), tagView);
        //你可以增加点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //比如打开url
                Toast.makeText(view.getContext(), data.getUrl(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}