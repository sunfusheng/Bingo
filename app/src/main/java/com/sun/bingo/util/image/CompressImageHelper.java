package com.sun.bingo.util.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;

import com.sun.bingo.util.image.i.IRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片压缩类
 *
 * @author admin
 */
public class CompressImageHelper {

    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap =
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
                        true);
        return resizedBitmap;
    }

    public static void deleteFile(final String filePath) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    new File(filePath).deleteOnExit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void compressImage(Context context, List<String> files, final IRequest<List<String>> iRequest) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            String str = files.get(i);
            if (str.startsWith("http")) {
                list.add(str);
                continue;
            }
            File file = new File(str);
            if (file.length() >= 500 * 1024) { // 文件大于500K，进行压缩
                String path = CommonParameter.getThumbnailCacheFile(context) + File.separator + System.currentTimeMillis() + ".jpg";
                try {
                    compressImage(file.getAbsolutePath(), path);
                    list.add(path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                list.add(str);
            }
        }
        iRequest.request(list);
    }

    //压缩图片
    public static String compressImageView(Context context, String imagePath) {
        if (TextUtils.isEmpty(imagePath)) {
            return "";
        }
        if (imagePath.startsWith("http")) {
            return imagePath;
        }
        File file = new File(imagePath);
        if (file.length() >= 500 * 1024) { // 文件大于500K，进行压缩
            try {
                String path = CommonParameter.getThumbnailCacheFile(context) + File.separator + System.currentTimeMillis() + ".jpg";
                compressImage(file.getAbsolutePath(), path);
                return path;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return imagePath;
    }

    /**
     * 异步压缩图片
     *
     * @param filePath
     * @return 文件大于100K，进行压缩
     */
    public static void compressImage(Context context, String filePath,
                                     final IRequest<String> iRequest) {
        final File file = new File(filePath);
        System.out.println("文件大小:" + file.length());
        if (file.length() >= 500 * 1024) {// 文件大于500K，进行压缩
            final String path =
                    CommonParameter.getThumbnailCacheFile(context) + File.separator
                            + System.currentTimeMillis() + ".jpg";
            try {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        compressImage(file.getAbsolutePath(), path);
                        iRequest.request(path);
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            iRequest.request(filePath);
        }
    }

    public static boolean mkFiledir(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return file.mkdir();
        }
        return false;
    }

    /**
     * 对图片进行压缩
     */
    private static void compressImage2(String srcPath, String targetFileName) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 1280f;// 这里设置高度为800f
        float ww = 1280f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        while (true) {
            newOpts.inSampleSize = be;// 设置缩放比例
            try {
                bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
                // bitmap = compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
                break;
            } catch (OutOfMemoryError e) {// 如果解析错误，则对处理压缩比例处理
                e.printStackTrace();
                be++;
            }
        }

        Matrix m = new Matrix();
        m.setRotate(readPictureDegree(srcPath)); // 旋转angle度
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);// 从新生成图片

        File file = new File(targetFileName);

        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void compressImage(String srcPath, String targetFileName) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 1280f;// 这里设置高度为800f
        float ww = 1280f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        while (true) {
            System.out.println("sample size：" + be);
            newOpts.inSampleSize = be;// 设置缩放比例
            try {
                bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
                // bitmap = compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
                break;
            } catch (OutOfMemoryError e) {// 如果解析错误，则对处理压缩比例处理
                System.out.println("重绘1...");
                e.printStackTrace();
                be++;
            }
        }

        Matrix m = new Matrix();
        m.setRotate(readPictureDegree(srcPath)); // 旋转angle度
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);// 从新生成图片

        File file = new File(targetFileName);

        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void saveBitmap(String targetFileName, Bitmap bitmap) {
        File file = new File(targetFileName);

        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100 && options > 0) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 把ByteArrayInputStream数据生成图片
        return BitmapFactory.decodeStream(isBm, null, opts);
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation =
                    exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return degree;
        }
        return degree;
    }

}
