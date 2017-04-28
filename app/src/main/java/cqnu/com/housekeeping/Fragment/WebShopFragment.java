package cqnu.com.housekeeping.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cqnu.com.housekeeping.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebShopFragment extends Fragment {
    private View rootView;
    private Bundle bundle;

    public WebShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_announcement, container, false);
        // Inflate the layout for this fragment
        bundle=getArguments();
        TextView universal_title=(TextView)rootView.findViewById(R.id.universal_title);
        universal_title.setText(bundle.getString("type"));
        return rootView;
    }

}
