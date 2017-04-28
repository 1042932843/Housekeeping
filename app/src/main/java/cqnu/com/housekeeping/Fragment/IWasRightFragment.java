package cqnu.com.housekeeping.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cqnu.com.housekeeping.Activity.LoginActivity;
import cqnu.com.housekeeping.Adapter.IWasRightFragment.Bean;
import cqnu.com.housekeeping.Adapter.IWasRightFragment.IWasRightFragmentAdapter;
import cqnu.com.housekeeping.Adapter.IWasRightFragment.IWasRightFragmentModel;
import cqnu.com.housekeeping.Design.loadingView.Loading;
import cqnu.com.housekeeping.Http.NetParams;
import cqnu.com.housekeeping.R;
import cqnu.com.housekeeping.Utils.GsonUtil;
import cqnu.com.housekeeping.Utils.StringUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class IWasRightFragment extends Fragment {
    private View rootView;
    private Bundle bundle;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private AVLoadingIndicatorView avloadingIndicatorView_BallClipRotatePulse;
    private boolean mAuthTask = true;
    private  IWasRightFragmentModel data=new IWasRightFragmentModel();
    private IWasRightFragmentAdapter adapter;
    private List<Bean> list=new ArrayList<>();
    public IWasRightFragment() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        getIWasRightFragmentData();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_announcement, container, false);
        // Inflate the layout for this fragment
        bundle=getArguments();
        TextView universal_title=(TextView)rootView.findViewById(R.id.universal_title);
        universal_title.setText(bundle.getString("type"));
        swipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        avloadingIndicatorView_BallClipRotatePulse=(AVLoadingIndicatorView)rootView.findViewById(R.id.avloadingIndicatorView_BallClipRotatePulse);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        // swipeRefreshLayout.setProgressBackgroundColor(R.color.white);
        //swipeRefreshLayout.setPadding(20, 20, 20, 20);
        //swipeRefreshLayout.setProgressViewOffset(true, 100, 200);
        //swipeRefreshLayout.setDistanceToTriggerSync(50);
        swipeRefreshLayout.setProgressViewEndTarget(true, 100);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getIWasRightFragmentData();
            }
        });
        listView=(ListView)rootView.findViewById(R.id.listView);
        adapter=new IWasRightFragmentAdapter(getContext(),list);
        listView.setAdapter(adapter);
        getIWasRightFragmentData();
        return rootView;
    }

    public void getIWasRightFragmentData(){
        mAuthTask = false;
        NetParams params = new NetParams( "/api/goods/getAll");
        x.http().get(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onCancelled(CancelledException arg0) {
                System.out.print("dsy");
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Loading.hideProgress(avloadingIndicatorView_BallClipRotatePulse);
                Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onFinished() {
                mAuthTask = true;
            }
            @Override
            public void onSuccess(JSONObject arg0) {
                data = GsonUtil.parseJsonWithGson(arg0.toString(), IWasRightFragmentModel.class);
                String code = data.getCode();
                if (code.equals("1") && data.getGoods() != null) {
                    list.clear();
                    list.addAll(data.getGoods());
                    adapter.notifyDataSetChanged();
                } else if(code.equals("3")) {
                    Toast.makeText(getActivity(), "登录过期,请重新登录", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getActivity(),LoginActivity.class);
                    intent.putExtra("flag",false);
                    startActivity(intent);
                }else{
                        Toast.makeText(getActivity(), "数据错误", Toast.LENGTH_SHORT).show();
                    }
                Loading.hideProgress(avloadingIndicatorView_BallClipRotatePulse);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
