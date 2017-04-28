package cqnu.com.housekeeping.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cqnu.com.housekeeping.Fragment.OrderDetailFragment;
import cqnu.com.housekeeping.Fragment.UcenterIndexFragment;
import cqnu.com.housekeeping.Fragment.UcenterOrderTypeFragment;
import cqnu.com.housekeeping.Fragment.UcenterRechargeFragment;
import cqnu.com.housekeeping.Fragment.UcenterSafeFragment;
import cqnu.com.housekeeping.R;
import cqnu.com.housekeeping.Utils.JudgeUtil;


public class UserCenterActivity extends AppCompatActivity{
    public static Fragment currentfragment;// 当前正在显示的Fragment
    public static UserCenterActivity instance;
    private List<Fragment> list = new ArrayList<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        instance=this;

        initStatusBar();
        initIndex();
    }
    public void initIndex(){
        Bundle bundle =new Bundle();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        Fragment fragment = new UcenterIndexFragment();
        bundle.putString("type", "用户中心");
        fragment.setArguments(bundle);
        if(currentfragment!=null){
            fragmentTransaction.hide(currentfragment);
        }
        fragmentTransaction.add(R.id.index_full, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void initStatusBar(){
        Window window = this.getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // window.setStatusBarColor(statusColor);

        ViewGroup mContentView = (ViewGroup)findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {

            ViewCompat.setFitsSystemWindows(mChildView, false);
        }
    }

    public void go_ucenter_safe(Bundle bundle) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        Fragment fragment = new UcenterSafeFragment();
        fragment.setArguments(bundle);
        if(currentfragment!=null){
            fragmentTransaction.hide(currentfragment);
        }
        fragmentTransaction.add(R.id.index_full, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void go_order_type(Bundle bundle) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        Fragment fragment = new UcenterOrderTypeFragment();
        fragment.setArguments(bundle);
        if(currentfragment!=null){
            fragmentTransaction.hide(currentfragment);
        }
        fragmentTransaction.add(R.id.index_full, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void go_ucenter_recharge(Bundle bundle){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        Fragment fragment = new UcenterRechargeFragment();
        fragment.setArguments(bundle);
        if(currentfragment!=null){
            fragmentTransaction.hide(currentfragment);
        }
        fragmentTransaction.add(R.id.index_full, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            finish();
        } else{
            if (count == 1) {
                finish();
            } else {
                int length = list.size();
                list = list.subList(0, length - 1);
                length = list.size();
                currentfragment = list.get(length - 1);
            }
            getSupportFragmentManager().popBackStack();
        }
    }
    @Override
    public void onAttachFragment(Fragment fragment) {
        // TODO Auto-generated method stub
        currentfragment = fragment;
        list.add(fragment);
        super.onAttachFragment(fragment);
    }
}
