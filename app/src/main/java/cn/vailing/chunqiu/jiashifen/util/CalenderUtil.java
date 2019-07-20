package cn.vailing.chunqiu.jiashifen.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dream on 2018/7/4.
 */
//3600000
public class CalenderUtil {
    private static CalenderUtil calenderUtil = new CalenderUtil();
    private long trans = 86400000;
    private SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
    private SimpleDateFormat createDate = new SimpleDateFormat("yyyy-MM-dd");

    public static CalenderUtil getInstance() {
        return calenderUtil;
    }

    public int getDayFromOriginal() {
        return (int) (System.currentTimeMillis() / trans);
    }

    public String getDateName() {
        long now = System.currentTimeMillis();
        return date.format(now);
    }

    public String changeToDate(int createDay) {
        long now =createDay*trans;
        return createDate.format(now);
    }
}
