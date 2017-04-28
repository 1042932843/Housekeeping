package cqnu.com.housekeeping.Fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.Arrays;

import cqnu.com.housekeeping.Activity.LoginActivity;
import cqnu.com.housekeeping.Activity.UserCenterActivity;
import cqnu.com.housekeeping.Application.dsyApplication;
import cqnu.com.housekeeping.Design.loadingView.Loading;
import cqnu.com.housekeeping.Design.wheelView.WheelView;
import cqnu.com.housekeeping.Http.NetParams;
import cqnu.com.housekeeping.Model.OrderReturn;
import cqnu.com.housekeeping.R;
import cqnu.com.housekeeping.Utils.GsonUtil;
import cqnu.com.housekeeping.Utils.JudgeUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailFragment extends Fragment {
    private View rootView;
    private Bundle bundle;
    private ImageView head;
    private AVLoadingIndicatorView avloadingIndicatorView_BallClipRotatePulse;
    private int time=1;
    private boolean mAuthTask = true;
    private OrderReturn data;
    private AlertDialog.Builder builder;
    private Dialog dialog;
    private Button submit;
    public OrderDetailFragment() {
        // Required empty public constructor
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
        }
        else{
            if(head!=null){
                dsyApplication.instance.displayImage(bundle.getString("img"), head);
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_order_detail, container, false);
        avloadingIndicatorView_BallClipRotatePulse=(AVLoadingIndicatorView)rootView.findViewById(R.id.avloadingIndicatorView_BallClipRotatePulse);
        Loading.hideProgress(avloadingIndicatorView_BallClipRotatePulse);
        bundle=getArguments();
        final String id=bundle.getString("id");
        TextView universal_title=(TextView)rootView.findViewById(R.id.universal_title);
        TextView name=(TextView)rootView.findViewById(R.id.name);
        TextView sex=(TextView)rootView.findViewById(R.id.sex);
        TextView age=(TextView)rootView.findViewById(R.id.age);
        TextView price=(TextView)rootView.findViewById(R.id.price);
        TextView address=(TextView)rootView.findViewById(R.id.address);
        TextView des=(TextView)rootView.findViewById(R.id.des);
        final TextView allprice=(TextView)rootView.findViewById(R.id.allprice);
        head=(ImageView)rootView.findViewById(R.id.order_head);
        submit=(Button)rootView.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitOrder(id);
            }
        });
        name.setText(bundle.getString("name"));
        sex.setText(bundle.getString("sex"));
        age.setText(bundle.getString("age"));
        price.setText(bundle.getString("price"));
        address.setText(bundle.getString("address"));
        universal_title.setText(bundle.getString("type"));
        des.setText(bundle.getString("des"));
        dsyApplication.instance.displayImage(bundle.getString("img"), head);

        allprice.setText(bundle.getString("price") + "元");
        WheelView wva = (WheelView)rootView.findViewById(R.id.main_wv);
        wva.setOffset(1);
        String[] PLANETS = {"一小时","两小时","三小时","四小时","五小时","六小时","七小时","八小时"};
        wva.setItems(Arrays.asList(PLANETS));
        wva.setHorizontalFadingEdgeEnabled(false);
        wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                if (bundle.getString("price") != null) {
                    double p = Double.valueOf(bundle.getString("price"));
                    allprice.setText((selectedIndex) * p + "元");
                    time = selectedIndex;
                }
                // Log.d(TAG, "selectedIndex: " + selectedIndex + ", item: " + item);
            }
        });


        return rootView;
    }

    private void submitOrder(String id) {
        Loading.showProgress(avloadingIndicatorView_BallClipRotatePulse);
        mAuthTask=false;
        NetParams params = new NetParams( "/api/goodsOrder/addByUser");
        params.addBodyParameter("sum",time+"");
        params.addBodyParameter("goods",id);
        if(!bundle.getString("sex").equals("")){
            params.addBodyParameter("orderType","personnel");
        }
        x.http().get(params, new Callback.CommonCallback<JSONObject>() {

            @Override
            public void onCancelled(CancelledException arg0) {
                System.out.print("dsy");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Loading.hideProgress(avloadingIndicatorView_BallClipRotatePulse);
                Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinished() {
                mAuthTask = true;
            }

            @Override
            public void onSuccess(JSONObject arg0) {
                data = GsonUtil.parseJsonWithGson(arg0.toString(), OrderReturn.class);
                String code = data.getCode();
                if (code.equals("1") ) {
                    String tel=data.getTel();
                    builder = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    builder.setTitle("预约成功！");
                    builder.setMessage("订单已生成，您可以前往用户中心订单管理相关页面查看，我们将在近期联系您，如果没有,请拨打订单中员工电话,联系后再确认支付!");
                    builder.setCancelable(false);
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(getActivity(),UserCenterActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    });
                    dialog=builder.show();
                } else if (code.equals("3")) {
                    Toast.makeText(getActivity(), "登录过期,请重新登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("flag", false);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "预约失败", Toast.LENGTH_SHORT).show();
                }
                Loading.hideProgress(avloadingIndicatorView_BallClipRotatePulse);
            }
        });
    }

}
