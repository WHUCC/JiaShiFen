package cn.vailing.chunqiu.jiashifen.fragment;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nantaphop.hovertouchview.HoverTouchAble;
import com.nantaphop.hovertouchview.HoverTouchHelper;
import com.squareup.picasso.Picasso;

import java.io.File;

import cn.vailing.chunqiu.jiashifen.R;
import cn.vailing.chunqiu.jiashifen.RecordDatabase;
import cn.vailing.chunqiu.jiashifen.activity.MainActivity;
import cn.vailing.chunqiu.jiashifen.bean.FileBean;
import cn.vailing.chunqiu.jiashifen.engine.RecordManager;
import cn.vailing.chunqiu.jiashifen.util.ContentValueUtil;
import cn.vailing.chunqiu.jiashifen.util.FileUtil;
import cn.vailing.chunqiu.jiashifen.view.MyHoverTouchHelper;
import cn.vailing.chunqiu.jiashifen.view.MyThumbnail;
import co.lujun.androidtagview.TagContainerLayout;
import jp.wasabeef.blurry.Blurry;
import yalantis.com.sidemenu.interfaces.ScreenShotable;

/**
 * Created by dream on 2018/7/1.
 */

public class HostPageFragment extends Fragment implements ScreenShotable, MyThumbnail.OnHoveListener, View.OnClickListener {
    private View containerView;
    protected int res;
    private Bitmap bitmap;
    private FrameLayout root;
    private GridLayout host_gridlayout;
    private ImageView imageButton;
    private float scale;
    private static RecordDatabase record;
    private TextView host_describe;
    private TextView host_tip;
    private TagContainerLayout host_tag;
    private static String name;
    private static int index = 0;
    private Button host_certain;
    private Button host_vague;
    private Button host_forget;
    private LinearLayout main_bottom;
    private RelativeLayout host_cover;
    private MyThumbnail myThumbnail = null;


