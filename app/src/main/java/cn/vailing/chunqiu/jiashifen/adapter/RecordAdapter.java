package cn.vailing.chunqiu.jiashifen.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.devs.squaremenu.OnMenuClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import cn.vailing.chunqiu.jiashifen.R;
import cn.vailing.chunqiu.jiashifen.RecordDatabase;
import cn.vailing.chunqiu.jiashifen.engine.LockDialogHelper;
import cn.vailing.chunqiu.jiashifen.engine.RecordManager;
import cn.vailing.chunqiu.jiashifen.util.CalenderUtil;
import cn.vailing.chunqiu.jiashifen.util.ContentValueUtil;
import cn.vailing.chunqiu.jiashifen.util.FileUtil;
import cn.vailing.chunqiu.jiashifen.util.ToastUtil;
import cn.vailing.chunqiu.jiashifen.view.MySquareMenu;
import co.lujun.androidtagview.TagContainerLayout;

/**
 * Created by dream on 2018/7/4.
 */

public class RecordAdapter extends BaseAdapter {
    List<RecordDatabase> recordDatabases;//已经排好序了
    private Context context;


    public RecordAdapter(Context context) {
        this.context = context;
        this.recordDatabases = RecordManager.getInstance().getRecordDatabases();
        inti();
    }

    @Override
    public int getCount() {
        return recordDatabases.size();
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
            view = LayoutInflater.from(context).inflate(R.layout.record_item, null);
            viewHolder = new ViewHolder();
            viewHolder.item_tv = (TextView) view.findViewById(R.id.item_tv);
            viewHolder.item_tag = (TagContainerLayout) view.findViewById(R.id.item_tag);
            viewHolder.item_delete = (ImageButton) view.findViewById(R.id.item_delete);
            viewHolder.item_createDay = (TextView) view.findViewById(R.id.item_createDay);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
            viewHolder.item_tag.removeAllTags();
        }
        viewHolder.item_tv.setText(recordDatabases.get(position).getTitle());
        String date = CalenderUtil.getInstance().changeToDate(recordDatabases.get(position).getCreateDay());
        viewHolder.item_createDay.setText(date);
        String name = recordDatabases.get(position).getName();
        File file = new File(context.getFilesDir(), name + ContentValueUtil.TAG);
        String res = FileUtil.readFile(file);
        if (!TextUtils.isEmpty(res)) {
            String[] stings = res.split(ContentValueUtil.DIVIDE);
            for (String s : stings) {
                if (!TextUtils.isEmpty(s)) {
                    viewHolder.item_tag.addTag(s);
                }
            }
        }
        viewHolder.item_delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final RecordDatabase recordDatabase = recordDatabases.get(position);
                LockDialogHelper.getInstance().createUnLockDialog(new LockDialogHelper.UnLockListener() {
                    @Override
                    public void onSuccess() {
                        recordDatabases.remove(recordDatabase);
                        recordDatabase.delete();
                        notifyDataSetChanged();

                    }

                    @Override
                    public void onFailure() {
                        ToastUtil.makeToast(context,"密码错误！");
                    }

                    @Override
                    public void onCancel() {
                        ToastUtil.makeToast(context,"取消！");
                    }
                });
                return true;
            }
        });
        return view;
    }

    class ViewHolder {
        TextView item_tv;
        TagContainerLayout item_tag;
        ImageButton item_delete;
        TextView item_createDay;
    }

    private void inti() {
    }
}