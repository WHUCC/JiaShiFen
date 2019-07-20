package cn.vailing.chunqiu.jiashifen.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;



import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.vailing.chunqiu.jiashifen.R;
import cn.vailing.chunqiu.jiashifen.bean.FileBean;
import cn.vailing.chunqiu.jiashifen.RecordDatabase;
import cn.vailing.chunqiu.jiashifen.util.CalenderUtil;
import cn.vailing.chunqiu.jiashifen.util.ContentValueUtil;
import cn.vailing.chunqiu.jiashifen.util.FileUtil;
import cn.vailing.chunqiu.jiashifen.util.ToastUtil;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import yalantis.com.sidemenu.interfaces.ScreenShotable;


/**
 * Created by dream on 2018/7/1.
 */

public class AddRecordFragment extends Fragment implements ScreenShotable, View.OnClickListener, View.OnLongClickListener {
    private View containerView;
    private ImageButton imageButton;
    private ImageButton tag_bt;
    private GridLayout gridLayout;
    private Button confirm;
    private TagContainerLayout mTagContainerLayout;
    private EditText record_title;
    private EditText record_describe;
    protected int res;
    private Bitmap bitmap;
    private float scale;
    private final int maxSize = 8;
    private TextView picture_describe;
    private TextView mark_tv;
    private String desText = "图片描述";
    private String markText = "标签选择";
    private int tagSize = 0;
    private final int maxTagSize = 3;
    private List<FileBean> fileBeanList;
    private List<MediaBean> mediaBeanList;
    private int id = 817624;

    public static AddRecordFragment newInstance(int resId) {
        AddRecordFragment addRecordFragment = new AddRecordFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Integer.class.getName(), resId);
        addRecordFragment.setArguments(bundle);
        return addRecordFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.containerView = view.findViewById(R.id.container);
    }

    private void setClick() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(record_title.getText().toString())) {
                    ToastUtil.makeToast(getContext(),  "求求你！输个标题");

                } else {
                    saveInfo();
                }
            }
        });
        imageButton.setOnClickListener(this);
        tag_bt.setOnClickListener(this);
        mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {

            }

            @Override
            public void onTagLongClick(int position, String text) {
                mTagContainerLayout.removeTag(position);
                tagSize--;
            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });
    }
