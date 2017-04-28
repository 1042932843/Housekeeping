package cqnu.com.housekeeping.Activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.tiancaicc.springfloatingactionmenu.MenuItemView;
import com.tiancaicc.springfloatingactionmenu.OnMenuActionListener;
import com.tiancaicc.springfloatingactionmenu.SpringFloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import cqnu.com.housekeeping.Design.cycleView.ADInfo;
import cqnu.com.housekeeping.Design.cycleView.CycleViewPager;
import cqnu.com.housekeeping.Design.cycleView.Initialize;
import cqnu.com.housekeeping.Design.myRecyclerView.FullyGridLayoutManager;
import cqnu.com.housekeeping.Design.myRecyclerView.HomeWaterFallAdapter;
import cqnu.com.housekeeping.Design.vsView.AdverNotice;
import cqnu.com.housekeeping.Design.vsView.JDAdverView;
import cqnu.com.housekeeping.Design.vsView.JDViewAdapter;
import cqnu.com.housekeeping.Model.Advertisement;
import cqnu.com.housekeeping.Model.AdvertisementItem;
import cqnu.com.housekeeping.Model.ClassifyItem;
import cqnu.com.housekeeping.R;
import cqnu.com.housekeeping.Utils.JudgeUtil;

public class MainActivity extends FragmentActivity implements View.OnClickListener{
    private int frameDuration = 20;
    private AnimationDrawable frameAnim;
    private AnimationDrawable frameReverseAnim;
    private static int[] frameAnimRes = new int[]{
            R.mipmap.compose_anim_1,
            R.mipmap.compose_anim_2,
            R.mipmap.compose_anim_3,
            R.mipmap.compose_anim_4,
            R.mipmap.compose_anim_5,
            R.mipmap.compose_anim_6,
            R.mipmap.compose_anim_7,
            R.mipmap.compose_anim_8,
            R.mipmap.compose_anim_9,
            R.mipmap.compose_anim_10,
            R.mipmap.compose_anim_11,
            R.mipmap.compose_anim_12,
            R.mipmap.compose_anim_13,
            R.mipmap.compose_anim_14,
            R.mipmap.compose_anim_15,
            R.mipmap.compose_anim_15,
            R.mipmap.compose_anim_16,
            R.mipmap.compose_anim_17,
            R.mipmap.compose_anim_18,
            R.mipmap.compose_anim_19
    };
    private CycleViewPager cycleViewPager;
    private Initialize ia;
    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener;
    private JDAdverView tbView;
    private JDViewAdapter jdadapter;
    private List<AdverNotice> jd_datas=new ArrayList<>();
    private RecyclerView rv;
    private HomeWaterFallAdapter homeWaterFallAdapter;
    private List<ClassifyItem> Data=new ArrayList<>();
    private FloatingActionButton fab;
    private OnMenuActionListener ol;
    private SpringFloatingActionMenu springFloatingActionMenu;
    private List<AdvertisementItem> adlist=new ArrayList<>();
    @Override
    public void onClick(View v) {
        MenuItemView menuItemView = (MenuItemView) v;
        // Toast.makeText(this,menuItemView.getLabelTextView().getText(),Toast.LENGTH_SHORT).show();
        String where=menuItemView.getLabelTextView().getText().toString();
        switch (where){
            case "极速洁":
                go_universal(where);
                break;
            case "套餐商城":
                go_universal(where);
                break;
            case "选我就对":
                go_universal(where);
                break;
            case "用户中心":
                if(JudgeUtil.isLogin(this)){
                    go_usercenter();
                }else{
                    go_login();
                }
                break;
            case "麻麻说":
                go_universal(where);
                break;
            case "设置":
                go_setting();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initStatusBar();
        initfloatball();
        initVsAdvertisement();
        initRecyclerView();

    }
    private void initStatusBar(){
        Window window = this.getWindow();
       //设置透明状态栏,这样才能让 ContentView 向上
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
       //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
       //设置状态栏颜色
       // window.setStatusBarColor(statusColor);

        ViewGroup mContentView = (ViewGroup)findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统 View 预留空间.
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }
    }
    private void initCarousel(){
        Intent it=getIntent();
        Advertisement ad=(Advertisement)it.getSerializableExtra("data");
        adlist.clear();
        adlist.addAll(ad.getAd());
        cycleViewPager = (CycleViewPager) getSupportFragmentManager().findFragmentById(R.id.fragment_cycle_viewpager_content);
        mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {
            @Override
            public void onImageClick(ADInfo info, int position, View imageView) {
                if (cycleViewPager.isCycle()) {
                    position = position - 1;
                }
                Toast.makeText(MainActivity.this, "点击了"+info.getUrl(), Toast.LENGTH_SHORT).show();

            }

        };
        if (ia != null) {
            ia.initialize(cycleViewPager, adlist, mAdCycleViewListener, this);

        }else{
            ia=new Initialize();
            ia.initialize(cycleViewPager, adlist, mAdCycleViewListener, this);

        }
    }

