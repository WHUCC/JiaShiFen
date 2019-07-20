package cn.vailing.chunqiu.jiashifen.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


import cn.vailing.chunqiu.jiashifen.R;
import cn.vailing.chunqiu.jiashifen.activity.MainActivity;
import cn.vailing.chunqiu.jiashifen.adapter.RecordAdapter;
import cn.vailing.chunqiu.jiashifen.engine.LockDialogHelper;
import cn.vailing.chunqiu.jiashifen.engine.RecordManager;
import cn.vailing.chunqiu.jiashifen.util.ImmerseUtil;
import cn.vailing.chunqiu.jiashifen.util.MyInterface;
import cn.vailing.chunqiu.jiashifen.util.ProgressDialogUtil;
import yalantis.com.sidemenu.interfaces.ScreenShotable;

/**
 * Created by dream on 2018/7/1.
 */

public class RecordBrowseFragment extends Fragment implements ScreenShotable, View.OnClickListener {
    private Handler hander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 0:
                    myAdapter.notifyDataSetChanged();
                    break;
                default:
                    //do something
                    break;
            }
        }
    };
    private View containerView;
    protected int res;
    private Bitmap bitmap;
    private ListView browse_lv;
    private RecordInfoFragment recordInfoFragment;
    private Button browse_level;
    private Button browse_study;
    private Button browse_date;
    private Button last_day;
    private Button last_week;
    private Button last_mounth;
    private Button classify_tag;
    private RecordAdapter myAdapter;

    public static RecordBrowseFragment newInstance(int resId) {
        RecordBrowseFragment hostPageFragment = new RecordBrowseFragment();
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
//        RecordManager.getInstance().update();
        View rootView = inflater.inflate(R.layout.record_browse_fragment, container, false);
        initUI(rootView);
        initListview();
//        setClick();
        return rootView;
    }


    private void initListview() {
        myAdapter = new RecordAdapter(getContext());
        browse_lv.setAdapter(myAdapter);
        browse_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                replaceRecordInfoFragment(position);
            }
        });
    }

    public ScreenShotable replaceRecordInfoFragment(int position) {

        MainActivity.res = R.drawable.record_info_bk;
        MainActivity.topId = R.drawable.record_info_top;
        MainActivity.color = "#2D7869";
        MainActivity.topTitle.setText(R.string.record_browse);
        ImmerseUtil.setImmerse(getActivity(), MainActivity.color);
        MainActivity.toolbar.setBackgroundResource(MainActivity.topId);
        recordInfoFragment = RecordInfoFragment.newInstance(this.res, RecordManager.getInstance().getRecordDatabases().get(position));
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, recordInfoFragment).commit();
        return recordInfoFragment;
    }

    private void initUI(View rootView) {
        browse_lv = (ListView) rootView.findViewById(R.id.browse_lv);

        browse_date = (Button) rootView.findViewById(R.id.browse_date);
        browse_level = (Button) rootView.findViewById(R.id.browse_level);
        browse_study = (Button) rootView.findViewById(R.id.browse_study);
        last_day = (Button) rootView.findViewById(R.id.last_day);
        last_week = (Button) rootView.findViewById(R.id.last_week);
        last_mounth = (Button) rootView.findViewById(R.id.last_mounth);
        classify_tag = (Button) rootView.findViewById(R.id.classify_tag);

        browse_date.setOnClickListener(this);
        browse_level.setOnClickListener(this);
        browse_study.setOnClickListener(this);
        last_day.setOnClickListener(this);
        last_week.setOnClickListener(this);
        last_mounth.setOnClickListener(this);
        classify_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LockDialogHelper.getInstance().createTagDialog(new LockDialogHelper.MyOnTagClickListener() {
                    @Override
                    public void Text(String text) {

                        RecordManager.getInstance().sortByTag(new MyInterface.onLoadRecordListener() {
                            ProgressDialogUtil progressDialogUtil = new ProgressDialogUtil(getContext(),"稍等","正在整理数据");
                            @Override
                            public void onBegin() {
                                setProgress();
                                progressDialogUtil.show();
                            }

                            @Override
                            public void onEnd() {
                                progressDialogUtil.cancel();
                                hander.sendEmptyMessage(0);
                            }
                        }, text);
                    }
                });
            }
        });
    }

    private void setProgress() {

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
                RecordBrowseFragment.this.bitmap = bitmap;
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
        switch (v.getId()) {
            case R.id.browse_date:
                RecordManager.getInstance().sortByDate();
                break;
            case R.id.browse_level:
                RecordManager.getInstance().sortByToday();
                break;
            case R.id.browse_study:
                RecordManager.getInstance().sortByStudy(RecordManager.getInstance().getRecordDatabases());
                break;
            case R.id.last_day:
                RecordManager.getInstance().sortByDay();
                break;
            case R.id.last_mounth:
                RecordManager.getInstance().sortByMounth();
                break;
            case R.id.last_week:
                RecordManager.getInstance().sortByWeek();
                break;

        }
        myAdapter.notifyDataSetChanged();
    }


}