//    private Long id;
//    private String name;
//    private int priority;
//    private int stage;
//    private int late_visit_day;
//    private int create_day;
//    private int visit_times;
//    private int right_times;
//    private int vague_times;
//    private int error_times;
//    private int carry_level;

    private void saveInfo() {
        String name = "v" + CalenderUtil.getInstance().getDateName();
        String title = record_title.getText().toString().trim();
        savePicture(name);
        saveDescribe(name);
        saveTags(name);
        int createDay = CalenderUtil.getInstance().getDayFromOriginal();
        RecordDatabase recordDatabase = new RecordDatabase();
        recordDatabase.setName(name);
        recordDatabase.setTitle(title);
        recordDatabase.setStage(0);
        recordDatabase.setCreateDay(createDay);
        recordDatabase.setShouldStudyTime(createDay+ContentValueUtil.reviewStage[0]);
        recordDatabase.setLateVisitDay(createDay);
        recordDatabase.setVisitTimes(0);
        recordDatabase.setRightTimes(0);
        recordDatabase.setVagueTimes(0);
        recordDatabase.setErrorTimes(0);
        recordDatabase.save();
        ToastUtil.makeToast(getContext(),  "保存成功!");
        clearFragment();
    }

    private void clearFragment() {
        picture_describe.setText(desText + "(" + 0 + "/" + maxSize + ")");
        record_describe.setText(markText + "(" + 0 + "/" + maxTagSize + ")");
        record_title.setText("");
        record_describe.setText("");
        gridLayout.removeAllViews();
        createImageButton();
        mTagContainerLayout.removeAllTags();
        fileBeanList.clear();
        mediaBeanList.clear();
    }

    private void saveTags(String name) {
        name = name + ContentValueUtil.TAG;
        StringBuilder sb = new StringBuilder();
        for (String info : mTagContainerLayout.getTags()) {
            sb.append(info);
            sb.append(ContentValueUtil.DIVIDE);
        }
        String info = sb.toString();
        File file = new File(getContext().getFilesDir(), name);
        FileUtil.writeFile(file, info);
    }

    private void saveDescribe(String name) {
        name = name + ContentValueUtil.DESCRIBE;
        String info = record_describe.getText().toString().trim();
        File file = new File(getContext().getFilesDir(), name);
        FileUtil.writeFile(file, info);
    }

    private void savePicture(String name) {
        String thumbnailBigPath = name + ContentValueUtil.THUMBNAILPICTURE;
        String originalPath = name+ ContentValueUtil.ORIGINALPICTURE;
        StringBuilder originalSB = new StringBuilder();
        StringBuilder thumbnailSB = new StringBuilder();
        for (FileBean fileBean:fileBeanList)
        {
            originalSB.append(fileBean.getOriginalPath());
            originalSB.append(ContentValueUtil.DIVIDE);

            thumbnailSB.append(fileBean.getThumbnailBigPath());
            thumbnailSB.append(ContentValueUtil.DIVIDE);
        }

        File thumbnailFile = new File(getContext().getFilesDir(), thumbnailBigPath);
        FileUtil.writeFile(thumbnailFile, thumbnailSB.toString().trim());

        File originalFile = new File(getContext().getFilesDir(), originalPath);
        FileUtil.writeFile(originalFile, originalSB.toString().trim());
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final AlertDialog alertDialog = builder.create();
        final View view = View.inflate(getContext(), R.layout.add_record_dailog, null);
        alertDialog.setView(view);
        alertDialog.show();
        Button comfirm = (Button) view.findViewById(R.id.dialog_confirm);
        final TagContainerLayout top = (TagContainerLayout) view.findViewById(R.id.dialog_tag_top);
        loadTop(top);
        final TagContainerLayout bottom = (TagContainerLayout) view.findViewById(R.id.dialog_tag_bottom);
        loadTags(bottom);
        ImageButton dialog_tag_add = (ImageButton) view.findViewById(R.id.dialog_tag_add);
        final EditText dialog_editext = (EditText) view.findViewById(R.id.dialog_editext);
        dialog_tag_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(dialog_editext.getText().toString().trim()))
                    return;
                else {
                    if (top.getTags().size() >= maxTagSize) {
                        ToastUtil.makeToast(getContext(), "朋友！最多三个");

                    } else {
                        if (top.getTags().size() > 0 && top.getTags().get(0).trim().equals("标签")) {
                            top.removeTag(0);
                        }
                        for (String info : top.getTags()) {
                            if (info.equals(dialog_editext.getText().toString().trim())) {
                                dialog_editext.setText("");
                                return;
                            }
                        }
                        top.addTag(dialog_editext.getText().toString().trim());
                        for (String info : bottom.getTags()) {
                            if (info.equals(dialog_editext.getText().toString().trim())) {
                                dialog_editext.setText("");
                                return;
                            }
                        }
                        bottom.addTag(dialog_editext.getText().toString().trim());
                    }

                }
                dialog_editext.setText("");
            }
        });
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (top.getTags().size() > 0 && top.getTags().get(0).trim().equals("标签"))
                    return;
                mark_tv.setText(markText + "(" + top.getTags().size() + "/" + maxTagSize + ")");
                mTagContainerLayout.setTags(top.getTags());
                addAndSaveTags(bottom.getTags());
            }
        });
        top.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {

            }

            @Override
            public void onTagLongClick(int position, String text) {
                top.removeTag(position);
            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });
        bottom.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                if (top.getTags().size() > 0 && top.getTags().get(0).trim().equals("标签")) {
                    top.removeTag(0);
                }
                if (top.getTags().size() >= maxTagSize) {
                    ToastUtil.makeToast(getContext(), "朋友！最多三个");
                } else {
                    for (String info : top.getTags()) {
                        if (info.equals(bottom.getTagText(position))) {
                            return;
                        }
                    }
                    top.addTag(bottom.getTagText(position));
                }
            }

            @Override
            public void onTagLongClick(int position, String text) {
                bottom.removeTag(position);
            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });
    }

    private void loadTop(TagContainerLayout top) {
        if (mTagContainerLayout.getTags().size() == 0) {
            top.setTags("标签");
        } else {
            for (String s : mTagContainerLayout.getTags()) {
                if (s.trim() != null) {
                    top.addTag(s);
                }
            }
        }
    }

    private void addAndSaveTags(List<String> bottom) {
        StringBuilder sb = new StringBuilder();
        for (String info : bottom) {
            sb.append(info);
            sb.append(ContentValueUtil.DIVIDE);
        }
        String info = sb.toString();
        File file = new File(getContext().getFilesDir(), "tags");
        FileUtil.writeFile(file, info);

    }

    private void loadTags(TagContainerLayout bottom) {
        judgeFileExists();
        File file = new File(getContext().getFilesDir(), "tags");
        String res = FileUtil.readFile(file);
        if (res != null) {
            String[] stings = res.split(ContentValueUtil.DIVIDE);
            for (String s : stings) {
                if (!TextUtils.isEmpty(s)) {
                    bottom.addTag(s);
                }
            }
        }
    }

    private void judgeFileExists() {
        File logFile = new File(getContext().getFilesDir(), "tags");
        if (FileUtil.isEmpty(logFile)) {
            FileUtil.createNew(logFile);
        }
    }

    private void initUI(View rootView) {
        record_title = (EditText) rootView.findViewById(R.id.record_title);
        record_describe = (EditText) rootView.findViewById(R.id.record_describe);
        confirm = (Button) rootView.findViewById(R.id.record_confirm);
        tag_bt = (ImageButton) rootView.findViewById(R.id.tag_bt);
        gridLayout = (GridLayout) rootView.findViewById(R.id.record_gridlayout);
        picture_describe = (TextView) rootView.findViewById(R.id.picture_describe);
        picture_describe.setText(desText + "(" + 0 + "/" + maxSize + ")");
        mark_tv = (TextView) rootView.findViewById(R.id.mark_tv);
        mark_tv.setText(markText + "(" + 0 + "/" + maxTagSize + ")");
        createImageButton();
        mTagContainerLayout = (TagContainerLayout) rootView.findViewById(R.id.record_tag);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getArguments().getInt(Integer.class.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_record_fragment, container, false);
        scale = getContext().getResources().getDisplayMetrics().density;
        initData();
        initUI(rootView);
        setClick();
        return rootView;
    }

    private void initData() {
        fileBeanList = new ArrayList<>();
        mediaBeanList = new ArrayList<>();
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
                AddRecordFragment.this.bitmap = bitmap;
            }
        };
        thread.start();
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tag_bt) {
            createDialog();

        } else {
            openImageSelectMultiMethod(maxSize);
        }
    }


    private void openImageSelectMultiMethod(int size) {
        RxGalleryFinal rxGalleryFinal = RxGalleryFinal
                .with(getActivity())
                .crop()
                .image()
                .multiple();
        if (mediaBeanList != null && !mediaBeanList.isEmpty()) {
            rxGalleryFinal
                    .selected(mediaBeanList);
        }
        rxGalleryFinal.maxSize(size)
                .imageLoader(ImageLoaderType.PICASSO)
                .subscribe(new RxBusResultDisposable<ImageMultipleResultEvent>() {

                    @Override
                    protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
                        mediaBeanList = imageMultipleResultEvent.getResult();
                        setImage(mediaBeanList);
                        picture_describe.setText(desText + "(" + imageMultipleResultEvent.getResult().size() + "/" + maxSize + ")");
                        ToastUtil.makeToast(getContext(), "已选择" + imageMultipleResultEvent.getResult().size() +
                                "张图片");

                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        ToastUtil.makeToast(getContext(), "OVER");

                    }
                })
                .openGallery();
    }

    private void setImage(List<MediaBean> image) {
        gridLayout.removeAllViews();
        ImageButton otherButton = null;
        FileBean fileBean = null;
        for (int i = 0; i < image.size(); i++) {
            otherButton = new ImageButton(getContext());
            GridLayout.LayoutParams paramsGl = new GridLayout.LayoutParams(
                    new ViewGroup.LayoutParams((int) (100 * scale + 0.5f), (int) (180 * scale + 0.5f)));
            paramsGl.setMargins((int) (2 * scale + 0.5f), 0, 0, 0);
            Picasso.with(getActivity()).load("file://" + image.get(i).getThumbnailBigPath()).into(otherButton);
            otherButton.setBackgroundResource(R.drawable.imagebutton_shape);
            otherButton.setOnClickListener(this);
            otherButton.setOnLongClickListener(this);
            otherButton.setScaleType(ImageButton.ScaleType.FIT_CENTER);
            gridLayout.addView(otherButton, paramsGl);
            fileBean = new FileBean(image.get(i).getThumbnailBigPath(), image.get(i).getOriginalPath(), otherButton);
            fileBeanList.add(fileBean);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        gridLayout.removeView(v);
        int i = 0;
        for (; i < fileBeanList.size(); ++i) {
            if (fileBeanList.get(i).getImageButton().equals(v)) {
                break;
            }
        }
        fileBeanList.remove(i);
        mediaBeanList.remove(i);
        picture_describe.setText(desText + "(" + gridLayout.getChildCount() + "/" + maxSize + ")");
        if (gridLayout.getChildCount() == 0) {
            createImageButton();
        }
        return true;
    }

    private void createImageButton() {
        imageButton = new ImageButton(getContext());
        GridLayout.LayoutParams paramsGl = new GridLayout.LayoutParams(
                new ViewGroup.LayoutParams((int) (100 * scale + 0.5f), (int) (180 * scale + 0.5f)));
        paramsGl.setMargins((int) (2 * scale + 0.5f), 0, 0, 0);
        imageButton.setOnClickListener(this);
        imageButton.setScaleType(ImageButton.ScaleType.FIT_XY);
        imageButton.setBackgroundResource(R.drawable.imagebutton_shape);
        gridLayout.addView(imageButton, paramsGl);
    }
}
