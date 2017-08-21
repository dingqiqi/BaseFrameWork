package com.dingqiqi.baseframework.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dingqiqi.baseframework.BuildConfig;
import com.dingqiqi.baseframework.R;
import com.dingqiqi.baseframework.adapter.FourthAdapter;
import com.dingqiqi.baseframework.http.HttpHelper;
import com.dingqiqi.baseframework.manager.ExecutorManager;
import com.dingqiqi.baseframework.mode.ImageMode;
import com.dingqiqi.baseframework.view.RecyclerLine;
import com.dingqiqi.baseframework.util.LogUtil;
import com.dingqiqi.baseframework.util.ToastUtil;
import com.dingqiqi.baseframework.view.BaseRecyclerRefresh;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/12/26.
 */
public class FourthFragment extends Fragment implements BaseRecyclerRefresh.BaseCallBack {

    private View mView;
    private BaseRecyclerRefresh mRecyclerRefresh;

    private List<ImageMode> mList;

    private FourthAdapter mAdapter;

    private BaseRecyclerRefresh.Status mStatus;

    private HashMap<String, String> mParams;

    private HashMap<String, String> mHeadParams;

    private final int mPageSize = 10;

    private int mTotalPage;

    private Gson mGson;

    private boolean mIsVisible;

    private View mHeadView;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mIsVisible = true;
            mRecyclerRefresh.startRefresh();
        } else {
            mIsVisible = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_fourth_layout, null, false);
        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.item_base_head_layout, container, false);
        initView();

        mRecyclerRefresh.setCallBack(this);
        mRecyclerRefresh.startRefresh();

        return mView;
    }

    private void initView() {
        mRecyclerRefresh = (BaseRecyclerRefresh) mView.findViewById(R.id.base_recycler_refresh_fourth);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerRefresh.getRecyclerView().setLayoutManager(linearLayoutManager);

        RecyclerLine recyLine = new RecyclerLine(getActivity());
        recyLine.setOrien(0);
        mRecyclerRefresh.getRecyclerView().addItemDecoration(recyLine);

        mList = new ArrayList<>();
        mAdapter = new FourthAdapter(getActivity(), mList);
        mAdapter.addHeadView(mHeadView);
//        mAdapter.addFooterView(mHeadView);

        mRecyclerRefresh.setAdapter(mAdapter);
        //必须在设置adapter之后
        //mRecyclerRefresh.addFooterView();

        mGson = new Gson();
        mParams = new HashMap<>();
        mHeadParams = new HashMap<>();

        mRecyclerRefresh.setInitPage(1);
    }

    @Override
    public void refresh(BaseRecyclerRefresh.Status status, int curPage) {
        mStatus = status;
        if (status == BaseRecyclerRefresh.Status.LOADMORE) {
            LogUtil.i("LOADMORE");
            getImageList(true);
        } else if (status == BaseRecyclerRefresh.Status.REFRESHING) {
            LogUtil.i("REFRESHING");
            getImageList(false);
        }
    }

    private void getImageList(boolean flag) {
        mParams.put("page", "" + mRecyclerRefresh.getCurrentPage());
        mParams.put("rows", "" + mPageSize);

        HttpHelper.getInstance().invokeGet(getActivity(), BuildConfig.URL_IMAGE_LIST, mParams, mHeadParams, new HttpHelper.RequestBack() {
            @Override
            public void onSuccess(String result) {
                closeRefreshOrLoad(false, mList.size());

                if ("".equals(result)) {
                    ToastUtil.showToast("返回空!");
                    return;
                }

                try {
                    JSONObject object = new JSONObject(result);
                    mTotalPage = Integer.parseInt(object.getString("total"));

                    if (mStatus == BaseRecyclerRefresh.Status.REFRESHING) {
                        mRecyclerRefresh.setLoadMoreEnable(true);
                        mList.clear();
                    }

                    if (mRecyclerRefresh.getCurrentPage() >= mTotalPage) {
                        mRecyclerRefresh.setLoadMoreEnable(false);
                    }

                    mList.addAll(mGson.fromJson(result, ImageMode.class).getTngou());
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(final String result) {
                closeRefreshOrLoad(true, mList.size());
                ToastUtil.showToast(getActivity(), result);
            }
        }, flag);
    }

    /**
     * 关闭加载界面
     *
     * @param isLoadFail 加载失败
     */
    public void closeRefreshOrLoad(final boolean isLoadFail, final int position) {
        if (mStatus == BaseRecyclerRefresh.Status.REFRESHING) {
            mRecyclerRefresh.stopRefresh();
        } else if (mStatus == BaseRecyclerRefresh.Status.LOADMORE) {
            if (isLoadFail) {
                mRecyclerRefresh.stopErrorLoad();
            } else {
                mRecyclerRefresh.stopLoadMore();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ExecutorManager.getInstance().stopRequest();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ExecutorManager.getInstance().stopRequest();
    }
}
