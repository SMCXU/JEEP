package com.u.jeep.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.u.jeep.dialog.LoadingDialogView;
import com.u.jeep.util.TypeUtil;

/**
 * 给我一行代码，还你十个BUG
 *
 * @author：Mr.U 创建时间：2019-08-01
 * 更改时间：2019-08-01
 * 版本号：1
 * 文件描述：
 */
public abstract class BaseFragment<P extends BasePresenter> extends NotMVPBaseFragment implements BaseView{

    public P mPresenter;
    protected LoadingDialogView dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //初始化presenter
        mPresenter = TypeUtil.getObject(this, 0);

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    //intent跳转
    protected void intent2Activity(Class<? extends AppCompatActivity> clazz) {

        Intent intent = new Intent(getContext(), clazz);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mPresenter != null) {//== null 类上没泛型
            mPresenter.detachView();//管理Presenter的生命周期
        }
        hideLoading();
    }


    /**
     * dismiss dialog
     */
    public void hideLoading() {
        shouldShowLoading = false;
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean shouldShowLoading = false;

    /**
     * loading dialog 是否可以取消
     */
    public void showLoading(Activity activity, boolean cancelable) {

        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = new LoadingDialogView(activity, cancelable);
        dialog.show();
    }

    /**
     * loading dialog 是否可以取消
     */
    public void showLoading(Activity activity) {
        shouldShowLoading = true;
        if (isSupportVisible()) {
            showLoading(activity, true);
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (shouldShowLoading) {
            showLoading(_mActivity);
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        hideLoading();
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


}
