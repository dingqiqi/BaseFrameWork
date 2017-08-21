package com.dingqiqi.baseframework.manager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.dingqiqi.baseframework.fragment.FirstFragment;
import com.dingqiqi.baseframework.fragment.FourthFragment;
import com.dingqiqi.baseframework.fragment.SecondFragment;
import com.dingqiqi.baseframework.fragment.ThirdFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * fragment管理类，控制fragment的加载和显示
 */
public class FragmentController {
    private int mContainerId;
    private FragmentManager manager;
    private Map<Integer, Fragment> fragments;

    private FirstFragment mFirstFragment;
    private SecondFragment mSecondFragment;
    private ThirdFragment mThirdFragment;
    private FourthFragment mFourthFragment;


    public FragmentController(FragmentActivity activity, int containerId) {
        manager = activity.getSupportFragmentManager();
        mContainerId = containerId;
        fragments = new HashMap<>();
    }

    public void addDefaultFragments() {
        fragments.put(0, mFirstFragment);
        fragments.put(1, mSecondFragment);
        fragments.put(2, mThirdFragment);
        fragments.put(3, mFourthFragment);

//        FragmentTransaction transaction = manager.beginTransaction();
//        for (Map.Entry<Integer, Fragment> entry : fragments.entrySet()) {
//            transaction.add(mContainerId, entry.getValue());
//        }
//
//        transaction.commitAllowingStateLoss();
    }

    /**
     * 显示一个fragment
     */
    public void showFragment(int key) {
        hideFragments();

        if (fragments.size() <= key) {
            Log.i("aaa", "throw FragmentController");
            return;
        }

        Fragment fragment = fragments.get(key);
        FragmentTransaction transaction = manager.beginTransaction();
        if (fragment == null) {
            switch (key) {
                case 0:
                    mFirstFragment = new FirstFragment();
                    fragments.put(0, mFirstFragment);
                    transaction.add(mContainerId, mFirstFragment);
                    break;
                case 1:
                    mSecondFragment = new SecondFragment();
                    fragments.put(1, mSecondFragment);
                    transaction.add(mContainerId, mSecondFragment);
                    break;
                case 2:
                    mThirdFragment = new ThirdFragment();
                    fragments.put(2, mThirdFragment);
                    transaction.add(mContainerId, mThirdFragment);
                    break;
                case 3:
                    mFourthFragment = new FourthFragment();
                    fragments.put(3, mFourthFragment);
                    transaction.add(mContainerId, mFourthFragment);
                    break;
            }
        } else {
            transaction.show(fragment);
        }

        transaction.commitAllowingStateLoss();
    }

    /**
     * 隐藏所有fragment
     */
    private void hideFragments() {
        FragmentTransaction transaction = manager.beginTransaction();
        for (Map.Entry<Integer, Fragment> entry : fragments.entrySet()) {
            if (entry.getValue() != null) {
                transaction.hide(entry.getValue());
            }
        }

        transaction.commitAllowingStateLoss();
    }
}
