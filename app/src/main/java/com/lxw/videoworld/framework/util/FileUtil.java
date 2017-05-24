package com.lxw.videoworld.framework.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.lxw.dailynews.app.bean.FileInfoBean;
import com.lxw.dailynews.framework.config.Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by lxw9047 on 2016/10/20.
 */

public class FileUtil {
    public static boolean isFileExists(String path) {
        try {
            if (!TextUtils.isEmpty(Constant.PATH__SD_CARD)) {
                File file = new File(path);
                if (file.exists()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets the content:// URI from the given corresponding path to a file
     *
     * @param context
     * @param imageFile
     * @return content Uri
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    /**
     * Gets the corresponding path to a file from the given content:// URI
     *
     * @param selectedVideoUri The content:// URI to find the file path from
     * @param contentResolver  The content resolver to use to perform the query.
     * @return the file path as a string
     */
    public static String getFilePathFromContentUri(Uri selectedVideoUri,
                                                   ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
//      也可用下面的方法拿到cursor
//      Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    //文件按照文件名排序
    public static ArrayList<FileInfoBean> getFileSortByTime(File[] files) {
        ArrayList<FileInfoBean> fileList = new ArrayList<FileInfoBean>();//将需要的子文件信息存入到FileInfo里面
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            FileInfoBean fileInfo = new FileInfoBean();
            fileInfo.name = file.getName();
            fileInfo.path = file.getPath();
            fileInfo.lastModified = file.lastModified();
            fileList.add(fileInfo);
        }
        Collections.sort(fileList, new FileComparator());//通过重写Comparator的实现类FileComparator来实现按文件创建时间排序。
        return fileList;
    }

    //文件名比较类
    public static class FileComparator implements Comparator<FileInfoBean> {
        public int compare(FileInfoBean file1, FileInfoBean file2) {
            if (Integer.valueOf(file1.getName()) > Integer.valueOf(file2.getName())) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    //删除文件/文件夹及子文件
    public static void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        }
    }

}