    public static HostPageFragment newInstance(int resId) {

        HostPageFragment hostPageFragment = new HostPageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Integer.class.getName(), resId);
        hostPageFragment.setArguments(bundle);
        return hostPageFragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.containerView = view.findViewById(R.id.container);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getArguments().getInt(Integer.class.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.host_page_fragment, container, false);
        scale = getContext().getResources().getDisplayMetrics().density;
        initUI(rootView);
        setOnClick();
        return rootView;
    }

    private void setOnClick() {
        host_certain.setOnClickListener(this);
        host_vague.setOnClickListener(this);
        host_forget.setOnClickListener(this);

    }

    private void initUI(View rootView) {

        host_certain = (Button) rootView.findViewById(R.id.host_certain);
        host_vague = (Button) rootView.findViewById(R.id.host_vague);
        host_forget = (Button) rootView.findViewById(R.id.host_forget);
        root = (FrameLayout) rootView.findViewById(R.id.container);
        host_gridlayout = (GridLayout) rootView.findViewById(R.id.host_gridlayout);
        host_describe = (TextView) rootView.findViewById(R.id.host_describe);
        host_tip = (TextView) rootView.findViewById(R.id.host_tip);
        host_tag = (TagContainerLayout) rootView.findViewById(R.id.host_tag);
        main_bottom = (LinearLayout) rootView.findViewById(R.id.main_bottom);
        host_cover = (RelativeLayout) rootView.findViewById(R.id.host_cover);
        host_cover.setBackgroundResource(R.drawable.main_cover);
        loadInfo();
    }

    private void loadInfo() {

        if (RecordManager.getInstance().isNeedStudy()) {
            record = RecordManager.getInstance().getShouldStudys().get(0);
            name = record.getName();
            MainActivity.topTitle.setText(record.getTitle());
            loadDescribe();
            loadPicture();
            loadTags();
            main_bottom.setVisibility(View.INVISIBLE);
            host_cover.setVisibility(View.VISIBLE);

            host_cover.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    main_bottom.setVisibility(View.VISIBLE);
                    host_cover.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            host_cover.setVisibility(View.VISIBLE);
            host_cover.setBackgroundResource(R.drawable.main_none);
            MainActivity.topTitle.setText("当日科目计划完成");
            host_tip.setVisibility(View.INVISIBLE);
            main_bottom.setVisibility(View.INVISIBLE);
        }

    }

    private void loadTags() {
        host_tag.removeAllTags();
        File file = new File(getContext().getFilesDir(), name + ContentValueUtil.TAG);
        String res = FileUtil.readFile(file);
        if (!TextUtils.isEmpty(res)) {
            String[] stings = res.split(ContentValueUtil.DIVIDE);
            for (String s : stings) {
                if (!TextUtils.isEmpty(s)) {
                    host_tag.addTag(s);
                }
            }
        }
    }


    private void loadPicture() {
        host_gridlayout.removeAllViews();
        host_gridlayout.bringToFront();
        File thumbnailFile = new File(getContext().getFilesDir(), name + ContentValueUtil.THUMBNAILPICTURE);
        String thumbnailBigPath = FileUtil.readFile(thumbnailFile);
        if (TextUtils.isEmpty(thumbnailBigPath)) {
            createImageView();
        } else {
            File originalFile = new File(getContext().getFilesDir(), name + ContentValueUtil.ORIGINALPICTURE);
            String originalPath = FileUtil.readFile(originalFile);
            String[] thumbnailBigPaths = thumbnailBigPath.split(ContentValueUtil.DIVIDE);
            String[] originalPaths = originalPath.split(ContentValueUtil.DIVIDE);
            for (int i = 0; i < thumbnailBigPaths.length; ++i) {
                if (!TextUtils.isEmpty(thumbnailBigPaths[i]) && !TextUtils.isEmpty(originalPaths[i])) {
                    myThumbnail = new MyThumbnail(getContext());
                    GridLayout.LayoutParams paramsGl = new GridLayout.LayoutParams(
                            new ViewGroup.LayoutParams((int) (83 * scale + 0.5f), (int) (83 * scale + 0.5f)));
                    paramsGl.setMargins((int) (2 * scale + 0.5f), 0, 0, 0);
                    myThumbnail.setOriginalPath(originalPaths[i].trim());
                    Picasso.with(getActivity()).load("file://" + thumbnailBigPaths[i]).into(myThumbnail);
                    myThumbnail.setBackgroundResource(R.drawable.imagebutton_shape);
                    myThumbnail.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                    myThumbnail.setOnHoveListener(this);
                    myThumbnail.setTag(false);
                    host_gridlayout.addView(myThumbnail, paramsGl);
                    HoverTouchHelper.make(root, myThumbnail);
                }

            }

        }
    }

    private void loadDescribe() {
        host_describe.setText("");
        File file = new File(getContext().getFilesDir(), name + ContentValueUtil.DESCRIBE);
        String des = FileUtil.readFile(file);
        host_describe.setText(des);
    }


    private void createImageView() {
        imageButton = new ImageView(getContext());
        GridLayout.LayoutParams paramsGl = new GridLayout.LayoutParams(
                new ViewGroup.LayoutParams((int) (100 * scale + 0.5f), (int) (180 * scale + 0.5f)));
        paramsGl.setMargins((int) (2 * scale + 0.5f), 0, 0, 0);
        imageButton.setScaleType(ImageButton.ScaleType.FIT_CENTER);
        imageButton.setBackgroundResource(R.drawable.imagebutton_shape);
        host_gridlayout.addView(imageButton, paramsGl);
    }

    @Override
    public void takeScreenShot() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Bitmap bitmap = Bitmap.createBitmap(containerView.getWidth(),
                        containerView.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                containerView.draw(canvas);
                HostPageFragment.this.bitmap = bitmap;
            }
        };

        thread.start();

    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void onStartHover() {
        MainActivity.topTitle.setVisibility(View.INVISIBLE);
        MainActivity.toolbar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStopHover() {
        MainActivity.topTitle.setVisibility(View.VISIBLE);
        MainActivity.toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.host_certain:
                RecordManager.getInstance().certain(record);
                break;
            case R.id.host_vague:
                RecordManager.getInstance().vague(record);
                break;
            case R.id.host_forget:
                RecordManager.getInstance().forget(record);
                break;
        }
        RecordManager.getInstance().getShouldStudys().remove(0);
        loadInfo();
    }
}
