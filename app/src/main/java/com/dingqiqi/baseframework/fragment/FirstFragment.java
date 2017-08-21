package com.dingqiqi.baseframework.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dingqiqi.baseframework.R;
import com.dingqiqi.baseframework.adapter.FirstAdapter;
import com.dingqiqi.baseframework.mode.ImageMode;
import com.dingqiqi.baseframework.view.RecyclerLine;
import com.dingqiqi.baseframework.view.BaseRecyclerRefresh;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/26.
 */
public class FirstFragment extends Fragment implements BaseRecyclerRefresh.BaseCallBack {

    private List<ImageMode> mList;
    private BaseRecyclerRefresh mRecyclerRefresh;
    private FirstAdapter mFirstAdapter;

    private RecyclerLine mRecyLine;

    private View mHeadView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_layout, null, false);
        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.item_base_head_layout, container, false);

        mRecyclerRefresh = (BaseRecyclerRefresh) view.findViewById(R.id.recycler_refresh_first);
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

        mFirstAdapter = new FirstAdapter(getActivity(), mList);
        mFirstAdapter.addHeadView(mHeadView);
        mFirstAdapter.addFooterView(mHeadView);
        mRecyclerRefresh.setAdapter(mFirstAdapter);

        mRecyclerRefresh.addLoadMoreEnable(false);
        mRecyclerRefresh.setRefreshEnable(false);
        mRecyclerRefresh.setCallBack(this);

        return view;
    }

    @Override
    public void refresh(BaseRecyclerRefresh.Status status, int curPage) {
        if (status == BaseRecyclerRefresh.Status.REFRESHING) {
            mRecyclerRefresh.stopRefresh();
        } else if (status == BaseRecyclerRefresh.Status.LOADMORE) {

        }
    }

}
