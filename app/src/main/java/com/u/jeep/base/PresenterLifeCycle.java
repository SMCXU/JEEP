package com.u.jeep.base;

/**
 * 给我一行代码，还你十个BUG
 *
 * @author：Mr.U 创建时间：2019-07-26
 * 更改时间：2019-07-26
 * 版本号：1
 * 文件描述：
 */
public interface PresenterLifeCycle<T extends BaseView> {

    void attachView(T view);

    void detachView();
}
