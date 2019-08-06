package com.u.jeep.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Window;

import com.u.jeep.R;

/**
 * 给我一行代码，还你十个BUG
 *
 * @author：Mr.U 创建时间：2019-07-26
 * 更改时间：2019-07-26
 * 版本号：1
 * 文件描述：
 */
public class LoadingDialogView extends Dialog {

    public Context mContext;

    public LoadingDialogView(Context context) {
        super(context, R.style.loading_alert_dialog);
        this.mContext = context;
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        window.setWindowAnimations(R.style.DialogWindowStyle);
    }


    public LoadingDialogView(Context context, boolean cancelable) {
        super(context, R.style.loading_alert_dialog);
        this.mContext = context;
        setCancelable(cancelable);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        window.setWindowAnimations(R.style.DialogWindowStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_alert_dialog);
    }
}
