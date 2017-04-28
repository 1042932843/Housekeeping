package cqnu.com.housekeeping.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jauker.widget.BadgeView;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import cqnu.com.housekeeping.Activity.LoginActivity;
import cqnu.com.housekeeping.Activity.MainActivity;
import cqnu.com.housekeeping.Activity.UniversalActivity;
import cqnu.com.housekeeping.Activity.UserCenterActivity;
import cqnu.com.housekeeping.Application.dsyApplication;
import cqnu.com.housekeeping.Design.loadingView.Loading;
import cqnu.com.housekeeping.Http.NetParams;
import cqnu.com.housekeeping.Model.OrderTypeSum;
import cqnu.com.housekeeping.Model.UserModel;
import cqnu.com.housekeeping.R;
import cqnu.com.housekeeping.Utils.GsonUtil;
import cqnu.com.housekeeping.Utils.StringUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class UcenterIndexFragment extends Fragment {
    private View rootView;
    private Bundle bundle;
    private LinearLayout address,massage,safe;
    private boolean mAuthTask = true;
    private TextView money,name;
    private OrderTypeSum orderTypeSum;
    private UserModel userModel;
    private ImageView imageView1,imageView2,imageView3,imageView4,user_img;
    private BadgeView badgeView1,badgeView2,badgeView3,badgeView4;
    private LinearLayout linearLayout0,linearLayout1,linearLayout2,linearLayout3,linearLayout4,ucenter_chongzhi;
    public UcenterIndexFragment() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();

    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
        }
        else{
            getUserInfo();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.activity_user_center_index, container, false);
        // Inflate the layout for this fragment
        bundle=getArguments();
        imageView1=(ImageView)rootView.findViewById(R.id.my_order_imageView1);
        imageView2=(ImageView)rootView.findViewById(R.id.my_order_imageView2);
        imageView3=(ImageView)rootView.findViewById(R.id.my_order_imageView3);
        imageView4=(ImageView)rootView.findViewById(R.id.my_order_imageView4);
        linearLayout0=(LinearLayout)rootView.findViewById(R.id.uc_0);
        linearLayout1=(LinearLayout)rootView.findViewById(R.id.uc_1);
        linearLayout2=(LinearLayout)rootView.findViewById(R.id.uc_2);
        linearLayout3=(LinearLayout)rootView.findViewById(R.id.uc_3);
        linearLayout4=(LinearLayout)rootView.findViewById(R.id.uc_4);
        ucenter_chongzhi=(LinearLayout)rootView.findViewById(R.id.ucenter_chongzhi);
        money=(TextView)rootView.findViewById(R.id.money);
        name=(TextView)rootView.findViewById(R.id.name);
        user_img=(ImageView)rootView.findViewById(R.id.user_img);
        badgeView1=new BadgeView(getContext());
        badgeView1.setTargetView(imageView1);
        badgeView1.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
        badgeView2=new BadgeView(getContext());
        badgeView2.setTargetView(imageView2);
        badgeView2.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
        badgeView3=new BadgeView(getContext());
        badgeView3.setTargetView(imageView3);
        badgeView3.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
        badgeView4=new BadgeView(getContext());
        badgeView4.setTargetView(imageView4);
        badgeView4.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
        address=(LinearLayout)rootView.findViewById(R.id.ucenter_address);
        massage=(LinearLayout)rootView.findViewById(R.id.ucenter_massage);
        safe=(LinearLayout)rootView.findViewById(R.id.ucenter_safe);
        setListener();
        return rootView;
    }
    private void setBadge(BadgeView badgeView,int num){
        if(num>=0){
            badgeView.setBadgeCount(num);
        }else{
            badgeView.setBadgeCount(0);
        }
    }
    private void setListener(){
        linearLayout0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type","全部订单");
                UserCenterActivity.instance.go_order_type(bundle);
            }
        });
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type","待付款");
                UserCenterActivity.instance.go_order_type(bundle);
            }
        });
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type","待确认");
                UserCenterActivity.instance.go_order_type(bundle);
            }
        });
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type","待评价");
                UserCenterActivity.instance.go_order_type(bundle);
            }
        });
        linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type","投诉/退款");
                UserCenterActivity.instance.go_order_type(bundle);
            }
        });
        safe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type", "安全设置");
                UserCenterActivity.instance.go_ucenter_safe(bundle);
            }
        });
        ucenter_chongzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type", "账户充值");
                bundle.putString("username",userModel.getUsername());
                bundle.putString("userid",userModel.getId());
                bundle.putString("money",userModel.getMoney());
                UserCenterActivity.instance.go_ucenter_recharge(bundle);
            }
        });
    }
    private void getUserInfo(){
        NetParams params = new NetParams( "/api/user/getInfo");
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
                userModel= GsonUtil.parseJsonWithGson(arg0.toString(),UserModel.class);
                String code = userModel.getCode();
                if (code.equals("1")) {
                    money.setText(userModel.getMoney());
                    name.setText(userModel.getUsername());
                    StringUtil.isSetPay=userModel.getIsSetPay();
                    dsyApplication.instance.displayImage(userModel.getImg(), user_img);
                    getAllSum();
                } else if (code.equals("3")) {
                    Toast.makeText(getActivity(), "登录过期,请重新登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("flag", false);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "数据请求失败", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void getAllSum(){
        mAuthTask=false;
        NetParams params = new NetParams( "/api/goodsOrder/getAllSum");
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
                    orderTypeSum= GsonUtil.parseJsonWithGson(arg0.toString(),OrderTypeSum.class);
                    String code = orderTypeSum.getCode();
                    if (code.equals("1")) {
                        setBadge(badgeView1,Integer.parseInt(orderTypeSum.getConfirm()));
                        setBadge(badgeView2,Integer.parseInt(orderTypeSum.getHaspay1()));
                        setBadge(badgeView3,Integer.parseInt(orderTypeSum.getHaspay2()));
                    } else if (code.equals("3")) {
                        Toast.makeText(getActivity(), "登录过期,请重新登录", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.putExtra("flag", false);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "数据请求失败", Toast.LENGTH_SHORT).show();
                    }

            }
        });
    }

}
