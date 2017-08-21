package com.dingqiqi.baseframework.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 图片压缩
 * Created by 丁奇奇 on 2017/4/28.
 */
public class ImageUtil {

    /**
     * 获取bitmap
     *
     * @param context 上下文
     * @param intent  intent
     * @return bitmap
     */
    public static Bitmap getImageBitmap(Context context, Intent intent) {
        Bitmap bitmap = null;
        if (intent.getExtras() != null) {
            bitmap = (Bitmap) intent.getExtras().get("data");
        } else {
            Uri uri = intent.getData();
            try {
                bitmap = BitmapFactory.decodeStream(context
                        .getContentResolver().openInputStream(uri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    /**
     * 压缩文件
     *
     * @param path      文件路径
     * @param imageView 控件
     * @return 压缩后的bitmap
     */
    public static Bitmap zipImage(String path, ImageView imageView) {
        // 获得图片的宽和高，并不把图片加载到内存中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        ImageSize imageSize = getImageViewSize(imageView);

        options.inSampleSize = caculateInSampleSize(options,
                imageSize.width, imageSize.height);

        // 使用获得到的InSampleSize再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 压缩文件
     *
     * @param inputStream 文件流
     * @param imageView   控件
     * @return 压缩后的bitmap
     */
    public static Bitmap zipImage(InputStream inputStream, ImageView imageView) {
        // 获得图片的宽和高，并不把图片加载到内存中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);

        ImageSize imageSize = getImageViewSize(imageView);

        options.inSampleSize = caculateInSampleSize(options,
                imageSize.width, imageSize.height);

        // 使用获得到的InSampleSize再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(inputStream, null, options);
    }

    /**
     * 根据ImageView获适当的压缩的宽和高
     *
     * @param imageView 显示view
     * @return view大小
     */
    public static ImageSize getImageViewSize(ImageView imageView) {
        ImageSize imageSize = new ImageSize();
        DisplayMetrics displayMetrics = imageView.getContext().getResources()
                .getDisplayMetrics();

        ViewGroup.LayoutParams lp = imageView.getLayoutParams();

        int width = imageView.getWidth();// 获取imageView的实际宽度
        if (width <= 0) {
            width = lp.width;// 获取imageView在layout中声明的宽度
        }

        if (width <= 0) {
            width = displayMetrics.widthPixels;
        }

        int height = imageView.getHeight();// 获取imageView的实际高度
        if (height <= 0) {
            height = lp.height;// 获取imageView在layout中声明的宽度
        }

        if (height <= 0) {
            height = displayMetrics.heightPixels;
        }

        if (width <= 0) {
            width = 100;
        }

        if (height <= 0) {
            height = 100;
        }

        imageSize.width = width;
        imageSize.height = height;

        return imageSize;
    }

    public static class ImageSize {
        int width;
        int height;
    }

    /**
     * 根据需求的宽和高以及图片实际的宽和高计算SampleSize
     *
     * @param options   bitmap的option
     * @param reqWidth  控件宽度
     * @param reqHeight 控件高度
     * @return 返回inSampleSize
     */
    public static int caculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                           int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;

        if (width > reqWidth || height > reqHeight) {
            int widthRadio = Math.round(width * 1.0f / reqWidth);
            int heightRadio = Math.round(height * 1.0f / reqHeight);

            inSampleSize = Math.max(widthRadio, heightRadio);
        }

        return inSampleSize;
    }

    /**
     * 图片特定形状切取
     *
     * @param context 上下文
     * @param bitmap  图片源
     * @param backImg 图片id
     * @return bitmap
     */
    public static Bitmap getShapeBitmap(Context context, Bitmap bitmap, int backImg) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            NinePatchDrawable drawable = (NinePatchDrawable) context.getResources()
                    .getDrawable(backImg);
            if (drawable != null) {
                drawable.setBounds(rect);
                Bitmap first = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas newCanvas = new Canvas(first);
                drawable.draw(newCanvas);
                paint.setAntiAlias(true);
                canvas.drawARGB(0, 0, 0, 0);
                paint.setColor(Color.BLACK);
                canvas.drawBitmap(first, rect, rectF, paint);//切成特定的形状
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
                canvas.drawBitmap(bitmap, rect, rect, paint);

                return output;
            }
        } catch (Exception e) {
            Log.e("CommonUtil", "error", e);
        }

        return null;
    }

}
