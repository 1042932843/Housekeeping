package cqnu.com.housekeeping.Design.cycleView;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import cqnu.com.housekeeping.Application.dsyApplication;
import cqnu.com.housekeeping.R;


/**
 * ImageView创建工厂
 */
public class ViewFactory {

	/**
	 * 获取ImageView视图的同时加载显示url
	 * @param
	 * @return
	 */
	public static ImageView getImageView(Context context, String url) {
		ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(
				R.layout.view_banner, null);
		dsyApplication.instance.displayImage(url, imageView);
		//ImageLoader.getInstance().displayImage(url, imageView);
		return imageView;
	}
}
