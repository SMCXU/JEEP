package com.u.jeep.base;

/**
 * 文件描述：所有的操作基本都需要 开始 结束 错误
 * 作者：Mr.U
 * 创建时间：2019-07-26
 * 更改时间：2019-07-26
 * 版本号：1
 * <p>
 * 给我一行代码，还你十个BUG
 */
public interface BaseView {

    void onStartLoad();

    void onStopLoad();

    void onError(String errorInfo);

    BaseActivity getActivity();
}
