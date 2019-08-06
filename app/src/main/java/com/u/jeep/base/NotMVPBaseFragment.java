package com.u.jeep.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * 给我一行代码，还你十个BUG
 *
 * @author：Mr.U 创建时间：2019-08-01
 * 更改时间：2019-08-01
 * 版本号：1
 * 文件描述：
 */
public abstract class NotMVPBaseFragment extends SupportFragment {

    //类名
    private String CLASS_NAME;
    Unbinder unbinder;
    protected View layout;
    protected Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //通过反射获取类名
        CLASS_NAME = this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (layout == null) {
            layout = inflater.inflate(getLayoutId(), container, false);
        }
        //View 注入
        unbinder = ButterKnife.bind(this, layout);
        mContext = getContext();
        //初始化View
        initView();
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(CLASS_NAME); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(CLASS_NAME);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();
}
