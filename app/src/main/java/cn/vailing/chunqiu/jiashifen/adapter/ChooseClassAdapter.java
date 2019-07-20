package cn.vailing.chunqiu.jiashifen.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import cn.vailing.chunqiu.jiashifen.R;
import cn.vailing.chunqiu.jiashifen.RecordDatabase;
import cn.vailing.chunqiu.jiashifen.engine.LockDialogHelper;
import cn.vailing.chunqiu.jiashifen.engine.RecordManager;
import cn.vailing.chunqiu.jiashifen.util.CalenderUtil;
import cn.vailing.chunqiu.jiashifen.util.ContentValueUtil;
import cn.vailing.chunqiu.jiashifen.util.FileUtil;
import cn.vailing.chunqiu.jiashifen.util.ToastUtil;
import co.lujun.androidtagview.TagContainerLayout;

/**
 * Created by dream on 2018/7/4.
 */

public class ChooseClassAdapter extends BaseAdapter {
    public static String[] tags;
    private Context context;


    public ChooseClassAdapter(Context context) {
        this.context = context;
        inti();
    }

    @Override
    public int getCount() {
        return tags.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.choose_class_item, null);
            viewHolder = new ViewHolder();
            viewHolder.choose_item_tag = (TagContainerLayout) view.findViewById(R.id.choose_item_tag);
            viewHolder.choose_ib = (ImageButton) view.findViewById(R.id.choose_ib);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
            viewHolder.choose_item_tag.removeAllTags();

        }
        viewHolder.choose_item_tag.setTags(tags[position]);
        viewHolder.choose_ib.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//
                return true;
            }
        });
        return view;
    }

    class ViewHolder {
        TagContainerLayout choose_item_tag;
        ImageButton choose_ib;

    }

    private void judgeFileExists() {
        File logFile = new File(context.getFilesDir(), "tags");
        if (FileUtil.isEmpty(logFile)) {
            FileUtil.createNew(logFile);
        }
    }

    private void inti() {
        judgeFileExists();
        File file = new File(context.getFilesDir(), "tags");
        String res = FileUtil.readFile(file);
        if (res != null) {
            tags = res.split(ContentValueUtil.DIVIDE);

        }
    }
}