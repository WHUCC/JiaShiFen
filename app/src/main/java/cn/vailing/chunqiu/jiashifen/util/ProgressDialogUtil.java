package cn.vailing.chunqiu.jiashifen.util;

import android.app.ProgressDialog;
import android.content.Context;



/**
 * Created by dream on 2017/4/17.
 */

public class ProgressDialogUtil {
    private Context context;
    private String title;
    private String des;
    private ProgressDialog dialog;

    public ProgressDialogUtil(Context context, String title, String des) {
        this.context = context;
        this.title = title;
        this.des = des;
        dialog = new ProgressDialog(context);
    }

    public void show() {
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.setTitle(title);
        dialog.setMessage(des);
        dialog.show();
    }

    public void cancel() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

}
