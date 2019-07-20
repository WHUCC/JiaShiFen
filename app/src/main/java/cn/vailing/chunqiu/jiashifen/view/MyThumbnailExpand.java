package cn.vailing.chunqiu.jiashifen.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.vailing.chunqiu.jiashifen.R;

/**
 * Created by dream on 2018/7/5.
 */
public class MyThumbnailExpand extends LinearLayout {

    private String path;

    private Drawable drawable;

    public MyThumbnailExpand(Context context) {
        super(context);
    }

    public MyThumbnailExpand(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyThumbnailExpand(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyThumbnailExpand(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MyThumbnailExpand(Context context, Drawable drawable, String path) {
        super(context);
        this.drawable = drawable;
        this.path = path;
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_my_thumbnail_expand, this);
        ImageView img = (ImageView) findViewById(R.id.img);
        img.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        TextView text = (TextView) findViewById(R.id.text);
        if (path != null)
            Picasso.with(getContext()).load("file://" +path).into(img);
        else
            img.setImageDrawable(this.drawable);
//        text.setText(this.text);

    }
}
