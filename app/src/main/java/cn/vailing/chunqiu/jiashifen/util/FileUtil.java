package cn.vailing.chunqiu.jiashifen.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by dream on 2018/7/4.
 */

public class FileUtil {
    public static String readFile(File file) {
        if (isEmpty(file))
            return null;
        String res = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            int length = fis.available();
            byte[] buffer = new byte[length];
            fis.read(buffer);
            res = new String(buffer, "UTF-8");
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void writeFile(File file, String info) {
        try {
            FileOutputStream fos = new FileOutputStream(file);

            fos.write(info.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isEmpty(File file) {
        if (!file.exists()) {
            return true;
        }
        return false;
    }
    public static void createNew(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
