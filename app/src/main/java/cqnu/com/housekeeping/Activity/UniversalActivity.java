package cqnu.com.housekeeping.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import cqnu.com.housekeeping.Fragment.AnnouncementFragment;
import cqnu.com.housekeeping.Fragment.IWasRightFragment;
import cqnu.com.housekeeping.Fragment.OrderDetailFragment;
import cqnu.com.housekeeping.Fragment.SpeedCleanFragment;
import cqnu.com.housekeeping.R;

public class UniversalActivity extends AppCompatActivity {
    public static UniversalActivity instance=null;
    public static Fragment currentfragment;// 当前正在显示的Fragment
    private List<Fragment> list = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance=this;
        setContentView(R.layout.activity_universal);
        Intent intent=getIntent();
        String type=intent.getStringExtra("type");
        initData(type);
    }
    public void initData(String type){
        Bundle bundle = new Bundle();
        switch (type){
            case "极速洁":
                bundle.putString("type", type);
                go_SpeedCleanFragment(bundle);
                break;
            case "套餐商城":
               // bundle.putString("type", type);
               /// go_SpeedCleanFragment(bundle);
                break;
            case "选我就对":
                bundle.putString("type", type);
                go_IWasRight(bundle);
                break;
            case "麻麻说":
                bundle.putString("type", type);
                go_AnnouncementFragment(bundle);
                break;
        }
    }

    public void go_SpeedCleanFragment(Bundle bundle) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        Fragment fragment = new SpeedCleanFragment();
        fragment.setArguments(bundle);
        if(currentfragment!=null){
            fragmentTransaction.hide(currentfragment);
        }
        fragmentTransaction.add(R.id.index_full, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void go_IWasRight(Bundle bundle) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        Fragment fragment = new IWasRightFragment();
        fragment.setArguments(bundle);
        if(currentfragment!=null){
            fragmentTransaction.hide(currentfragment);
        }
        fragmentTransaction.add(R.id.index_full, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void go_AnnouncementFragment(Bundle bundle) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        Fragment fragment = new AnnouncementFragment();
        fragment.setArguments(bundle);
        if(currentfragment!=null){
            fragmentTransaction.hide(currentfragment);
        }
        fragmentTransaction.add(R.id.index_full, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void go_OrderDetailFragment(Bundle bundle) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        Fragment fragment = new OrderDetailFragment();
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
