package cn.vailing.chunqiu.jiashifen.util;

import java.util.List;

/**
 * Created by dream on 2018/7/14.
 */

public class OtherUtil {
    public static  boolean equalList(List list1, List list2) {
        if (list1.size() != list2.size())
            return false;
        for(int i = 0;i<list1.size();++i)
        {
            if (!list1.get(i).equals(list2.get(i)))
            {
                return false;
            }
        }

        return true;
    }
}
