package com.dingqiqi.baseframework.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dingqiqi.baseframework.R;
import com.dingqiqi.baseframework.adapter.SecondAdapter;
import com.dingqiqi.baseframework.mode.ImageMode;
import com.dingqiqi.baseframework.view.RecyclerLine;
import com.dingqiqi.baseframework.util.LogUtil;
import com.dingqiqi.baseframework.view.BaseRecyclerRefresh;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/26.
 */
public class SecondFragment extends Fragment implements BaseRecyclerRefresh.BaseCallBack {

    private BaseRecyclerRefresh mRecyclerRefresh;

    private RecyclerLine mRecyLine;

    private List<ImageMode> mList;

    private SecondAdapter mSecondAdapter;

    private Handler mHandler = new Handler();

    private View mHeadView;
    private View mHeadView1;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_layout, null, false);

        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.item_base_head_layout, container, false);
        mHeadView1 = LayoutInflater.from(getActivity()).inflate(R.layout.item_base_head_layout, container, false);

        mRecyclerRefresh = (BaseRecyclerRefresh) view.findViewById(R.id.base_recycle_refresh_second);

        initView();

        return view;
    }

    public void initView() {
        mRecyclerRefresh.getRecyclerView().setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        mRecyLine = new RecyclerLine(getActivity());
        mRecyLine.setOrien(0);
        mRecyclerRefresh.getRecyclerView().addItemDecoration(mRecyLine);

        mList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            ImageMode mode = new ImageMode();
            mode.setText("" + i);
            mList.add(mode);
        }

        mSecondAdapter = new SecondAdapter(getActivity(), mList);
        mSecondAdapter.addHeadView(mHeadView);
        mSecondAdapter.addHeadView(mHeadView1);
        mSecondAdapter.addFooterView(mHeadView);
        mSecondAdapter.addFooterView(mHeadView1);

        mRecyclerRefresh.setAdapter(mSecondAdapter);

        mRecyclerRefresh.setCallBack(this);
    }

    @Override
    public void refresh(BaseRecyclerRefresh.Status status, int curPage) {
        if (status == BaseRecyclerRefresh.Status.REFRESHING) {
            //先停止加载更多线程
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogUtil.i("REFRESHING");
                    mRecyclerRefresh.stopRefresh();
                }
            }, 2000);
        } else if (status == BaseRecyclerRefresh.Status.LOADMORE) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogUtil.i("LOADMORE");
                    mRecyclerRefresh.stopLoadMore();
                }
            }, 2000);
        }
    }

}
