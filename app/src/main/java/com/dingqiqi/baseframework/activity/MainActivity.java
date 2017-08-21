package com.dingqiqi.baseframework.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.dingqiqi.baseframework.R;
import com.dingqiqi.baseframework.camera.SelectImageActivity;
import com.dingqiqi.baseframework.camera.SelectImageDialog;
import com.dingqiqi.baseframework.manager.FragmentController;

public class MainActivity extends BaseActivity {

    private FragmentController mFragmentController;

    private SelectImageDialog mSelectImageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("测试Title", -1);
        mFragmentController = new FragmentController(this, R.id.frameLayout);
        mFragmentController.addDefaultFragments();

        hideBack();
        setListener(this);

        //默认选中第一个
        ((RadioButton) mBottomView.getChildAt(0)).setChecked(true);

        mSelectImageDialog = new SelectImageDialog();
    }

    @Override
    public void onBack() {
        Log.i("aaa", "back");
    }

    @Override
    public void onMenu() {
        Log.i("aaa", "menu");

        mSelectImageDialog.setListener(mClickListener);
        mSelectImageDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onBottomCheck(int position) {
        mFragmentController.showFragment(position);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, SelectImageActivity.class);

            switch (v.getId()) {
                case R.id.btn_photos:
                    mSelectImageDialog.dismiss();
                    //获取方式
                    intent.putExtra(SelectImageActivity.SELECT_IMAGE_TYPE, SelectImageActivity.SELECT_PHOTO_VALUE);
                    //是否裁剪
                    intent.putExtra(SelectImageActivity.SELECT_IMAGE_CROP, true);
                    startActivity(intent);
                    break;
                case R.id.btn_file:
                    mSelectImageDialog.dismiss();
                    //获取方式
                    intent.putExtra(SelectImageActivity.SELECT_IMAGE_TYPE, SelectImageActivity.SELECT_FILE_VALUE);
                    //是否裁剪
                    intent.putExtra(SelectImageActivity.SELECT_IMAGE_CROP, true);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };
}
