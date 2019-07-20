package cn.vailing.chunqiu.jiashifen.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
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

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.nantaphop.hovertouchview.HoverTouchHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import cn.vailing.chunqiu.jiashifen.R;
import cn.vailing.chunqiu.jiashifen.RecordDatabase;
import cn.vailing.chunqiu.jiashifen.activity.MainActivity;
import cn.vailing.chunqiu.jiashifen.engine.RecordManager;
import cn.vailing.chunqiu.jiashifen.util.ContentValueUtil;
import cn.vailing.chunqiu.jiashifen.util.FileUtil;
import cn.vailing.chunqiu.jiashifen.view.MyThumbnail;
import co.lujun.androidtagview.TagContainerLayout;
import yalantis.com.sidemenu.interfaces.ScreenShotable;


public class RecordInfoFragment extends Fragment implements ScreenShotable, MyThumbnail.OnHoveListener {
    private static RecordDatabase record;
    private View containerView;
    protected int res;
    private Bitmap bitmap;
    private FrameLayout root;
    private GridLayout record_info_gridlayout;
    private ImageView imageButton;
    private float scale;

    private TextView record_info_describe;

    private TagContainerLayout record_info_tag;
    private static String name;
    protected String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    protected String[] mParties = new String[]{
            "right", "vague", "error", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };
    Typeface mTfRegular;
    Typeface mTfLight;

    private MyThumbnail myThumbnail = null;
    private PieChart record_info_pie;

    public static RecordInfoFragment newInstance(int resId, RecordDatabase recordDatabase) {
        RecordInfoFragment contentFragment = new RecordInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Integer.class.getName(), resId);
        contentFragment.setArguments(bundle);
        record = recordDatabase;
        name = record.getName();
        return contentFragment;
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
        View rootView = inflater.inflate(R.layout.record_main, container, false);
        scale = getContext().getResources().getDisplayMetrics().density;
        initUI(rootView);
        return rootView;
    }


    private void initUI(View rootView) {
        record_info_pie = (PieChart) rootView.findViewById(R.id.record_info_pie);
        root = (FrameLayout) rootView.findViewById(R.id.container);

        record_info_gridlayout = (GridLayout) rootView.findViewById(R.id.record_info_gridlayout);
        record_info_describe = (TextView) rootView.findViewById(R.id.record_info_describe);

        record_info_tag = (TagContainerLayout) rootView.findViewById(R.id.record_info_tag);
        loadInfo();
    }

    private void loadInfo() {
        loadPie();
        MainActivity.topTitle.setText(record.getTitle());
        loadDescribe();
        loadPicture();
        loadTags();
    }

    private void setData(int count, float range) {
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        float[] num = new float[]{record.getRightTimes(), record.getVagueTimes(), record.getErrorTimes()};
        for (int i = 0; i < count; i++) {

            entries.add(new PieEntry((num[i] / (float) record.getVisitTimes()), mParties[i % mParties.length]));
            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setSliceSpace(3f);//--饼状图
            dataSet.setSelectionShift(15f);//--选中饼状图时，向外扩张的大小.

            ArrayList<Integer> colors = new ArrayList<Integer>();

            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.LIBERTY_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.PASTEL_COLORS)
                colors.add(c);

            colors.add(ColorTemplate.getHoloBlue());

            dataSet.setColors(colors);

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(15);       //--设置字体大小
            data.setValueTextColor(Color.BLACK);//--设置饼状图其中各个块上的百分比颜色
            data.setValueTypeface(mTfLight);  //--设置字体
            record_info_pie.setData(data);

            // undo all highlights
            record_info_pie.highlightValues(null);

            record_info_pie.invalidate();

        }
    }


    private void loadPie() {

        mTfRegular = Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Light.ttf");
//        record_info_pie =    (PieChart) findViewById(R.id.chart1);
        record_info_pie.setUsePercentValues(true);
        record_info_pie.setContentDescription("描述内容");
        record_info_pie.setCenterTextSize(18);

        record_info_pie.setExtraOffsets(5, 5, 5, 5);

        record_info_pie.setDragDecelerationFrictionCoef(0.95f);

        record_info_pie.setCenterTextTypeface(mTfLight);

        record_info_pie.setCenterText(String.format("%.2f", record.getCarryLevel()));//--设置中心点文字

        record_info_pie.setDrawHoleEnabled(true);
        record_info_pie.setHoleColor(0xef3614);

        record_info_pie.setTransparentCircleColor(Color.TRANSPARENT);//--内圆边框色
        record_info_pie.setTransparentCircleAlpha(110);       //--内圆边框透明度

        record_info_pie.setHoleRadius(58f); //--内院半径
        record_info_pie.setTransparentCircleRadius(61f); //--内圆边框大小半径

        record_info_pie.setDrawCenterText(true);

        record_info_pie.setRotationAngle(0); //--绘制的开始位置
        // enable rotation of the chart by touch
        record_info_pie.setRotationEnabled(true);     //--允许旋转
        record_info_pie.setHighlightPerTapEnabled(true); //---允许点击其中某个扇形区域.
//
//        // add a selection listener
//        record_info_pie.setOnChartValueSelectedListener(this);

        setData(3, 100);

        record_info_pie.animateY(1400, Easing.EasingOption.EaseInOutQuad);


        // entry label styling
        record_info_pie.setEntryLabelColor(Color.BLACK); //--设置饼状图其中各个块上的文字颜色
        record_info_pie.setEntryLabelTypeface(mTfRegular);//---设置字体
        record_info_pie.setEntryLabelTextSize(10);    //--设置字体大小
    }

    private void loadTags() {
        record_info_tag.removeAllTags();
        File file = new File(getContext().getFilesDir(), name + ContentValueUtil.TAG);
        String res = FileUtil.readFile(file);
        if (!TextUtils.isEmpty(res)) {
            String[] stings = res.split(ContentValueUtil.DIVIDE);
            for (String s : stings) {
                if (!TextUtils.isEmpty(s)) {
                    record_info_tag.addTag(s);
                }
            }
        }
    }


    private void loadPicture() {
        record_info_gridlayout.removeAllViews();
        record_info_gridlayout.bringToFront();
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
                    record_info_gridlayout.addView(myThumbnail, paramsGl);
                    HoverTouchHelper.make(root, myThumbnail);
                }

            }

        }
    }

    private void createImageView() {
        imageButton = new ImageView(getContext());
        GridLayout.LayoutParams paramsGl = new GridLayout.LayoutParams(
                new ViewGroup.LayoutParams((int) (100 * scale + 0.5f), (int) (180 * scale + 0.5f)));
        paramsGl.setMargins((int) (2 * scale + 0.5f), 0, 0, 0);
        imageButton.setScaleType(ImageButton.ScaleType.FIT_CENTER);
        imageButton.setBackgroundResource(R.drawable.imagebutton_shape);
        record_info_gridlayout.addView(imageButton, paramsGl);
    }

    private void loadDescribe() {

        File file = new File(getContext().getFilesDir(), name + ContentValueUtil.DESCRIBE);
        String des = FileUtil.readFile(file);
        record_info_describe.setText(des);
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
                RecordInfoFragment.this.bitmap = bitmap;
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

}

