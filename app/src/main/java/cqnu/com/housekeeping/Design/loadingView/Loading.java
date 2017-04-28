package cqnu.com.housekeeping.Design.loadingView;


import android.view.View;

import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by DSY on 2016/12/5.
 */
public class Loading {

    public static void showProgress(AVLoadingIndicatorView avLoadingIndicatorView){
        if(avLoadingIndicatorView!=null){
            avLoadingIndicatorView.setVisibility(View.VISIBLE);
        }

    }

    public static void hideProgress(AVLoadingIndicatorView avLoadingIndicatorView){
        if(avLoadingIndicatorView!=null){
            avLoadingIndicatorView.setVisibility(View.GONE);
        }

    }

}
