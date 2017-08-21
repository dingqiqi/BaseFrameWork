package com.dingqiqi.baseframework.manager;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dingqiqi.baseframework.R;

/**
 * 图片加载库
 * Created by dingqiqi on 2016/12/28.
 */
public class GlideManager {

    private static GlideManager mGlideManager = new GlideManager();

    public static GlideManager getInstance() {
        return mGlideManager;
    }

    public void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url)
                //DiskCacheStrategy.RESULT 存储修改后的图片
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                //圆角图片
                .transform(new GlideRoundTransform(context, 10))
                //优先级
                .priority(Priority.HIGH)
                //占位
                .placeholder(R.mipmap.ic_launcher)
                //错误
                .error(R.mipmap.ic_launcher)
                //不使用动画
                .dontAnimate()
                .into(imageView);
    }

    /**
     * 加载指定资源id的图片设置到ImageView
     *
     * @param resourceId
     * @param view
     */
    public void LoadImage(Context context, Integer resourceId, ImageView view) {
        Glide.with(context).load(resourceId)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                //圆角图片
                .transform(new GlideRoundTransform(context, 10))
                .placeholder(R.mipmap.ic_launcher)//默认加载的图片
                .error(R.mipmap.ic_launcher)  //加载错误时显示<可指定图片>
                .dontAnimate()
                .into(view);
    }

    /**
     * 启动下载，暂停下载（列表滑动的时候）
     *
     * @param context
     * @param flag    true 启动 false 暂停
     */
    public void startOrPauseRequeat(Context context, boolean flag) {
        if (flag) {
            Glide.with(context).resumeRequests();
        } else {
            Glide.with(context).pauseRequests();
        }
    }

    /**
     * 清除加载图片
     *
     * @param imageView
     */
    public void clearLoad(ImageView imageView) {
        Glide.clear(imageView);
        // 必须在UI线程中调用
        //Glide.get(context).clearMemory();
        // 必须在后台线程中调用
        //Glide.get(context).clearDiskCache();
    }

    /**
     * 用原图的1/10作为缩略图
     *
     * @param context
     * @param imageView
     */
    public void thumbnailImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                //圆角图片
                .transform(new GlideRoundTransform(context, 10))
                .placeholder(R.mipmap.ic_launcher)//默认加载的图片
                .error(R.mipmap.ic_launcher)  //加载错误时显示<可指定图片>
                .dontAnimate()
                .thumbnail(0.1f)
                .into(imageView);
    }

    /**
     * 用其它图片作为缩略图
     *
     * @param context
     * @param imageView
     * @param sourceId  缩略图Id
     */
    public void thumbnailImage(Context context, String url, ImageView imageView, int sourceId) {
        DrawableRequestBuilder<Integer> thumbnailRequest = Glide
                .with(context)
                .load(sourceId);

        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                //圆角图片
                .transform(new GlideRoundTransform(context, 10))
                .placeholder(R.mipmap.ic_launcher)//默认加载的图片
                .error(R.mipmap.ic_launcher)  //加载错误时显示<可指定图片>
                .dontAnimate()
                .thumbnail(thumbnailRequest)
                .into(imageView);
    }

}
