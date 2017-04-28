package com.tiancaicc.springfloatingactionmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.ui.Util;
import com.melnykov.fab.FloatingActionButton;
import com.tumblr.backboard.Actor;
import com.tumblr.backboard.MotionProperty;
import com.tumblr.backboard.imitator.Imitator;
import com.tumblr.backboard.imitator.SpringImitator;
import com.tumblr.backboard.performer.MapPerformer;
import com.tumblr.backboard.performer.Performer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;


/**
 * Copyright (C) 2016 tiancaiCC
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class SpringFloatingActionMenu extends FrameLayout implements ViewTreeObserver.OnGlobalLayoutListener {

    private static final String TAG = "SFAM";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ANIMATION_TYPE_BLOOM, ANIMATION_TYPE_TUMBLR})
    public @interface ANIMATION_TYPE {
    }

    public static final int ANIMATION_TYPE_BLOOM = 0;
    public static final int ANIMATION_TYPE_TUMBLR = 1;

    private Context mContext;

    private FloatingActionButton mFAB;

    private View mRevealCircle;

    private ArrayList<MenuItem> mMenuItems;

    private ArrayList<ImageButton> mFollowCircles;

    private ArrayList<MenuItemView> mMenuItemViews;

    private ViewGroup mContainerView;

    private ArrayList<OnMenuActionListener> mActionListeners;

    private OnFabClickListener mOnFabClickListener;

    @ColorRes
    private int mRevealColor;

    private int mMenuItemCount;

    private int mGravity;

    private int mMarginSize = 16;

    private boolean mMenuOpen = false;

    private int mAnimationType;

    private int mRevealDuration = 600;

    private int mTumblrTimeInterval = 70;

    // fix animation not stable caused by FAB click too fast
    private boolean mAnimating = false;

    private boolean mEnableFollowAnimation = true;

    public SpringFloatingActionMenu(Builder builder) {
        super(builder.context);
        this.mContext = builder.context;
        this.mMenuItems = builder.menuItems;
        this.mFAB = builder.fab;
        this.mMenuItemCount = builder.menuItems.size();
        this.mGravity = builder.gravity;
        this.mActionListeners = builder.actionListeners;
        this.mRevealColor = builder.revealColor;
        this.mAnimationType = builder.animationType;
        this.mOnFabClickListener = builder.onFabClickListener;
        this.mEnableFollowAnimation = builder.enableFollowAnimation;
        init(mContext);
    }

    public SpringFloatingActionMenu(Context context) {
        this(context, null, 0);
    }

    public SpringFloatingActionMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpringFloatingActionMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {

        this.mContext = context;
        // this mContainerView will be added when animation happened,
        // see DestroySelfSpringListener.onSpringActivate()
        mContainerView = new FrameLayout(context);
        //add reveal circle, it will at bottom position
        mContainerView.addView(mRevealCircle = generateRevealCircle());

        //generate and add follow circles
        if (mEnableFollowAnimation) {
            mFollowCircles = generateFollowCircles();
            for (int i = mFollowCircles.size() - 1; i >= 0; i--) {
                // note follow circles is not added in container view, is just added in this SpringFloatingActionMenu
                addView(mFollowCircles.get(i));
            }
        }

        //generate and add menuItemViews
        mMenuItemViews = generateMenuItemViews();
        for (MenuItemView menuItemView : mMenuItemViews) {
            mContainerView.addView(menuItemView);
            addOnMenuActionListener(menuItemView);
        }
        mMenuItemViews.get(0).bringToFront();

//        addView(mContainerView);
        //add FAB
        LayoutParams fablp = Utils.createWrapParams();
        fablp.gravity = mGravity;
        addView(mFAB, fablp);


        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //add self to root view
        ViewGroup rootView = (ViewGroup) ((Activity) mContext).findViewById(android.R.id.content);
        rootView.addView(this);
        bringToFront();
        getViewTreeObserver().addOnGlobalLayoutListener(this);

        mFAB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnimating) {
                    return;
                }
                if (mOnFabClickListener != null) {
                    mOnFabClickListener.onClcik();
                }
                if (mMenuOpen) {
                    hideMenu();
                } else {
                    showMenu();
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        Resources resources = getResources();
        Activity context = (Activity) mContext;

        // layout FAB and follow circles
        int fabWidth = mFAB.getMeasuredWidth();
        int fabHeight = mFAB.getMeasuredHeight();
        int bottomInsets = Utils.getInsetsBottom(context, this);

        int fabX = 0;
        int fabY = 0;
        if (mGravity == (Gravity.BOTTOM | Gravity.RIGHT)) {
            int marginX = Utils.dpToPx(mMarginSize, resources);
            int marginY = Utils.dpToPx(mMarginSize, resources) + bottomInsets;
            fabX = right - fabWidth - marginX;
            fabY = bottom - fabHeight - marginY;
            mFAB.layout(fabX, fabY, fabX + fabWidth, fabY + fabHeight);
        } else if (mGravity == (Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM)) {
            int marginY = Utils.dpToPx(mMarginSize, resources) + bottomInsets;
            fabX = right / 2 - fabWidth / 2;
            fabY = bottom - fabHeight - marginY;
            mFAB.layout(fabX, fabY, fabX + fabWidth, fabY + fabHeight);
        } else {
            throw new IllegalStateException("gravity only support bottom center and bottom right");
        }


        int fabCenterX = fabX + fabWidth / 2;
        int fabCenterY = fabY + fabHeight / 2;

        for (ImageButton circle : mFollowCircles) {
            int x = fabCenterX - circle.getWidth() / 2;
            int y = fabCenterY - circle.getHeight() / 2;
            circle.layout(x, y, x + circle.getMeasuredWidth(), y + circle.getMeasuredHeight());
        }

        int x = fabCenterX - mRevealCircle.getWidth() / 2;
        int y = fabCenterY - mRevealCircle.getHeight() / 2;
        mRevealCircle.layout(x, y, x + mRevealCircle.getMeasuredWidth(), y + mRevealCircle.getMeasuredHeight());

        //layout menu items
        layoutMenuItems(left, top, right, bottom);
    }

    private void layoutMenuItems(int left, int top, int right, int bottom) {
        switch (mAnimationType) {
            case ANIMATION_TYPE_BLOOM:
                layoutMenuItemsAsGrid(left, top, right, bottom);
                break;
            case ANIMATION_TYPE_TUMBLR:
                layoutMenuItemsAsTumblr(left, top, right, bottom);
                break;

        }
    }

    private void layoutMenuItemsAsGrid(int left, int top, int right, int bottom) {
        Resources resources = getResources();

        int edgeGap = Util.dpToPx(24, resources);

        int colCount = 3;
        int rowCount = mMenuItemCount % colCount == 0 ? mMenuItemCount / colCount : mMenuItemCount / colCount + 1;
        Log.d(TAG, "row count:" + rowCount);

        int itemHeight = mMenuItemViews.get(0).getMeasuredHeight();
        int itemWidth = mMenuItemViews.get(0).getMeasuredWidth();

        int containerWidth = getMeasuredWidth();
        int containerHeight = getMeasuredHeight();

        int itemGap = (containerWidth - 2 * edgeGap - colCount * itemWidth) / (colCount - 1);
        //top and bottom gap
        int tbGap = (containerHeight - rowCount * itemHeight - itemGap * (rowCount - 1)) / 2;

        for (int rowIdx = 0; rowIdx < rowCount; rowIdx++) {
            for (int colIdx = 0; colIdx < colCount; colIdx++) {
                int idxInItem = rowIdx * colCount + colIdx;
                if (idxInItem < mMenuItemCount) {
                    View item = mMenuItemViews.get(rowIdx * colCount + colIdx);
                    Log.d(TAG, "menu item index:" + (rowIdx * colCount + colIdx));
                    int itemLeft = edgeGap + colIdx * itemWidth + colIdx * itemGap;
                    int itemTop = tbGap + rowIdx * itemGap + rowIdx * itemHeight;
                    item.layout(itemLeft, itemTop, itemLeft + itemWidth, itemTop + itemHeight);
                } else {
                    break;
                }
            }
        }
    }

    private void layoutMenuItemsAsTumblr(int left, int top, int right, int bottom) {
        if (mMenuItemCount < 4 || mMenuItemCount > 7)
            throw new IllegalStateException("layout as tumblr style must have morn than 4 items,less than 7 items");
        int itemHeight = mMenuItemViews.get(0).getMeasuredHeight();
        int itemWidth = mMenuItemViews.get(0).getMeasuredWidth();

        int itemDiameter = mMenuItemViews.get(0).getDiameter();
        int itemRadius = itemDiameter / 2;

        int ringRadius = (int) (itemDiameter * 1.6);

        int containerWidth = getMeasuredWidth();
        int containerHeight = getMeasuredHeight();

        int ringCenterX = containerWidth / 2;
        int ringCenterY = containerHeight / 2;

        //layout first item at container center
        int firstX = containerWidth / 2 - itemRadius;
        int firstY = containerHeight / 2 - itemRadius;
        View firstItem = mMenuItemViews.get(0);
        firstItem.layout(firstX, firstY, firstX + itemWidth, firstY + itemHeight);

        double arcunit = 2 * Math.PI / (mMenuItemCount - 1);

        for (int i = 0; i < mMenuItemCount - 1; i++) {
            View item = mMenuItemViews.get(i + 1);
            double arc = arcunit * i;
            int x = (int) (ringCenterX + ringRadius * Math.sin(arc) - itemRadius);
            int y = (int) (ringCenterY - ringRadius * Math.cos(arc) - itemRadius);
            item.layout(x, y, x + item.getMeasuredWidth(), y + item.getMeasuredHeight());
        }

    }

    private ArrayList<ImageButton> generateFollowCircles() {

        int diameter = mFAB.getType() == FloatingActionButton.TYPE_NORMAL ?
                Utils.getDimension(mContext, R.dimen.fab_size_normal) :
                Utils.getDimension(mContext, R.dimen.fab_size_mini);

        ArrayList<ImageButton> circles = new ArrayList<>(mMenuItems.size());
        for (MenuItem item : mMenuItems) {
            ImageButton circle = new ImageButton(mContext);
            OvalShape ovalShape = new OvalShape();
            ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
            shapeDrawable.getPaint().setColor(getResources().getColor(item.getBgColor()));
            circle.setBackgroundDrawable(shapeDrawable);
            circle.setImageResource(item.getIcon());
            LayoutParams lp = new LayoutParams(diameter, diameter);
            circle.setLayoutParams(lp);
            circles.add(circle);
        }

        return circles;
    }

    private ArrayList<MenuItemView> generateMenuItemViews() {
        ArrayList<MenuItemView> menuItemViews = new ArrayList<>(mMenuItems.size());
        for (MenuItem item : mMenuItems) {
            MenuItemView menuItemView = new MenuItemView(mContext, item);
            menuItemView.setLayoutParams(Utils.createWrapParams());
//            menuItemView.setOnClickListener(item.getOnClickListener());
            menuItemViews.add(menuItemView);
        }
        return menuItemViews;
    }

    private View generateRevealCircle() {
        int diameter = mFAB.getType() == FloatingActionButton.TYPE_NORMAL ?
                Utils.getDimension(mContext, R.dimen.fab_size_normal) :
                Utils.getDimension(mContext, R.dimen.fab_size_mini);
        View view = new View(mContext);
        OvalShape ovalShape = new OvalShape();
        ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
        shapeDrawable.getPaint().setColor(ContextCompat.getColor(mContext, mRevealColor));
        view.setBackgroundDrawable(shapeDrawable);
        LayoutParams lp = new LayoutParams(diameter, diameter);
        view.setLayoutParams(lp);

        // Make view clickable to avoid clicks on any view located behind the menu
        view.setClickable(true);

        // note it is invisible, but will be visible while  animating
        view.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void onGlobalLayout() {
        Log.d(TAG, "onGlobalLayout");
        if (mEnableFollowAnimation) {
            applyFollowAnimation();
        }
    }

    private void addOnMenuActionListener(OnMenuActionListener listener) {
        this.mActionListeners.add(listener);
    }

    public void showMenu() {
        Log.d(TAG, "showMenu");
        switch (mAnimationType) {
            case ANIMATION_TYPE_BLOOM:
                applyBloomOpenAnimation();
                break;
            case ANIMATION_TYPE_TUMBLR:
                applyTumblrOpenAniamtion();
                break;

        }
        hideFollowCircles();
        revealIn();
        for (OnMenuActionListener listener : mActionListeners) {
            listener.onMenuOpen();
        }
        mMenuOpen = !mMenuOpen;
    }

    public void hideMenu() {
        Log.d(TAG, "hideMenu");
        switch (mAnimationType) {
            case ANIMATION_TYPE_BLOOM:
                applyBloomCloseAnimation();
                break;
            case ANIMATION_TYPE_TUMBLR:
                applyTumblrCloseAnimation();
                break;

        }
        showFollowCircles();
        revealOut();
        for (OnMenuActionListener listener : mActionListeners) {
            listener.onMenuClose();
        }
        mMenuOpen = !mMenuOpen;
    }

    public boolean isMenuOpen() {
        return mMenuOpen;
    }

    private void hideFollowCircles() {
        for (View view : mFollowCircles) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    private void showFollowCircles() {
        for (View view : mFollowCircles) {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void revealIn() {
        mRevealCircle.setVisibility(View.VISIBLE);
        mRevealCircle.animate()
                .scaleX(100)
                .scaleY(100)
                .setDuration(mRevealDuration)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mAnimating = false;
                        animation.removeAllListeners();
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        mAnimating = true;
                    }
                })
                .start();
    }

    private void revealOut() {
        mRevealCircle.animate()
                .scaleX(1)
                .scaleY(1)
                .setDuration(mRevealDuration)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mRevealCircle.setVisibility(View.INVISIBLE);
                        animation.removeAllListeners();
                        mAnimating = false;
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        mAnimating = true;
                    }
                })
                .start();
    }

    private void fadeIn() {
        int colorFrom = Color.parseColor("#00000000");
        int colorTo = Color.parseColor("#40000000");
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }

    private void fadeOut() {
        int colorFrom = Color.parseColor("#40000000");
        int colorTo = Color.parseColor("#00000000");
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }


    private void applyFollowAnimation() {
        /* Animation code */

        final SpringSystem springSystem = SpringSystem.create();

        // create the springs that control movement
        final Spring springX = springSystem.createSpring();
        final Spring springY = springSystem.createSpring();

        // bind circle movement to events
        new Actor.Builder(springSystem, mFAB)
                .addMotion(springX, Imitator.TRACK_DELTA, Imitator.FOLLOW_EXACT, MotionProperty.X)
                .addMotion(springY, Imitator.TRACK_DELTA, Imitator.FOLLOW_EXACT, MotionProperty.Y)
                .build();

        // add springs to connect between the views
        final Spring[] followsX = new Spring[mMenuItemCount];
        final Spring[] followsY = new Spring[mMenuItemCount];

        for (int i = 0; i < mFollowCircles.size(); i++) {

            // create spring to bind views
            followsX[i] = springSystem.createSpring();
            followsY[i] = springSystem.createSpring();
            followsX[i].addListener(new Performer(mFollowCircles.get(i), View.TRANSLATION_X));
            followsY[i].addListener(new Performer(mFollowCircles.get(i), View.TRANSLATION_Y));

            // imitates another character
            final SpringImitator followX = new SpringImitator(followsX[i]);
            final SpringImitator followY = new SpringImitator(followsY[i]);

            //  imitate the previous character
            if (i == 0) {
                springX.addListener(followX);
                springY.addListener(followY);
            } else {
                followsX[i - 1].addListener(followX);
                followsY[i - 1].addListener(followY);
            }
        }
    }

    private void applyBloomOpenAnimation() {
        final SpringSystem springSystem = SpringSystem.create();

        for (int i = 0; i < mMenuItemCount; i++) {
            // create the springs that control movement
            final Spring springX = springSystem.createSpring();
            final Spring springY = springSystem.createSpring();

            MenuItemView menuItemView = mMenuItemViews.get(i);
            springX.addListener(new MapPerformer(menuItemView, View.X, mFAB.getLeft(), menuItemView.getLeft()));
            springY.addListener(new MapPerformer(menuItemView, View.Y, mFAB.getTop(), menuItemView.getTop()));
            DestroySelfSpringListener destroySelfSpringListener = new DestroySelfSpringListener(this,mContainerView,true);
            springX.addListener(destroySelfSpringListener);
            springY.addListener(destroySelfSpringListener);
            springX.setEndValue(1);
            springY.setEndValue(1);
        }
    }

    private void applyBloomCloseAnimation() {
        final SpringSystem springSystem = SpringSystem.create();

        for (int i = 0; i < mMenuItemCount; i++) {
            // create the springs that control movement
            final Spring springX = springSystem.createSpring();
            final Spring springY = springSystem.createSpring();

            MenuItemView menuItemView = mMenuItemViews.get(i);
            springX.addListener(new MapPerformer(menuItemView, View.X, menuItemView.getLeft(), mFAB.getLeft()));
            springY.addListener(new MapPerformer(menuItemView, View.Y, menuItemView.getTop(), mFAB.getTop()));
            DestroySelfSpringListener destroySelfSpringListener = new DestroySelfSpringListener(this,mContainerView,false);
            springX.addListener(destroySelfSpringListener);
            springY.addListener(destroySelfSpringListener);
            springX.setEndValue(1);
            springY.setEndValue(1);
        }
    }

    private void applyTumblrOpenAniamtion() {
        final View firstItem = mMenuItemViews.get(0);

        //make start position at center
        for (MenuItemView itemView : mMenuItemViews) {
            itemView.disableAlphaAnimation();
            itemView.setX(firstItem.getLeft());
            itemView.setY(firstItem.getY());
            itemView.setScaleX(0);
            itemView.setScaleY(0);
        }
        final SpringSystem springSystem = SpringSystem.create();

        final Spring springScaleX = springSystem.createSpring();
        final Spring springScaleY = springSystem.createSpring();

        springScaleX.addListener(new MapPerformer(firstItem, View.SCALE_X, 0, 1));
        springScaleY.addListener(new MapPerformer(firstItem, View.SCALE_Y, 0, 1));
        final DestroySelfSpringListener destroySelfSpringListener = new DestroySelfSpringListener(this,mContainerView,true);
        springScaleX.addListener(destroySelfSpringListener);
        springScaleY.addListener(destroySelfSpringListener);
        springScaleX.setEndValue(1);
        springScaleY.setEndValue(1);

        for (int i = 1; i < mMenuItemCount; i++) {
            final View menuItemView = mMenuItemViews.get(i);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    final Spring springScaleX = springSystem.createSpring();
                    final Spring springScaleY = springSystem.createSpring();

                    springScaleX.addListener(new MapPerformer(menuItemView, View.SCALE_X, 0, 1));
                    springScaleY.addListener(new MapPerformer(menuItemView, View.SCALE_Y, 0, 1));
                    final DestroySelfSpringListener destroySelfSpringListener = new DestroySelfSpringListener(SpringFloatingActionMenu.this,mContainerView,true);
                    springScaleX.addListener(destroySelfSpringListener);
                    springScaleY.addListener(destroySelfSpringListener);
                    springScaleX.setEndValue(1);
                    springScaleY.setEndValue(1);

                    final Spring springX = springSystem.createSpring();
                    final Spring springY = springSystem.createSpring();

                    springX.addListener(new MapPerformer(menuItemView, View.X, firstItem.getLeft(), menuItemView.getLeft()));
                    springY.addListener(new MapPerformer(menuItemView, View.Y, firstItem.getTop(), menuItemView.getTop()));
                    springX.addListener(destroySelfSpringListener);
                    springY.addListener(destroySelfSpringListener);
                    springX.setEndValue(1);
                    springY.setEndValue(1);
                }
            }, mTumblrTimeInterval * (i - 1));
        }
    }

    private void applyTumblrCloseAnimation() {

        final int alphaDuration = 130;

        final SpringSystem springSystem = SpringSystem.create();

        final View firstItem = mMenuItemViews.get(0);
        final Spring springScaleX = springSystem.createSpring();
        final Spring springScaleY = springSystem.createSpring();

        springScaleX.addListener(new MapPerformer(firstItem, View.SCALE_X, 1, 0));
        springScaleY.addListener(new MapPerformer(firstItem, View.SCALE_Y, 1, 0));
        final DestroySelfSpringListener destroySelfSpringListener = new DestroySelfSpringListener(this,mContainerView,false);
        springScaleX.addListener(destroySelfSpringListener);
        springScaleY.addListener(destroySelfSpringListener);
        springScaleX.setEndValue(1);
        springScaleY.setEndValue(1);

        firstItem.animate()
                .alpha(160);

        for (int i = mMenuItemCount - 1; i >= 1; i--) {
            final View menuItemView = mMenuItemViews.get(i);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    final Spring springScaleX = springSystem.createSpring();
                    final Spring springScaleY = springSystem.createSpring();

                    springScaleX.addListener(new MapPerformer(menuItemView, View.SCALE_X, 1, 0));
                    springScaleY.addListener(new MapPerformer(menuItemView, View.SCALE_Y, 1, 0));
                    final DestroySelfSpringListener destroySelfSpringListener = new DestroySelfSpringListener(SpringFloatingActionMenu.this,mContainerView,false);
                    springScaleX.addListener(destroySelfSpringListener);
                    springScaleY.addListener(destroySelfSpringListener);
                    springScaleX.setEndValue(1);
                    springScaleY.setEndValue(1);

                    final Spring springX = springSystem.createSpring();
                    final Spring springY = springSystem.createSpring();

                    springX.addListener(new MapPerformer(menuItemView, View.X, menuItemView.getLeft(), firstItem.getLeft()));
                    springY.addListener(new MapPerformer(menuItemView, View.Y, menuItemView.getTop(), firstItem.getTop()));
                    springX.addListener(destroySelfSpringListener);
                    springY.addListener(destroySelfSpringListener);
                    springX.setEndValue(1);
                    springY.setEndValue(1);

                    menuItemView.animate()
                            .alpha(0)
                            .setDuration(alphaDuration);
                }
            }, mTumblrTimeInterval * (mMenuItemCount - i - 1));
        }
    }

    public FloatingActionButton getFAB() {
        return mFAB;
    }

    public static class Builder {

        private Context context;

        private ArrayList<MenuItem> menuItems = new ArrayList<>();

        private FloatingActionButton fab;

        private int gravity = Gravity.BOTTOM | Gravity.RIGHT;

        private int animationType = ANIMATION_TYPE_BLOOM;

        private boolean enableFollowAnimation = true;

        @ColorRes
        private int revealColor = android.R.color.holo_purple;

        private OnFabClickListener onFabClickListener;

        private ArrayList<OnMenuActionListener> actionListeners = new ArrayList<>();

        public Builder(Context context) {
            this.context = context;
        }

        public SpringFloatingActionMenu build() {
            return new SpringFloatingActionMenu(this);
        }

        public Builder addMenuItem(@ColorRes int bgColor, int icon, String label,
                                   @ColorRes int textColor, View.OnClickListener onClickListener) {
            menuItems.add(new MenuItem(bgColor, icon, label, textColor, onClickListener));
            return this;
        }

        public Builder addMenuItem(@ColorRes int bgColor, int icon, String label,
                                   @ColorRes int textColor, int diameter, View.OnClickListener onClickListener) {
            menuItems.add(new MenuItem(bgColor, icon, label, textColor, diameter, onClickListener));
            return this;
        }

        public Builder fab(FloatingActionButton fab) {
            this.fab = fab;
            return this;
        }

        public Builder gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder onMenuActionListner(OnMenuActionListener listener) {
            actionListeners.add(listener);
            return this;
        }

        public Builder animationType(@ANIMATION_TYPE int type) {
            this.animationType = type;
            return this;
        }

        public Builder revealColor(@ColorRes int color) {
            this.revealColor = color;
            return this;
        }

        public Builder enableFollowAnimation(boolean enable) {
            this.enableFollowAnimation = enable;
            return this;
        }

        public Builder onFabClickListener(OnFabClickListener listener) {
            this.onFabClickListener = listener;
            return this;
        }
    }

}
