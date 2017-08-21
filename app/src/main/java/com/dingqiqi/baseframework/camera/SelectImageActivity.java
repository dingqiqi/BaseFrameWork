package com.dingqiqi.baseframework.camera;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.dingqiqi.baseframework.application.CustomApplication;
import com.dingqiqi.baseframework.util.FileUtil;
import com.dingqiqi.baseframework.util.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 根据传入的参数进去照相或者相册的Activity
 */
public class SelectImageActivity extends Activity {
    /**
     * 拍照照片文件
     */
    private File mCurrentPhotoFile;
    /**
     * 图片输出Uri
     */
    private Uri mUri;
    /**
     * 是否裁剪图片
     */
    private boolean mIsCrop = false;
    /**
     * 获取图片方式参数
     */
    public static final String SELECT_IMAGE_TYPE = "type";
    /**
     * 选中拍照
     */
    public static final String SELECT_PHOTO_VALUE = "1";
    /**
     * 选中图库
     */
    public static final String SELECT_FILE_VALUE = "2";
    /**
     * 图片是否裁剪参数
     */
    public static final String SELECT_IMAGE_CROP = "crop";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mCurrentPhotoFile = new File(FileUtil.getImagePath(this),
                getPhotoFileName());

        if (getIntent().getBooleanExtra(SELECT_IMAGE_CROP, false)) {
            mIsCrop = true;
        } else {
            mIsCrop = false;
        }

        //拍照
        if (SELECT_PHOTO_VALUE.equals(getIntent().getStringExtra(SELECT_IMAGE_TYPE))) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                mUri = FileProvider.getUriForFile(SelectImageActivity.this, getPackageName() + ".FileProvide", mCurrentPhotoFile);
            } else {
                mUri = Uri.fromFile(mCurrentPhotoFile);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
            startActivityForResult(intent, 2);
            //相册
        } else if (SELECT_FILE_VALUE.equals(getIntent().getStringExtra(SELECT_IMAGE_TYPE))) {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 3);
        } else {
            ToastUtil.showToast("选择方式不存在!");
            finish();
        }
    }


    ;

    /**
     * 设置拍照获得的照片名字
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'DQQ_IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 3) {
                Uri uri = data.getData();
                if (uri == null) {
                    ToastUtil.showToast("图片获取失败!");
                    return;
                }
                CustomApplication.mFilePath = FileUtil.getImagePath(SelectImageActivity.this, uri);
                if (mIsCrop) {
                    startPhotoZoom(uri);
                }
            } else if (requestCode == 2) {
                if (mCurrentPhotoFile.exists()) {
                    CustomApplication.mFilePath = mCurrentPhotoFile.getPath();
                    if (mIsCrop) {
                        startPhotoZoom(mUri);
                    }
                } else {
                    ToastUtil.showToast("图片获取失败!");
                    return;
                }
            } else if (requestCode == 1) {
                // 拿到剪切数据
                CustomApplication.mFileBitmap = getImageBitmap(SelectImageActivity.this, data);
            }

            // --------------上传或者其他操作
            // 中文名称
            //path = URLDecoder.decode(path);
        }
        finish();
    }

    /**
     * 裁剪图片
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        //申请权限(android N必加,不然报错)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        // 不能超过400
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);

        intent.putExtra("return-data", true);
        startActivityForResult(intent, 1);
    }

    /**
     * 获取bitmap
     *
     * @param context
     * @param intent
     * @return
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
                return null;
            }
        }
        return bitmap;
    }
}
