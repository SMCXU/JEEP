package com.u.jeep.base;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.gyf.barlibrary.ImmersionBar;
import com.u.jeep.R;
import com.u.jeep.anno.Presenter;
import com.u.jeep.dialog.LoadingDialogView;
import com.u.jeep.util.TypeUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * 文件描述：Base 里都干了啥？
 * * ①引入Presenter model 对象（管理presenter 的生命周期）
 * * ②ButterKnife 绑定View
 * * ③设置状态栏
 * * ④友盟
 * 作者：Mr.U
 * 创建时间：2019-07-26
 * 更改时间：2019-07-26
 * 版本号：1
 * <p>
 * 给我一行代码，还你十个BUG
 *
 * @author u
 */

public abstract class BaseActivity<P extends BasePresenter> extends SupportActivity implements BaseView, BGASwipeBackHelper.Delegate {

    public BGASwipeBackHelper mSwipeBackHelper;
    public P mPresenter;

    protected LoadingDialogView dialogView;

    protected Context mContext;

    private InputMethodManager imm;

    protected ImmersionBar mImmersionBar;

    static {
        //为了兼容vector矢量图 http://blog.csdn.net/qq_15545283/article/details/51472458
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private LoadingDialogView dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStackManager.getInstance().addActivity(this);
        mContext = this;
        //在设置布局前作操作
        doBeforeLayout();
        //设置布局
        setContentView(getLayoutId());
        //View注入
        ButterKnife.bind(this);
        //初始化沉浸式
        if (isImmersionBarEnabled()) {
            setStatusBar();
        }
        //初始化Presenetr
        mPresenter = TypeUtil.getObject(this, 0);//activity类上的泛型
        if (mPresenter == null) {//去类顶上的泛型去找
            Class<? extends BaseActivity> aClass = this.getClass();
            Presenter pAnno = aClass.getAnnotation(Presenter.class);
            if (pAnno != null) {
                Class pClass = pAnno.value();
                if (!pClass.getName().endsWith("Presenter")) {
                    throw new ClassCastException("Presenter 泛型 必须放Presenter 类");
                }
                try {
                    Object o = pClass.newInstance();
                    mPresenter = (P) o;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }

        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        //初始化View
        initView(savedInstanceState);
    }

    //intent跳转
    protected void intent2Activity(Class<? extends AppCompatActivity> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected abstract void initView(Bundle savedInstanceState);


    public abstract int getLayoutId();

    protected void doBeforeLayout() {
        initSwipeBackFinish();

    }

    /**
     * 初始化滑动返回。在 super.onCreateView(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init(this) 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(false);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(false);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper.setIsNavigationBarOverlap(false);

    }

    //是否可以使用沉浸式 默认true
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 设置页面状态栏
     */
    private void setStatusBar() {

        mImmersionBar = ImmersionBar.with(this)
//                .statusBarColor(R.color.white) //设置状态栏颜色，不写默认透明色
                .statusBarDarkFont(true, 1f)//原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不加上透明度
                .fitsSystemWindows(true)//解决状态栏和布局重叠问题，任选其一，默认为false,当为true时一定要指定statueBarColor,不然状态栏为透明，还有一些重载方法
                .keyboardEnable(true);

        //指定一个导航栏颜色
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            mImmersionBar.navigationBarColor(R.color.nav_bar_color_white);
        } else {
            mImmersionBar.navigationBarColor(R.color.nav_bar_color_black);
        }

        mImmersionBar.init();
    }

    @Override
    public void onStartLoad() {

    }

    @Override
    public void onStopLoad() {

    }

    @Override
    public void onError(String errorInfo) {

    }

    @Override
    public BaseActivity getActivity() {
        return null;
    }

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {

    }

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    @Override
    public void onSwipeBackLayoutCancel() {

    }

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

    @Override
    public void onBackPressedSupport() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding()) {
            return;
        }
        mSwipeBackHelper.backward();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * loading dialog 是否可以取消
     */
    protected void showLoading(boolean cancelable) {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = new LoadingDialogView(this, cancelable);
        dialog.show();
    }

    /**
     * loading dialog 是否可以取消
     */
    public void showLoading() {
        showLoading(true);
    }

    /**
     * dismiss dialog
     */
    public void hideLoading() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 管理Presenter的生命周期 attachView/detachView
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        hideLoading();

        try {
            if (mImmersionBar != null) {
                mImmersionBar.destroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ActivityStackManager.getInstance().removeActivity(this);
    }

    @Override
    public void finish() {
        super.finish();
        hideSoftKeyBoard();
    }

    public void hideSoftKeyBoard() {
        View localView = getCurrentFocus();
        if (this.imm == null) {
            this.imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.imm != null)) {
            this.imm.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }

}
