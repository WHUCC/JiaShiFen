package cn.vailing.chunqiu.jiashifen.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;

import cn.vailing.chunqiu.jiashifen.R;
import cn.vailing.chunqiu.jiashifen.activity.MainActivity;
import cn.vailing.chunqiu.jiashifen.adapter.ChooseClassAdapter;
import cn.vailing.chunqiu.jiashifen.engine.RecordManager;
import cn.vailing.chunqiu.jiashifen.util.ContentValueUtil;
import cn.vailing.chunqiu.jiashifen.util.FileUtil;
import cn.vailing.chunqiu.jiashifen.util.ImmerseUtil;
import cn.vailing.chunqiu.jiashifen.util.MyInterface;
import cn.vailing.chunqiu.jiashifen.util.ProgressDialogUtil;
import cn.vailing.chunqiu.jiashifen.util.ToastUtil;
import co.lujun.androidtagview.TagContainerLayout;
import yalantis.com.sidemenu.interfaces.ScreenShotable;


/**
 * Created by Konstantin on 22.12.2014.
 */
public class ChooseClassFragment extends Fragment implements ScreenShotable {
    private Handler hander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 0:
                    replaceHostPageFragment();
                    break;
                default:
                    //do something
                    break;
            }
        }
    };
    private View containerView;
    private ListView choose_lv;
    protected int res;
    private Bitmap bitmap;
    private ChooseClassAdapter chooseClassAdapter;
    private HostPageFragment hostPageFragment;
    public static ChooseClassFragment newInstance(int resId) {
        ChooseClassFragment contentFragment = new ChooseClassFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Integer.class.getName(), resId);
        contentFragment.setArguments(bundle);
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
        View rootView = inflater.inflate(R.layout.choose_class_fragment, container, false);
        initUI(rootView);
        initListView();
        init();

        return rootView;
    }

    private void init() {
//        if (RecordManager.getInstance().isNeedStudy()) {
//            MainActivity.topTitle.setText("选择复习科目");
//        } else {
//            MainActivity.topTitle.setText("当日复习计划完成");
//            choose_lv.setVisibility(View.INVISIBLE);
//        }
    }

    private void initListView() {
        chooseClassAdapter  =new ChooseClassAdapter(getContext());
        choose_lv.setAdapter(chooseClassAdapter);
        choose_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            ProgressDialogUtil progressDialogUtil = new ProgressDialogUtil(getContext(),"稍等","正在整理数据!");
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

              RecordManager.getInstance().getStudyByTag(new MyInterface.onLoadRecordListener() {
                  @Override
                  public void onBegin() {
                      progressDialogUtil.show();
                  }

                  @Override
                  public void onEnd() {
                      progressDialogUtil.cancel();
                      hander.sendEmptyMessage(0);
                  }
              }, ChooseClassAdapter.tags[position]);
            }
        });
    }
    public ScreenShotable replaceHostPageFragment() {
        MainActivity.res = R.drawable.record_info_bk;
        hostPageFragment = HostPageFragment.newInstance(this.res);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, hostPageFragment).commit();
        return hostPageFragment;
    }

    private void initUI(View rootView) {
        choose_lv = (ListView) rootView.findViewById(R.id.choose_lv);
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
                ChooseClassFragment.this.bitmap = bitmap;
            }
        };

        thread.start();

    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }
}

