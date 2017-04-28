package cqnu.com.housekeeping.Fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.util.Map;

import cqnu.com.housekeeping.Activity.LoginActivity;
import cqnu.com.housekeeping.Activity.UserCenterActivity;
import cqnu.com.housekeeping.Adapter.UcenterOrderTypeFragment.UcenterOrderTypeFragmentModel;
import cqnu.com.housekeeping.Adapter.UcenterSafeFragment.Bean;
import cqnu.com.housekeeping.Adapter.UcenterSafeFragment.UcenterSafeAdapter;
import cqnu.com.housekeeping.Design.loadingView.Loading;
import cqnu.com.housekeeping.Http.NetParams;
import cqnu.com.housekeeping.Model.OrderReturn;
import cqnu.com.housekeeping.R;
import cqnu.com.housekeeping.Utils.GsonUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class UcenterSafeFragment extends Fragment {
    private View rootView,dialogView,dialogView2;
    private Bundle bundle;
    private AVLoadingIndicatorView avloadingIndicatorView_BallClipRotatePulse;
    private ListView listView;
    private UcenterSafeAdapter adapter;
    List<Bean> list=new ArrayList<>();
    private AlertDialog.Builder builder,builder2;

    private EditText editText1,editText2,editText1_1,editText2_1;
    private int type=0;
    public UcenterSafeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_announcement, container, false);

        // Inflate the layout for this fragment
        bundle=getArguments();
        avloadingIndicatorView_BallClipRotatePulse=(AVLoadingIndicatorView)rootView.findViewById(R.id.avloadingIndicatorView_BallClipRotatePulse);
        Loading.hideProgress(avloadingIndicatorView_BallClipRotatePulse);
        TextView universal_title=(TextView)rootView.findViewById(R.id.universal_title);
        universal_title.setText(bundle.getString("type"));
        listView=(ListView)rootView.findViewById(R.id.listView);
        adapter=new UcenterSafeAdapter(getContext(),list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 0:
                        Dialog dialog0;
                        dialogView=inflater.inflate(R.layout.dialog_password_item, container, false);
                        builder.setView(dialogView);
                        editText1=(EditText)dialogView.findViewById(R.id.editText1);
                        editText2=(EditText)dialogView.findViewById(R.id.editText2);
                        TextView t=(TextView)dialogView.findViewById(R.id.text);
                        t.setText("将登录密码修改为");
                        dialog0 = builder.show();
                        type=0;
                        break;
                    case 1:
                        Dialog dialog1;
                        dialogView2=inflater.inflate(R.layout.dialog_password_item, container, false);
                        builder.setView(dialogView2);
                        editText1_1=(EditText)dialogView2.findViewById(R.id.editText1);
                        editText2_1=(EditText)dialogView2.findViewById(R.id.editText2);
                        TextView t2=(TextView)dialogView2.findViewById(R.id.text);
                        t2.setText("将支付密码修改为");
                        dialog1 = builder.show();
                        type=1;
                        break;
                    case 2:
                        Dialog dialog2;
                        builder2 = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        builder2.setTitle("退出登录？");
                        builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                clearUserInfo();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.putExtra("flag", true);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        });
                        builder2.setNegativeButton("取消", null);
                        dialog2 = builder2.show();
                        break;
                }
            }
        });
        init();
        return rootView;
    }
    public void init(){
        builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String p="";
                String p2="";
                if(type==0){
                    p= editText1.getText().toString();
                   p2= editText2.getText().toString();
                }
                if(type==1){
                    p= editText1_1.getText().toString();
                   p2= editText2_1.getText().toString();
                }

                if(!TextUtils.isEmpty(p)&&!TextUtils.isEmpty(p2)){
                    resetPassword(p,p2,type);
                }else{
                    Toast.makeText(getActivity(), "不输入完整我怎么帮你改嘛", Toast.LENGTH_SHORT).show();
                }

            }
        });
        Bean resetpassword=new Bean();
        resetpassword.setTitle("设置登录密码");
        list.add(resetpassword);
        Bean resetpay=new Bean();
        resetpay.setTitle("设置支付密码");
        list.add(resetpay);
        Bean quite=new Bean();
        quite.setTitle("退出登录");
        list.add(quite);
        adapter.notifyDataSetChanged();
    }

    public void resetPassword(String p,String p2,int type){
        String url="";
        if(type==0){
            url="/api/user/fixPassword";
        }
        if(type==1){
            url="/api/user/fixPaypassword";
        }
        if(!url.equals("")){
            NetParams params = new NetParams(url);
            params.addBodyParameter("loginpassword",p);
            params.addBodyParameter("password",p2);
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
                    OrderReturn data = GsonUtil.parseJsonWithGson(arg0.toString(), OrderReturn.class);
                    String code = data.getCode();
                    if (code.equals("1")){
                        Toast.makeText(getActivity(), "密码修改成功", Toast.LENGTH_SHORT).show();
                    } else if (code.equals("3")) {
                        Toast.makeText(getActivity(), "登录过期,请重新登录", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.putExtra("flag", false);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), data.getMsg() , Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
    public void clearUserInfo(){
        SharedPreferences preferences = getActivity().getSharedPreferences("user",
                Context.MODE_PRIVATE);
        Map paraMap = new HashMap();
        String user_id = preferences.getString("user_id", "");
        if (user_id != null && !user_id.equals("")) {
            paraMap.put("user_id", user_id);
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