    private void initfloatball(){
        createFabFrameAnim();
        createFabReverseFrameAnim();
        //必须手动创建FAB, 并设置属性
        fab = new FloatingActionButton(this);
        fab.setType(FloatingActionButton.TYPE_NORMAL);
        fab.setImageResource(R.mipmap.compose_anim_1);
        fab.setColorPressedResId(R.color.fab);
        fab.setColorNormalResId(R.color.fab);
        fab.setColorRippleResId(R.color.translucent_background);
        fab.setShadow(true);
        ol =new OnMenuActionListener() {
            @Override
            public void onMenuOpen() {
                //设置FAB的icon当菜单打开的时候
                fab.setImageDrawable(frameAnim);
                frameReverseAnim.stop();
                frameAnim.start();
            }

            @Override
            public void onMenuClose() {
                //设置回FAB的图标当菜单关闭的时候
                fab.setImageDrawable(frameReverseAnim);
                frameAnim.stop();
                frameReverseAnim.start();
            }
        };
        springFloatingActionMenu= new SpringFloatingActionMenu.Builder(this)
                .fab(fab)
                        //添加菜单按钮参数依次是背景颜色,图标,标签,标签的颜色,点击事件
                .addMenuItem(R.color.photo, R.mipmap.ic_messaging_posttype_chat, this.getResources().getString(R.string.fab_3), R.color.photo,this)
                .addMenuItem(R.color.chat, R.mipmap.ic_messaging_posttype_clean, this.getResources().getString(R.string.fab_1), R.color.chat,this)
                .addMenuItem(R.color.quote, R.mipmap.ic_messaging_posttype_shop, this.getResources().getString(R.string.fab_2), R.color.quote,this)
                .addMenuItem(R.color.link, R.mipmap.ic_messaging_posttype_right, this.getResources().getString(R.string.fab_4), R.color.link,this)
                .addMenuItem(R.color.audio, R.mipmap.ic_messaging_posttype_user, this.getResources().getString(R.string.fab_5), R.color.audio,this)
                .addMenuItem(R.color.text, R.mipmap.ic_messaging_posttype_install, this.getResources().getString(R.string.set), R.color.text, this)
               // .addMenuItem(R.color.video, R.mipmap.ic_messaging_posttype_video, this.getResources().getString(R.string.more), R.color.video,this)
                        //设置动画类型
                .animationType(SpringFloatingActionMenu.ANIMATION_TYPE_TUMBLR)
                        //设置reveal效果的颜色
                .revealColor(R.color.white)
                        //设置FAB的位置,只支持底部居中和右下角的位置
                .gravity(Gravity.CENTER | Gravity.BOTTOM)
                .onMenuActionListner(ol)
                .build();
    }
    private void initVsAdvertisement(){
        tbView = (JDAdverView)findViewById(R.id.jdadver);
        tbView.setVisibility(View.GONE);
       // initVsData();
       // jdadapter = new JDViewAdapter(jd_datas);
       // tbView.setAdapter(jdadapter);
      //  //开启线程
      //  tbView.start();
    }
    private void initVsData() {
        jd_datas.add(new AdverNotice("2016不听不得行超级音乐集", "http://cfssx.img48.wal8.com/img48/557515_20160920155917/147460012023.png", "不听你就输了 <-输尼马币"));
        jd_datas.add(new AdverNotice("听我想听", "http://cfssx.img48.wal8.com/img48/557515_20160920155917/147460005756.jpg", "蒋哥大煞笔"));
        jd_datas.add(new AdverNotice("跑马的蒋哥你威武雄壮", "http://cfssx.img48.wal8.com/img48/557515_20160920155917/147460002498.jpg", "dsy测试版"));
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initRecyclerView(){
        rv = (RecyclerView)findViewById(R.id.recyclerView);
        rv.setNestedScrollingEnabled(false);
        rv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        FullyGridLayoutManager layoutManager = new FullyGridLayoutManager(this,2);
        layoutManager.setOrientation(FullyGridLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);

        for(int i=0;i<6;i++){
            ClassifyItem ci = new ClassifyItem();
            ci.setImg("http://cfssx.img48.wal8.com/img48/557515_20160920155917/147460002498.jpg");
            ci.setTitle("测试数据"+i);
            ci.setContext("内容简介"+i);
            Data.add(ci);
        }
        homeWaterFallAdapter=new HomeWaterFallAdapter(this,Data);

        homeWaterFallAdapter.setItemClickListener(new HomeWaterFallAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, String tag) {
               // Toast.makeText(MainActivity.this, "点击了" + tag, Toast.LENGTH_SHORT).show();
                if(tag!=""){
                    go_web(tag);
                }
            }
        });
        rv.setAdapter(homeWaterFallAdapter);
    }

    public void go_setting(){
        Intent intent=new Intent(this,SettingsActivity.class);
        startActivity(intent);
    }
    public void go_usercenter() {
        Intent intent=new Intent(this,UserCenterActivity.class);
        startActivity(intent);
    }
    public void go_universal(String type){
        Intent intent=new Intent(this,UniversalActivity.class);
        intent.putExtra("type",type);
        startActivity(intent);
    }
    public void go_login(){
        Intent intent=new Intent(this,LoginActivity.class);
        intent.putExtra("flag",true);
        startActivity(intent);
    }
    public void go_web(String context){
        Intent intent=new Intent(this,WebActivity.class);
        intent.putExtra("context",context);
        startActivity(intent);
    }
    private void createFabFrameAnim() {
        frameAnim = new AnimationDrawable();
        frameAnim.setOneShot(true);
        Resources resources = getResources();
        for (int res : frameAnimRes) {
            frameAnim.addFrame(resources.getDrawable(res), frameDuration);
        }
    }

    private void createFabReverseFrameAnim() {
        frameReverseAnim = new AnimationDrawable();
        frameReverseAnim.setOneShot(true);
        Resources resources = getResources();
        for (int i = frameAnimRes.length - 1; i >= 0; i--) {
            frameReverseAnim.addFrame(resources.getDrawable(frameAnimRes[i]), frameDuration);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initCarousel();

    }
    @Override
    public void onStart() {
        super.onStart();


    }
    public void closeFloatBall(){
        if(springFloatingActionMenu!=null){
            if(springFloatingActionMenu.isMenuOpen()){
                springFloatingActionMenu.hideMenu();
            }
        }
    }

    private long exitTime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(springFloatingActionMenu!=null){
            if(springFloatingActionMenu.isMenuOpen()){
                springFloatingActionMenu.hideMenu();
                return true;
            }
        }
         if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (System.currentTimeMillis() - exitTime > 2000) {
                    Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                    System.exit(0);
                }
                return true;
            }

        return super.onKeyDown(keyCode, event);
    }

}
