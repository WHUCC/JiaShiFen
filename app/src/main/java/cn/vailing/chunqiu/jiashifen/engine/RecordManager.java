package cn.vailing.chunqiu.jiashifen.engine;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.vailing.chunqiu.jiashifen.RecordDatabase;
import cn.vailing.chunqiu.jiashifen.util.CalenderUtil;
import cn.vailing.chunqiu.jiashifen.util.ContentValueUtil;
import cn.vailing.chunqiu.jiashifen.util.FileUtil;
import cn.vailing.chunqiu.jiashifen.util.MyInterface;
import cn.vailing.chunqiu.jiashifen.util.SpUtil;

/**
 * Created by dream on 2018/7/5.
 */

public class RecordManager {
    private static RecordManager recordManager = new RecordManager();
    private final static List<RecordDatabase> recordDatabases = RecordDatabase.listAll(RecordDatabase.class);
    private final static List<RecordDatabase> shouldStudys = new ArrayList<>();
    private static List<RecordDatabase> tempDatabases;
    private Context context;
    private int nowday= CalenderUtil.getInstance().getDayFromOriginal();;

    public List<RecordDatabase> getShouldStudys() {
        return shouldStudys;
    }

    public List<RecordDatabase> getRecordDatabases() {
        return recordDatabases;
    }

    public static RecordManager getInstance() {
        return recordManager;
    }

    public void init(Context context) {
        this.context = context;
        tempDatabases = new ArrayList<>();
        update();
    }

    public void update() {
        shouldStudys.clear();
        if (recordDatabases == null || recordDatabases.size() == 0)
            return;
        sortByStudy(recordDatabases);
        for (RecordDatabase recordDatabase : recordDatabases) {
            if (nowday - recordDatabase.getShouldStudyTime() >= 0) {
                shouldStudys.add(recordDatabase);
            }
        }
        SpUtil.setInt(context, ContentValueUtil.LASTVISITTIEM, nowday);
    }

    private void sort() {
        sortByStudy(recordDatabases);
    }

    public void sortByStudy(List<RecordDatabase> databases) {

        Collections.sort(databases, new Comparator<RecordDatabase>() {
            /*
             * int compare(Person p1, Person p2) 返回一个基本类型的整型，
             * 返回负数表示：p1 小于p2，
             * 返回0 表示：p1和p2相等，
             * 返回正数表示：p1大于p2
             */
            public int compare(RecordDatabase p1, RecordDatabase p2) {
                if (p1.getShouldStudyTime() > p2.getShouldStudyTime()) {
                    return 1;
                }
                if (p1.getShouldStudyTime() == p2.getShouldStudyTime()) {
                    return 0;
                }
                return -1;
            }
        });
    }

    public boolean isNeedStudy() {
        return shouldStudys.size() != 0;
    }

    public void sortByDate() {
        Collections.sort(recordDatabases, new Comparator<RecordDatabase>() {
            /*
             * int compare(Person p1, Person p2) 返回一个基本类型的整型，
             * 返回负数表示：p1 小于p2，
             * 返回0 表示：p1和p2相等，
             * 返回正数表示：p1大于p2
             */
            public int compare(RecordDatabase p1, RecordDatabase p2) {
                if (p1.getCreateDay() > p2.getCreateDay()) {
                    return 1;
                }
                if (p1.getCreateDay() == p2.getCreateDay()) {
                    return 0;
                }
                return -1;
            }
        });
    }

    public void sortByToday() {
        tempDatabases = RecordDatabase.listAll(RecordDatabase.class);
        recordDatabases.clear();
        sortByStudy(tempDatabases);
        for (RecordDatabase recordDatabase : tempDatabases) {
            if (nowday - recordDatabase.getShouldStudyTime() >= 0) {
                recordDatabases.add(recordDatabase);
            }
        }
    }

    public void certain(RecordDatabase recordDatabase) {
        int stage = recordDatabase.getStage() + 1;
        stage = (stage > 7) ? 7 : stage;
        recordDatabase.setStage(stage);
        recordDatabase.setLateVisitDay(CalenderUtil.getInstance().getDayFromOriginal());
        recordDatabase.setShouldStudyTime(CalenderUtil.getInstance().getDayFromOriginal() + ContentValueUtil.reviewStage[stage]);
        recordDatabase.setRightTimes(recordDatabase.getRightTimes() + 1);
        recordDatabase.setVisitTimes(recordDatabase.getVisitTimes() + 1);
        float level = (float) recordDatabase.getRightTimes() / ((float) recordDatabase.getVisitTimes());
        recordDatabase.setCarryLevel(level);
        recordDatabase.update();
    }

