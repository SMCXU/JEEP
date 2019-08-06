package com.u.jeep.util;

import java.lang.reflect.ParameterizedType;

/**
 * 给我一行代码，还你十个BUG
 *
 * @author：Mr.U 创建时间：2019-07-27
 * 更改时间：2019-07-27
 * 版本号：1
 * 文件描述：针对泛型的工具类
 */
public class TypeUtil {

    //获取指定类上的指定下标泛型实例
    public static <T> T getObject(Object o, int index) {

        try {
            /**
             *  Object    getClass 获取指定对象 Class 对象
             *            getGenericSuperclass 获取Class上的泛型
             *  ParameterizedType  泛型参数
             *      getActualTypeArguments  获取Class的泛型
             *      newInstance 直接获取一个实例
             */

            T t = ((Class<T>) ((ParameterizedType) o.getClass().getGenericSuperclass()).getActualTypeArguments()[index]).newInstance();

            return t;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }
}
