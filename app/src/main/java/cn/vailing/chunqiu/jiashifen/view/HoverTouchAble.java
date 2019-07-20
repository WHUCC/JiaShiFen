package cn.vailing.chunqiu.jiashifen.view;

import android.view.View;

/**
 * Created by nantaphop on 01-Jan-16.
 */
public interface HoverTouchAble{
    public View getHoverView();
    public int getHoverAnimateDuration();
    public void onStartHover();
    public void onStopHover();

}
