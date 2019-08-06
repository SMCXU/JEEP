package com.u.jeep.anno;

import android.annotation.TargetApi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 给我一行代码，还你十个BUG
 *
 * @author：Mr.U 创建时间：2019-07-28
 * 更改时间：2019-07-28
 * 版本号：1
 * 文件描述：
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Presenter {
    /**
     * 传Presenter.class
     * */
    Class value();
}
