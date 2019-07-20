package cn.vailing.chunqiu.jiashifen.bean;

import android.widget.ImageButton;

/**
 * Created by dream on 2018/7/4.
 */

public class FileBean {
    private String thumbnailBigPath;
    private String originalPath;
    private ImageButton imageButton;

    public ImageButton getImageButton() {
        return imageButton;
    }

    public void setImageButton(ImageButton imageButton) {
        this.imageButton = imageButton;
    }

    public FileBean( String thumbnailBigPath,String originalPath,ImageButton imageButton) {
        this.imageButton = imageButton;
        this.originalPath = originalPath;
        this.thumbnailBigPath = thumbnailBigPath;
    }

    public FileBean() {

    }




    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getThumbnailBigPath() {
        return thumbnailBigPath;
    }

    public void setThumbnailBigPath(String thumbnailBigPath) {
        this.thumbnailBigPath = thumbnailBigPath;
    }
}
