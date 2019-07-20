package cn.vailing.chunqiu.jiashifen.util;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.vailing.chunqiu.jiashifen.R;

/**
 * Created by dream on 2018/6/30.
 */

public class ToastUtil {
    public static void makeToast(Context context, String message){
        Toast toast = new Toast(context);

        TextView textView = new TextView(context);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setTextSize(18f);
        textView.setBackgroundResource(R.drawable.imagebutton_shape);
        textView.setTextColor(0xffd1d2d1);
        textView.setText(message);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(textView);
        toast.show();
    }

}
