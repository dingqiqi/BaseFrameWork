package com.dingqiqi.baseframework.activity;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dingqiqi.baseframework.Interface.NavigationListener;
import com.dingqiqi.baseframework.R;
import com.dingqiqi.baseframework.application.CustomApplication;
import com.dingqiqi.baseframework.util.DisplayUtil;
import com.dingqiqi.baseframework.manager.SystemBarTintManager;
import com.dingqiqi.baseframework.view.WaitingDialog;

/**
 * Activity基类
 * Created by 丁奇奇 on 2016/12/26.
 */
public class BaseActivity extends FragmentActivity implements NavigationListener {
    /**
     * 状态栏布局
     */
    private View mNavigationBar;

    private ImageView mIvBack;
    private TextView mTvTitle;
    private ImageView mIvMenu;
    /**
     * 底部相应控件
     */
    private View mLineView;
    public RadioGroup mBottomView;

    /**
     * 状态栏点击监听
     */
    private NavigationListener mListener;

    //点击同一 view 最小的时间间隔，小于这个数则忽略此次单击。
    private static long intervalTime = 800;
    //最后点击时间
    private long lastClickTime = 0;
    //最后被单击的 View 的ID
    private long lastClickView = 0;

    private FragmentActivity mCurActivity = this;
    /**
     * 等待框
     */
    private WaitingDialog mWaitingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT > 11) {
            if (getActionBar() != null) {
                getActionBar().hide();
            }
        }
        //设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //输入法隐藏
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //沉浸式状态栏
        initSystemBar(this);

        CustomApplication.getInstance().addActivity(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.base_activity);
        initBar();

        //初始化导航栏
        mNavigationBar = findViewById(R.id.navigation_bar);
        mIvBack = (ImageView) findViewById(R.id.ib_back);
        mIvMenu = (ImageView) findViewById(R.id.ib_menu);
        mTvTitle = (TextView) findViewById(R.id.tv_title);

        mLineView = findViewById(R.id.bottom_line_view);
        mBottomView = (RadioGroup) findViewById(R.id.bottom_view);

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onBack();
                }
                finish();
            }
        });

        mIvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onMenu();
                }
            }
        });

        //初始化根布局容器
        FrameLayout mBaseContainer = (FrameLayout) findViewById(R.id.base_container);
        ViewGroup.inflate(this, layoutResID, mBaseContainer);

        mBottomView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = 0;
                for (int i = 0; i < group.getChildCount(); i++) {
                    if (group.getChildAt(i) instanceof RadioButton) {
                        RadioButton button = (RadioButton) group.getChildAt(i);
                        if (button != null && button.isChecked()) {
                            if (mListener != null) {
                                mListener.onBottomCheck(index == -1 ? i / 2 : i);
                            }
                            break;
                        }
                    } else {
                        //有竖线
                        index = -1;
                    }
                }
            }
        });
    }

    /**
     * 设置标题
     *
     * @param text
     * @param textSize -1 不改变标题大小
     */
    public void setTitle(String text, int textSize) {
        mTvTitle.setText(text);
        if (textSize != -1) {
            mTvTitle.setTextSize(textSize);
        }
    }

    /**
     * 设置状态栏背景色
     *
     * @param color
     */
    public void setNavigationBarBg(int color) {
        mNavigationBar.setBackgroundColor(color);
    }

    /**
     * 标题栏点击监听
     *
     * @param mListener
     */
    public void setListener(NavigationListener mListener) {
        this.mListener = mListener;
    }

    public void initBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        View view = findViewById(R.id.view);
        if (view != null) {
            if (Build.VERSION.SDK_INT < 19) {
                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
            } else {
                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DisplayUtil.getStatusHeight(this)));
            }
        }
    }

    /**
     * 隐藏导航栏，splash页面，主页面会用到
     */
    protected void hideNavigationBar() {
        mNavigationBar.setVisibility(View.GONE);
    }

    /**
     * 隐藏菜单按键
     */
    protected void hideMenu() {
        mIvMenu.setVisibility(View.INVISIBLE);
    }

    /**
     * 隐藏后退按键
     */
    protected void hideBack() {
        mIvBack.setVisibility(View.INVISIBLE);
    }

    /**
     * 隐藏分组按钮
     */
    protected void hideBottomView() {
        mLineView.setVisibility(View.GONE);
        mBottomView.setVisibility(View.GONE);
    }

    /**
     * 初始化沉浸式状态栏
     *
     * @param activity
     */
    private void initSystemBar(FragmentActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        // 使用颜色资源
        tintManager.setStatusBarTintResource(getStatusColor());
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity
     * @param on
     */
    @TargetApi(19)
    private void setTranslucentStatus(FragmentActivity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 状态栏的颜色
     * 子类可以通过复写这个方法来修改状态栏颜色
     *
     * @return ID
     */
    protected int getStatusColor() {
        return R.color.colorPrimary;
    }

    /**
     * 是否快速多次点击(连续多点击）
     *
     * @param view 被点击view，如果前后是同一个view，则进行双击校验
     * @return 认为是重复点击时返回true。
     */
    private boolean isFastClick(View view) {
        long time = System.currentTimeMillis() - lastClickTime;
        if (time < intervalTime && lastClickView == view.getId()) {
            lastClickTime = System.currentTimeMillis();
            return true;
        }
        lastClickTime = System.currentTimeMillis();
        lastClickView = view.getId();

        return false;
    }

    /**
     * 是否快速多次点击(连续多点击）
     *
     * @return 认为是重复点击时返回true。
     */
    private boolean isFastClick() {
        long time = System.currentTimeMillis() - lastClickTime;

        if (time < intervalTime) {
            lastClickTime = System.currentTimeMillis();
            return true;
        }

        lastClickTime = System.currentTimeMillis();

        return false;
    }

    /*
    * 显示等待框
    */
    public void showWaitingDialog() {
        showWaitingDialog("");
    }

    /**
     * 显示等待框
     */
    public void showWaitingDialog(final String content) {
        if (mCurActivity == null || mCurActivity.isFinishing()) {
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mWaitingDialog != null) {
                    if (mWaitingDialog.getOwnerActivity() != null
                            && mWaitingDialog.getOwnerActivity() == mCurActivity) {
                        mWaitingDialog.setContent(content);
                    } else {
                        initWaitingDialog(content);
                    }
                    if (!mWaitingDialog.isShowing()) {
                        mWaitingDialog.show();
                    }
                } else {
                    initWaitingDialog(content);
                    mWaitingDialog.show();
                }
            }
        });
    }

    /**
     * 初始化WaitingDialog
     */
    private void initWaitingDialog(String content) {
        mWaitingDialog = null;
        mWaitingDialog = new WaitingDialog(mCurActivity, content);
        mWaitingDialog.setCanceledOnTouchOutside(false);
        mWaitingDialog.setCancelable(false);
    }

    /**
     * 隐藏等待框
     */
    public void dismissWaitingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mWaitingDialog != null && mWaitingDialog.isShowing()) {
                    try {
                        mWaitingDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onBack() {

    }

    @Override
    public void onMenu() {

    }

    @Override
    public void onBottomCheck(int position) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.getInstance().removeActivity(this);
    }

}



