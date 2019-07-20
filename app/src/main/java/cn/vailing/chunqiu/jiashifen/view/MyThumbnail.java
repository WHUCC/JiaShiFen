package cn.vailing.chunqiu.jiashifen.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nantaphop.hovertouchview.HoverTouchAble;

import cn.vailing.chunqiu.jiashifen.R;
import cn.vailing.chunqiu.jiashifen.util.ToastUtil;

/**
 * Created by dream on 2018/7/5.
 */

public class MyThumbnail extends ImageView implements HoverTouchAble {
    private String originalPath;
    private OnHoveListener onHoveListener;

    public MyThumbnail(Context context) {
        super(context);
    }

    public MyThumbnail(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyThumbnail, 0, 0);
        try {
            originalPath = a.getString(R.styleable.MyThumbnail_originalPath);
        } finally {
            // TypedArray objects are shared and must be recycled.
            a.recycle();
        }
    }

    public MyThumbnail(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyThumbnail(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    @Override
    public View getHoverView() {
        return new MyThumbnailExpand(getContext(), getDrawable(), originalPath);
    }

    @Override
    public int getHoverAnimateDuration() {
        return 300;
    }

    @Override
    public void onStartHover() {
        if (onHoveListener == null)
            return;
        onHoveListener.onStartHover();
//        ToastUtil.makeToast(getContext(), "Start Hover");
    }

    @Override
    public void onStopHover() {
        if (onHoveListener == null)
            return;
          onHoveListener.onStopHover();
//        ToastUtil.makeToast(getContext(), "Stop Hover");
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public OnHoveListener getOnHoveListener() {
        return onHoveListener;
    }

    public void setOnHoveListener(OnHoveListener onHoveListener) {
        this.onHoveListener = onHoveListener;
    }

    public interface OnHoveListener {
        void onStartHover();

        void onStopHover();
    }
}
