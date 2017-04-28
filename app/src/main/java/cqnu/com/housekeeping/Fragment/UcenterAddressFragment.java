package cqnu.com.housekeeping.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import cqnu.com.housekeeping.Design.loadingView.Loading;
import cqnu.com.housekeeping.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UcenterAddressFragment extends Fragment {
    private View rootView;
    private Bundle bundle;
    private AVLoadingIndicatorView avloadingIndicatorView_BallClipRotatePulse;
    public UcenterAddressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_announcement, container, false);
        // Inflate the layout for this fragment
        bundle=getArguments();
        avloadingIndicatorView_BallClipRotatePulse=(AVLoadingIndicatorView)rootView.findViewById(R.id.avloadingIndicatorView_BallClipRotatePulse);
        Loading.hideProgress(avloadingIndicatorView_BallClipRotatePulse);
        TextView universal_title=(TextView)rootView.findViewById(R.id.universal_title);
        universal_title.setText(bundle.getString("type"));
        return rootView;
    }

}
