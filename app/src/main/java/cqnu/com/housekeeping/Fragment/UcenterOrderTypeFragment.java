package cqnu.com.housekeeping.Fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cqnu.com.housekeeping.Activity.LoginActivity;
import cqnu.com.housekeeping.Activity.UserCenterActivity;
import cqnu.com.housekeeping.Adapter.UcenterOrderTypeFragment.Bean;
import cqnu.com.housekeeping.Adapter.UcenterOrderTypeFragment.UcenterOrderTypeFragmentAdapter;
import cqnu.com.housekeeping.Adapter.UcenterOrderTypeFragment.UcenterOrderTypeFragmentModel;
import cqnu.com.housekeeping.Design.loadingView.Loading;
import cqnu.com.housekeeping.Http.NetParams;
import cqnu.com.housekeeping.Model.OrderReturn;
import cqnu.com.housekeeping.R;
import cqnu.com.housekeeping.Utils.GsonUtil;
import cqnu.com.housekeeping.Utils.StringUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class UcenterOrderTypeFragment extends Fragment {
    private View rootView;
    private Bundle bundle;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private UcenterOrderTypeFragmentModel data;
    private OrderReturn orderReturn;
    private List<Bean> list=new ArrayList<>();
    private UcenterOrderTypeFragmentAdapter adapter;
    private boolean mAuthTask = true;
    private String pageType;
    private boolean isFirst=true;
    private AVLoadingIndicatorView avloadingIndicatorView_BallClipRotatePulse;
    private AlertDialog.Builder builder;
    private Dialog dialog;
    public UcenterOrderTypeFragment() {
        // Required empty public constructor
    }

    public BroadcastReceiver UcenterOrderTypeFragmentReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            HashMap<String,Object> CMD=(HashMap<String,Object>) intent.getSerializableExtra("CMD");
            if(CMD!=null) {
                String type = (String) CMD.get("type");
                String content = (String) CMD.get("content");
                switch (type){
                    case "pay":
                        final String orderid = (String) CMD.get("id");
                        if(StringUtil.isSetPay.equals("true")){
                            final EditText inputServer = new EditText(getContext());
                            builder = new AlertDialog.Builder(getContext(),AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                            builder.setView(inputServer);
                            builder.setTitle("请输入支付密码");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String pay= inputServer.getText().toString();
                                    Pay(orderid,pay);
                                }
                            });
                            builder.setNegativeButton("取消",null);
                            dialog=builder.show();
                        }else{
                            Toast.makeText(getActivity(), "请设置支付密码", Toast.LENGTH_SHORT).show();
                            bundle.putString("type", "安全设置");
                            UserCenterActivity.instance.go_ucenter_safe(bundle);
                        }


                        break;
                    case "cancel":
                        String orderId = (String) CMD.get("id");
                        doCancel(orderId);
                        break;
                    case "error":
                        Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    };

    private void doCancel(String id) {
        NetParams params = new NetParams("/api/goodsOrder/cancel");
        params.addBodyParameter("id",id);
        x.http().get(params, new Callback.CommonCallback<JSONObject>() {

            @Override
            public void onCancelled(CancelledException arg0) {
                System.out.print("dsy");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(isFirst){
                    Loading.hideProgress(avloadingIndicatorView_BallClipRotatePulse);
                    isFirst=false;
                }
                Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFinished() {
                mAuthTask = true;
            }

            @Override
            public void onSuccess(JSONObject arg0) {
                data = GsonUtil.parseJsonWithGson(arg0.toString(), UcenterOrderTypeFragmentModel.class);
                String code = data.getCode();
                if (code.equals("1") ) {
                    Toast.makeText(getActivity(), "订单删除成功", Toast.LENGTH_SHORT).show();
                    getOrderData(pageType);
                } else if (code.equals("3")) {
                    Toast.makeText(getActivity(), "登录过期,请重新登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("flag", false);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "数据错误", Toast.LENGTH_SHORT).show();
                }
                if(isFirst){
                    Loading.hideProgress(avloadingIndicatorView_BallClipRotatePulse);
                    isFirst=false;
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    private void Pay(String id,String payPassword){
        NetParams params = new NetParams("/api/goodsOrder/pay");
        params.addBodyParameter("id",id);
        params.addBodyParameter("payPassword",payPassword);
        x.http().get(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onCancelled(CancelledException arg0) {
                System.out.print("dsy");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinished() {
                mAuthTask = true;
            }

            @Override
            public void onSuccess(JSONObject arg0) {
                orderReturn = GsonUtil.parseJsonWithGson(arg0.toString(), OrderReturn.class);
                String code = orderReturn.getCode();
                String msg=orderReturn.getMsg();
                if (code.equals("1") ) {
                    Toast.makeText(getActivity(), "支付成功,请到待评价页面查看", Toast.LENGTH_SHORT).show();
                    getOrderData(pageType);
                } else if (code.equals("3")) {
                    Toast.makeText(getActivity(), "登录过期,请重新登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("flag", false);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        IntentFilter filter=new IntentFilter("cqnu.com.housekeeping.Fragment.UcenterOrderTypeFragment");
        getContext().registerReceiver(UcenterOrderTypeFragmentReceiver, filter);
        rootView=inflater.inflate(R.layout.fragment_announcement, container, false);
        // Inflate the layout for this fragment
        bundle=getArguments();
        pageType= bundle.getString("type");
        TextView universal_title=(TextView)rootView.findViewById(R.id.universal_title);
        universal_title.setText(pageType);

        swipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        avloadingIndicatorView_BallClipRotatePulse=(AVLoadingIndicatorView)rootView.findViewById(R.id.avloadingIndicatorView_BallClipRotatePulse);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        //swipeRefreshLayout.setProgressBackgroundColor(R.color.white);
        //swipeRefreshLayout.setPadding(20, 20, 20, 20);
        //swipeRefreshLayout.setProgressViewOffset(true, 100, 200);
        //swipeRefreshLayout.setDistanceToTriggerSync(50);
        swipeRefreshLayout.setProgressViewEndTarget(true, 100);
        listView=(ListView)rootView.findViewById(R.id.listView);
        adapter=new UcenterOrderTypeFragmentAdapter(getContext(),list);
        listView.setAdapter(adapter);
        getOrderData(pageType);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrderData(pageType);
            }
        });
        return rootView;
    }


    private void getOrderData(String type){
        String url="/api/goodsOrder/getUserOrderByState";
        NetParams params = new NetParams(url);
        switch (type){
            case "待付款":
                params.addBodyParameter("state","0");
                getData(params);
                break;
            case "待确认":
                params.addBodyParameter("state","2");
                getData(params);
                break;
            case "全部订单":
                String urlall="/api/goodsOrder/getUserOrder";
                NetParams paramsall = new NetParams(urlall);
                getData(paramsall);
                break;
            case "待评价":
                params.addBodyParameter("state","3");
                getData(params);
                break;
            case "投诉/退款":
                getData(params);
                break;
        }
    }

    private void getData(NetParams params){

        x.http().get(params, new Callback.CommonCallback<JSONObject>() {

            @Override
            public void onCancelled(CancelledException arg0) {
                System.out.print("dsy");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(isFirst){
                    Loading.hideProgress(avloadingIndicatorView_BallClipRotatePulse);
                    isFirst=false;
                }
                Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFinished() {
                mAuthTask = true;
            }

            @Override
            public void onSuccess(JSONObject arg0) {
                data = GsonUtil.parseJsonWithGson(arg0.toString(), UcenterOrderTypeFragmentModel.class);
                String code = data.getCode();
                if (code.equals("1") && data.getGoods() != null) {
                    list.clear();
                    list.addAll(data.getGoods());
                    adapter.notifyDataSetChanged();
                } else if (code.equals("3")) {
                    Toast.makeText(getActivity(), "登录过期,请重新登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("flag", false);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "数据错误", Toast.LENGTH_SHORT).show();
                }
                if(isFirst){
                    Loading.hideProgress(avloadingIndicatorView_BallClipRotatePulse);
                    isFirst=false;
                }

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        getContext().unregisterReceiver(UcenterOrderTypeFragmentReceiver);
    }

}
