package cqnu.com.housekeeping.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;

import cqnu.com.housekeeping.Activity.LoginActivity;
import cqnu.com.housekeeping.Adapter.RechargeFragmentAdapter.RechargeFragmentAdapter;
import cqnu.com.housekeeping.Adapter.RechargeFragmentAdapter.RechargeFragmentModel;
import cqnu.com.housekeeping.Design.loadingView.Loading;
import cqnu.com.housekeeping.Http.NetParams;
import cqnu.com.housekeeping.Model.OrderReturn;
import cqnu.com.housekeeping.R;
import cqnu.com.housekeeping.Utils.GsonUtil;
import cqnu.com.housekeeping.Utils.StringUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class UcenterRechargeFragment extends Fragment {
    private View rootView;
    private Bundle bundle;
    private AVLoadingIndicatorView avloadingIndicatorView_BallClipRotatePulse;
    private RecyclerView recyclerView;
    private RechargeFragmentAdapter adapter;
    private Button tvPay;
    private TextView username;
    private EditText usermoney;
    public UcenterRechargeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_recharge, container, false);
        // Inflate the layout for this fragment
        bundle=getArguments();
        avloadingIndicatorView_BallClipRotatePulse=(AVLoadingIndicatorView)rootView.findViewById(R.id.avloadingIndicatorView_BallClipRotatePulse);
        Loading.hideProgress(avloadingIndicatorView_BallClipRotatePulse);
        TextView universal_title=(TextView)rootView.findViewById(R.id.universal_title);
        universal_title.setText(bundle.getString("type"));
        username=(TextView)rootView.findViewById(R.id.username);
        username.setText(bundle.getString("username"));
        usermoney=(EditText)rootView.findViewById(R.id.usermoney);
        usermoney.setText(bundle.getString("money"));
        usermoney.setEnabled(false);
        final String id=bundle.getString("userid");
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recylerview);
        tvPay = (Button)rootView.findViewById(R.id.tvPay);
        tvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(StringUtil.reCharge)<100){
                    Toast.makeText(getActivity(), "你妈说你怎么也得充100以上才行", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), StringUtil.reCharge, Toast.LENGTH_SHORT).show();
                    doRecharge(id,StringUtil.reCharge);
                }

            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter = new RechargeFragmentAdapter());
        adapter.replaceAll(getData());
        return rootView;
    }
    public ArrayList<RechargeFragmentModel> getData() {
        ArrayList<RechargeFragmentModel> list = new ArrayList<>();
        for (int i = 100; i < 900; i=i+100) {
            String count = i+"";
            list.add(new RechargeFragmentModel(RechargeFragmentModel.ONE, count));
        }
        list.add(new RechargeFragmentModel(RechargeFragmentModel.TWO, null));

        return list;
    }

    private void doRecharge(String id,String money){
        NetParams params = new NetParams("/api/user/addMoney");
        params.addBodyParameter("payId",id);
        params.addBodyParameter("money",money);
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
            }

            @Override
            public void onSuccess(JSONObject arg0) {
                OrderReturn  orderReturn = GsonUtil.parseJsonWithGson(arg0.toString(), OrderReturn.class);
                String code = orderReturn.getCode();
                String msg = orderReturn.getMsg();
                if (code.equals("1")) {
                    Toast.makeText(getActivity(), "充值成功", Toast.LENGTH_SHORT).show();
                    usermoney.setText(orderReturn.getMoney());
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
}