    public void vague(RecordDatabase recordDatabase) {
        int stage = recordDatabase.getStage() - 2;
        stage = (stage < 0) ? 0 : stage;
        recordDatabase.setStage(stage);
        recordDatabase.setLateVisitDay(CalenderUtil.getInstance().getDayFromOriginal());
        recordDatabase.setShouldStudyTime(CalenderUtil.getInstance().getDayFromOriginal() + ContentValueUtil.reviewStage[stage]);
        recordDatabase.setVagueTimes(recordDatabase.getRightTimes() + 1);
        recordDatabase.setVisitTimes(recordDatabase.getVisitTimes() + 1);
        float level = (float) recordDatabase.getRightTimes() / ((float) recordDatabase.getVisitTimes());
        recordDatabase.setCarryLevel(level);
        recordDatabase.update();
    }

    public void forget(RecordDatabase recordDatabase) {
        int stage = 0;
        recordDatabase.setStage(stage);
        recordDatabase.setLateVisitDay(CalenderUtil.getInstance().getDayFromOriginal());
        recordDatabase.setShouldStudyTime(CalenderUtil.getInstance().getDayFromOriginal() + ContentValueUtil.reviewStage[stage]);
        recordDatabase.setErrorTimes(recordDatabase.getRightTimes() + 1);
        recordDatabase.setVisitTimes(recordDatabase.getVisitTimes() + 1);
        float level = (float) recordDatabase.getRightTimes() / ((float) recordDatabase.getVisitTimes());
        recordDatabase.setCarryLevel(level);
        recordDatabase.update();
    }

    public void sortByDay() {
        recordDatabases.clear();
        tempDatabases = RecordDatabase.listAll(RecordDatabase.class);
        int nowDay = CalenderUtil.getInstance().getDayFromOriginal();
        for (int i = tempDatabases.size() - 1; i >= 0; --i) {
            if (nowDay - tempDatabases.get(i).getCreateDay() <= 1) {
                recordDatabases.add(tempDatabases.get(i));
            } else {
                return;
            }
        }
    }

    public void sortByMounth() {
        recordDatabases.clear();
        tempDatabases = RecordDatabase.listAll(RecordDatabase.class);
        int nowDay = CalenderUtil.getInstance().getDayFromOriginal();
        for (int i = tempDatabases.size() - 1; i >= 0; --i) {
            if (nowDay - tempDatabases.get(i).getCreateDay() <= 30) {
                recordDatabases.add(tempDatabases.get(i));
            } else {
                return;
            }
        }
    }

    public void sortByWeek() {
        recordDatabases.clear();
        tempDatabases = RecordDatabase.listAll(RecordDatabase.class);
        int nowDay = CalenderUtil.getInstance().getDayFromOriginal();
        for (int i = tempDatabases.size() - 1; i >= 0; --i) {
            if (nowDay - tempDatabases.get(i).getCreateDay() <= 7) {
                recordDatabases.add(tempDatabases.get(i));
            } else {
                return;
            }
        }
    }

    public void sortByTag(@NonNull final MyInterface.onLoadRecordListener onLoadRecordListener, final String text) {
        onLoadRecordListener.onBegin();
        new Thread() {
            @Override
            public void run() {
                super.run();
                recordDatabases.clear();
                tempDatabases = RecordDatabase.listAll(RecordDatabase.class);
                File file = null;
                String res = null;
                int nowDay = CalenderUtil.getInstance().getDayFromOriginal();
                for (int i = tempDatabases.size() - 1; i >= 0; --i) {
                    file = new File(context.getFilesDir(), tempDatabases.get(i).getName() + ContentValueUtil.TAG);
                    res = FileUtil.readFile(file);
                    if (res.contains(text)) {
                        recordDatabases.add(tempDatabases.get(i));
                    }
                }
                onLoadRecordListener.onEnd();
            }
        }.start();

    }

    public void getStudyByTag(@NonNull final MyInterface.onLoadRecordListener onLoadRecordListener, final String tag) {
        onLoadRecordListener.onBegin();
        new Thread() {
            @Override
            public void run() {
                super.run();
                shouldStudys.clear();
                nowday = CalenderUtil.getInstance().getDayFromOriginal();
                tempDatabases = RecordDatabase.listAll(RecordDatabase.class);
                sortByStudy(tempDatabases);
                File file = null;
                String res = null;
                for (RecordDatabase recordDatabase : tempDatabases) {
                    if (nowday - recordDatabase.getShouldStudyTime() >= 0) {
                        file = new File(context.getFilesDir(), recordDatabase.getName() + ContentValueUtil.TAG);
                        res = FileUtil.readFile(file);
                        if (res != null && res.contains(tag)) {

                            shouldStudys.add(recordDatabase);
                        }
                    }
                }
                onLoadRecordListener.onEnd();
            }
        }.start();

    }
}
