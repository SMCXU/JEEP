package com.u.jeep.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * 文件描述：
 * 作者：Mr.U
 * 创建时间：2019-07-26
 * 更改时间：2019-07-26
 * 版本号：1
 * <p>
 * 给我一行代码，还你十个BUG
 */
public class BaseApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
