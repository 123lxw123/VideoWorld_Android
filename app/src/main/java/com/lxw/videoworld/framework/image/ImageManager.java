package com.lxw.videoworld.framework.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.lxw.videoworld.framework.util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Zion on 2016/10/15.
 *
 * @描述 : Glide加载图片的封装，圆形、圆角，模糊等处理操作用到了jp.wasabeef:glide-transformations:2.0.1
 * Glide默认使用httpurlconnection协议，可以配置为OkHttp
 */
public class ImageManager {

    private static ImageManager mInstance;
    private static boolean flag_loadImage = true;

    private ImageManager() {
    }

    public static ImageManager getInstance() {
        if (mInstance == null) {
            synchronized (ImageManager.class) {
                if (mInstance == null) {
                    mInstance = new ImageManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 常量
     */
    static class Contants {
        public static final int BLUR_VALUE = 20; //模糊
        public static final int CORNER_RADIUS = 20; //圆角
        public static final float THUMB_SIZE = 0.1f; //0-1之间  10%原图的大小
    }

    /**
     * 常规加载图片
     *
     * @param context
     * @param imageView 图片容器
     * @param imgUrl    图片地址
     */
    public void loadImage(Context context, ImageView imageView, String imgUrl) {
        if (flag_loadImage) {
            Glide.with(context)
                    .load(imgUrl)
                    .crossFade()
                    .priority(Priority.NORMAL) //下载的优先级
                    //all:缓存源资源和转换后的资源 none:不作任何磁盘缓存
                    //source:缓存源资源   result：缓存转换后的资源
                    .diskCacheStrategy(DiskCacheStrategy.RESULT) //缓存策略
                    .into(imageView);
        }
    }

    /**
     * 常规加载图片
     *
     * @param context
     * @param imageView 图片容器
     * @param imgUrl    图片地址
     */
    public void loadImage(Context context, ImageView imageView, String imgUrl, boolean flag_cache) {
        if (flag_loadImage) {
            if(flag_cache){
                Glide.with(context)
                        .load(imgUrl)
                        .crossFade()
                        .priority(Priority.NORMAL) //下载的优先级
                        //all:缓存源资源和转换后的资源 none:不作任何磁盘缓存
                        //source:缓存源资源   result：缓存转换后的资源
                        .diskCacheStrategy(DiskCacheStrategy.RESULT) //缓存策略
                        .into(imageView);
            }else{
                Glide.with(context)
                        .load(imgUrl)
                        .crossFade()
                        .priority(Priority.NORMAL) //下载的优先级
                        //all:缓存源资源和转换后的资源 none:不作任何磁盘缓存
                        //source:缓存源资源   result：缓存转换后的资源
                        .diskCacheStrategy(DiskCacheStrategy.RESULT) //缓存策略
                        .into(imageView);
            }
        }
    }

    /**
     * 常规加载图片
     *
     * @param context
     * @param imageView  图片容器
     * @param imgUrl     图片地址
     * @param errorImgId 加载错误显示图片资源id
     */
    public void loadImage(Context context, ImageView imageView, String imgUrl, int errorImgId) {
        if (flag_loadImage) {
            Glide.with(context)
                    .load(imgUrl)
                    .error(errorImgId)
                    .crossFade()
                    .priority(Priority.NORMAL) //下载的优先级
                    //all:缓存源资源和转换后的资源 none:不作任何磁盘缓存
                    //source:缓存源资源   result：缓存转换后的资源
                    .diskCacheStrategy(DiskCacheStrategy.RESULT) //缓存策略
                    .into(imageView);
        }
    }
    /**
     * 常规加载图片
     *
     * @param context
     * @param imageView  图片容器
     * @param imgUrl     图片地址
     * @param errorImgId 加载错误显示图片资源id
     */
    public void loadImage(Context context, ImageView imageView, String imgUrl, int errorImgId, boolean flag_cache) {
        if (flag_loadImage) {
            if(flag_cache){
                Glide.with(context)
                        .load(imgUrl)
                        .error(errorImgId)
                        .crossFade()
                        .priority(Priority.NORMAL) //下载的优先级
                        //all:缓存源资源和转换后的资源 none:不作任何磁盘缓存
                        //source:缓存源资源   result：缓存转换后的资源
                        .diskCacheStrategy(DiskCacheStrategy.RESULT) //缓存策略
                        .into(imageView);
            }else{
                Glide.with(context)
                        .load(imgUrl)
                        .error(errorImgId)
                        .crossFade()
                        .priority(Priority.NORMAL) //下载的优先级
                        //all:缓存源资源和转换后的资源 none:不作任何磁盘缓存
                        //source:缓存源资源   result：缓存转换后的资源
                        .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存策略
                        .into(imageView);
            }
        }
    }

    /**
     * 常规下载图片
     *
     * @param context
     * @param imgUrl   图片地址
     * @param path     图片保存路径
     * @param fileName 图片保存文件名
     */
    public void downloadImage(Context context, String imgUrl, String path, String fileName) {
        downloadImage(context, imgUrl, path, fileName, false);
    }

    /**
     * 常规下载图片
     *
     * @param context
     * @param imgUrl     图片地址
     * @param path       图片保存路径
     * @param fileName   图片保存文件名
     * @param isOverride 是否覆盖下载
     */
    public void downloadImage(Context context, String imgUrl, String path, String fileName, boolean isOverride) {
        try {
            File file = null;
            if (FileUtil.isFileExists(path + fileName)) {
                if (!isOverride) {
                    return;
                }
                file = new File(path + fileName);
                file.delete();
            } else {
                if (!FileUtil.isFileExists(path)) {
                    new File(path).mkdirs();
                }
            }
            if (file == null) {
                file = new File(path + fileName);
            }
            Bitmap bitmap = Glide.with(context)
                    .load(imgUrl)
                    .asBitmap()
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
            if (bitmap != null) {
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 常规下载图片
     *
     * @param context
     * @param imgUrl  图片地址
     */
    public Bitmap downloadImage(Context context, String imgUrl) {
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context)
                    .load(imgUrl)
                    .asBitmap()
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 加载缩略图
     *
     * @param context
     * @param imageView 图片容器
     * @param imgUrl    图片地址
     */
    public void loadThumbnailImage(Context context, ImageView imageView, String imgUrl) {
        if (flag_loadImage) {
            Glide.with(context)
                    .load(imgUrl)
                    .crossFade()
                    .priority(Priority.NORMAL) //下载的优先级
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                    .thumbnail(Contants.THUMB_SIZE)
                    .into(imageView);
        }
    }

    /**
     * 加载图片并设置为指定大小
     *
     * @param context
     * @param imageView
     * @param imgUrl
     * @param withSize
     * @param heightSize
     */
    public void loadOverrideImage(Context context, ImageView imageView,
                                  String imgUrl, int withSize, int heightSize) {
        if (flag_loadImage) {
            Glide.with(context)
                    .load(imgUrl)
                    .crossFade()
                    .priority(Priority.NORMAL) //下载的优先级
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE) //缓存策略
                    .override(withSize, heightSize)
                    .into(imageView);
        }
    }

    /**
     * 加载图片并对其进行模糊处理
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadBlurImage(Context context, ImageView imageView, String imgUrl) {
        if (flag_loadImage) {
            Glide.with(context)
                    .load(imgUrl)
                    .crossFade()
                    .priority(Priority.NORMAL) //下载的优先级
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                    .bitmapTransform(new BlurTransformation(context, Contants.BLUR_VALUE))
                    .into(imageView);
        }
    }

    /**
     * 加载圆图
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadCircleImage(Context context, ImageView imageView, String imgUrl) {
        if (flag_loadImage) {
            Glide.with(context)
                    .load(imgUrl)
                    .crossFade()
                    .priority(Priority.NORMAL) //下载的优先级
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(imageView);
        }
    }

    /**
     * 加载模糊的圆角的图片
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadBlurCircleImage(Context context, ImageView imageView, String imgUrl) {
        if (flag_loadImage) {
            Glide.with(context)
                    .load(imgUrl)
                    .crossFade()
                    .priority(Priority.NORMAL) //下载的优先级
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                    .bitmapTransform(
                            new BlurTransformation(context, Contants.BLUR_VALUE),
                            new CropCircleTransformation(context))
                    .into(imageView);
        }
    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadCornerImage(Context context, ImageView imageView, String imgUrl) {
        if (flag_loadImage) {
            Glide.with(context)
                    .load(imgUrl)
                    .crossFade()
                    .priority(Priority.NORMAL) //下载的优先级
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                    .bitmapTransform(
                            new RoundedCornersTransformation(
                                    context, Contants.CORNER_RADIUS, Contants.CORNER_RADIUS))
                    .into(imageView);
        }
    }

    /**
     * 加载模糊的圆角图片
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadBlurCornerImage(Context context, ImageView imageView, String imgUrl) {
        if (flag_loadImage) {
            Glide.with(context)
                    .load(imgUrl)
                    .crossFade()
                    .priority(Priority.NORMAL) //下载的优先级
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                    .bitmapTransform(
                            new BlurTransformation(context, Contants.BLUR_VALUE),
                            new RoundedCornersTransformation(
                                    context, Contants.CORNER_RADIUS, Contants.CORNER_RADIUS))
                    .into(imageView);
        }
    }

    /**
     * 同步加载图片
     *
     * @param context
     * @param imgUrl
     * @param target
     */
    public void loadBitmapSync(Context context, String imgUrl, SimpleTarget<Bitmap> target) {
        if (flag_loadImage) {
            Glide.with(context)
                    .load(imgUrl)
                    .asBitmap()
                    .priority(Priority.NORMAL) //下载的优先级
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                    .into(target);
        }
    }

    /**
     * 加载gif
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadGifImage(Context context, ImageView imageView, String imgUrl) {
        if (flag_loadImage) {
            Glide.with(context)
                    .load(imgUrl)
                    .asGif()
                    .crossFade()
                    .priority(Priority.NORMAL) //下载的优先级
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                    .into(imageView);
        }
    }

    /**
     * 加载gif的缩略图
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadGifThumbnailImage(Context context, ImageView imageView, String imgUrl) {
        if (flag_loadImage) {
            Glide.with(context)
                    .load(imgUrl)
                    .asGif()
                    .crossFade()
                    .priority(Priority.NORMAL) //下载的优先级
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                    .thumbnail(Contants.THUMB_SIZE)
                    .into(imageView);
        }
    }

    /**
     * 恢复请求，一般在停止滚动的时候
     *
     * @param context
     */
    public void resumeRequests(Context context) {
        Glide.with(context).resumeRequests();
    }

    /**
     * 暂停请求 正在滚动的时候
     *
     * @param context
     */
    public void pauseRequests(Context context) {
        Glide.with(context).pauseRequests();
    }

    /**
     * 清除磁盘缓存
     *
     * @param context
     */
    public void clearDiskCache(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();//清理磁盘缓存 需要在子线程中执行
            }
        }).start();
    }

    /**
     * 清除内存缓存
     *
     * @param context
     */
    public void clearMemory(Context context) {
        Glide.get(context).clearMemory();//清理内存缓存  可以在UI主线程中进行
    }

    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    public String getCacheSize(Context context) {
        try {
            return getFormatSize(getFolderSize(new File(context.getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    private long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    private static String getFormatSize(double size) {

        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }
}