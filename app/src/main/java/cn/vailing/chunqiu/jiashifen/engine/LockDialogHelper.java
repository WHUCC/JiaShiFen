package cn.vailing.chunqiu.jiashifen.engine;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.vailing.chunqiu.jiashifen.R;
import cn.vailing.chunqiu.jiashifen.util.CalenderUtil;
import cn.vailing.chunqiu.jiashifen.util.ContentValueUtil;
import cn.vailing.chunqiu.jiashifen.util.FileUtil;
import cn.vailing.chunqiu.jiashifen.util.OtherUtil;
import cn.vailing.chunqiu.jiashifen.util.SpUtil;
import cn.vailing.chunqiu.jiashifen.util.ToastUtil;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

/**
 * Created by dream on 2018/7/14.
 */

public class LockDialogHelper {
    private static LockDialogHelper lockDialogHelper = new LockDialogHelper();
    private Context context;

    public void init(Context context) {
        this.context = context;
    }

    public static LockDialogHelper getInstance() {
        return lockDialogHelper;
    }

    public void createLockDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog alertDialog = builder.create();
        final View view = View.inflate(context, R.layout.lock_dialog, null);
        alertDialog.setView(view);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        final TextView lock_tv = (TextView) view.findViewById(R.id.lock_tv);
        final PatternLockView pattern_lock_view = (PatternLockView) view.findViewById(R.id.pattern_lock_view);

        pattern_lock_view.addPatternLockListener(new PatternLockViewListener() {
            List<PatternLockView.Dot> firstPattern;

            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {

                if (firstPattern == null || firstPattern.size() == 0)//第一次
                {
                    firstPattern = new ArrayList<PatternLockView.Dot>(pattern);
                    lock_tv.setText("请确认密码");

                } else {

                    if (OtherUtil.equalList(firstPattern, pattern)) {
                        ToastUtil.makeToast(context, "密码创建成功");
                        SpUtil.setString(context, ContentValueUtil.LOCK, PatternLockUtils.patternToString(pattern_lock_view, firstPattern));
                        alertDialog.dismiss();
                    } else {

                        ToastUtil.makeToast(context, "密码不同，请重试！");
                        lock_tv.setText("请输入密码");
                    }
                    firstPattern.clear();
                }
                pattern_lock_view.clearPattern();
            }


            @Override
            public void onCleared() {

            }
        });
    }

    public void createUnLockDialog(@NonNull final UnLockListener unLockListener) {
        final String password = SpUtil.getString(context, ContentValueUtil.LOCK, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog alertDialog = builder.create();
        final View view = View.inflate(context, R.layout.lock_dialog, null);
        alertDialog.setView(view);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                unLockListener.onCancel();
            }
        });
        alertDialog.show();
        final TextView lock_tv = (TextView) view.findViewById(R.id.lock_tv);
        final PatternLockView pattern_lock_view = (PatternLockView) view.findViewById(R.id.pattern_lock_view);

        pattern_lock_view.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {

                String nowPattern = PatternLockUtils.patternToString(pattern_lock_view, pattern);
                if (nowPattern.equals(password))
                {

                    unLockListener.onSuccess();
                }
                else
                {
                    unLockListener.onFailure();
                }
                alertDialog.dismiss();
            }


            @Override
            public void onCleared() {

            }
        });
    }

    public void createTagDialog(@NonNull final MyOnTagClickListener myOnTagClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog alertDialog = builder.create();
        final View view = View.inflate(context, R.layout.tag_dialog, null);
        alertDialog.setView(view);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        TagContainerLayout tagContainerLayout = (TagContainerLayout) view.findViewById(R.id.tag_dialog_layout);
        judgeFileExists();
        File file = new File(context.getFilesDir(), "tags");
        String res = FileUtil.readFile(file);
        if (res != null) {
            String[] stings = res.split(ContentValueUtil.DIVIDE);
            for (String s : stings) {
                if (!TextUtils.isEmpty(s)) {
                    tagContainerLayout.addTag(s);
                }
            }
        }
        tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                myOnTagClickListener.Text(text);
                ToastUtil.makeToast(context,text);
                alertDialog.dismiss();
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });
    }
    private void judgeFileExists() {
        File logFile = new File(context.getFilesDir(), "tags");
        if (FileUtil.isEmpty(logFile)) {
            FileUtil.createNew(logFile);
        }
    }
    public interface UnLockListener{
        void onSuccess();
        void onFailure();
        void onCancel();
    }

    public interface MyOnTagClickListener {
        void Text(String text);
    }
//    private PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
//        @Override
//        public void onStarted() {
//            Log.d(getClass().getName(), "Pattern drawing started");
//        }
//
//        @Override
//        public void onProgress(List<PatternLockView.Dot> progressPattern) {
//            Log.d(getClass().getName(), "Pattern progress: " +
//                    PatternLockUtils.patternToString(mPatternLockView, progressPattern));
//        }
//
//        @Override
//        public void onComplete(List<PatternLockView.Dot> pattern) {
//            Log.d(getClass().getName(), "Pattern complete: " +
//                    PatternLockUtils.patternToString(mPatternLockView, pattern));
//        }
//
//        @Override
//        public void onCleared() {
//            Log.d(getClass().getName(), "Pattern has been cleared");
//        }
//    };
}
