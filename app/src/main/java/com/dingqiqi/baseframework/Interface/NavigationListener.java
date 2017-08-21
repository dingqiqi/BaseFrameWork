package com.dingqiqi.baseframework.Interface;

/**
 * toolbar点击回调
 * Created by 丁奇奇 on 2017/8/21.
 */
public interface NavigationListener {
    void onBack();

    void onMenu();

    void onBottomCheck(int position);
}
