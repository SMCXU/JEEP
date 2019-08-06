package com.u.jeep.base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 给我一行代码，还你十个BUG
 *
 * @author：Mr.U 创建时间：2019-07-26
 * 更改时间：2019-07-26
 * 版本号：1
 * 文件描述：activity堆栈管理
 */
public class ActivityStackManager {


    private static Stack<Activity> mActivityStack;

    //静态内部类的单例  单例最优解
    private static class ManagerInner {
        private static ActivityStackManager instance = new ActivityStackManager();
    }

    public static ActivityStackManager getInstance() {
        return ManagerInner.instance;
    }


    private ActivityStackManager() {
        mActivityStack = new Stack<>();
    }

    /**
     * 入栈
     */
    public void addActivity(Activity activity) {
        mActivityStack.push(activity);
    }

    /**
     * 出栈
     */
    public void removeActivity(Activity activity) {
        mActivityStack.remove(activity);
    }

    /**
     * 彻底退出
     */
    public void finishAllActivity() {
        Activity activity;
        while (!mActivityStack.empty()) {
            activity = mActivityStack.pop();
            if (activity != null) {
                activity.finish();
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {

            if (activity.getClass().equals(cls)) {
                activity.finish();
                activity = null;
                return;
            }
        }

    }

    /**
     * 查找栈中是否存在指定的activity
     */
    public boolean checkActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {

            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 结束指定的activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * finish指定的activity之上所有的activity
     *
     * @param actCls
     * @param isIncludeSelf
     * @return
     */
    public boolean finishToActivity(Class<? extends Activity> actCls, boolean isIncludeSelf) {
        List<Activity> buf = new ArrayList<>();
        int size = mActivityStack.size();
        Activity activity = null;
        for (int i = size - 1; i >= 0; i--) {
            activity = mActivityStack.get(i);
            if (activity.getClass().isAssignableFrom(actCls)) {
                for (Activity a : buf) {
                    a.finish();
                }
                return true;
            } else if (i == size - 1 && isIncludeSelf) {
                buf.add(activity);
            } else if (i != size - 1) {
                buf.add(activity);
            }
        }
        return false;
    }

    /**
     * finish指定的activity之上所有的activity，包括指定的activity本身
     */
    public boolean finishToActivityIncludeBottom(Class<? extends Activity> actCls, boolean isIncludeSelf) {
        List<Activity> buf = new ArrayList<>();
        int size = mActivityStack.size();
        Activity activity = null;
        for (int i = size - 1; i >= 0; i--) {
            activity = mActivityStack.get(i);
            if (activity.getClass().isAssignableFrom(actCls)) {
                buf.add(activity);
                for (Activity a : buf) {
                    a.finish();
                }
                return true;
            } else if (i == size - 1 && isIncludeSelf) {
                buf.add(activity);
            } else if (i != size - 1) {
                buf.add(activity);
            }
        }
        return false;
    }

    /**
     * 得到某一个activity
     */
    public Activity getActivity(Class<? extends Activity> actCls) {
        int size = mActivityStack.size();
        for (int i = 0; i < size; i++) {
            if (mActivityStack.get(i).getClass().equals(actCls)) {
                return mActivityStack.get(i);
            }
        }
        return null;
    }

    /**
     * 获取栈顶的activity
     */
    public Activity getCurrentActivity() {
        int size = mActivityStack.size();
        if (size > 1) {
            return mActivityStack.get(size - 1);
        }
        return null;
    }

    /**
     * 获取当前activity的数量,可以用来判断应用是否运行在前台
     */
    public int getCount() {
        return mActivityStack.size();
    }
}
