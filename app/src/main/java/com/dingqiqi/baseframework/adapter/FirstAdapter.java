package com.dingqiqi.baseframework.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dingqiqi.baseframework.mode.BaseMode;

import java.util.List;

/**
 * Created by Administrator on 2017/1/17.
 */
public class FirstAdapter extends BaseAdapter {

    public FirstAdapter(Context mContext, List<? extends BaseMode> mList) {
        super(mContext, mList);
    }

    @Override
    public View getCustomItemView(ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public ItemHolder getCustomItemHolder(View view, int viewType) {
        if (viewType == BaseAdapter.HEAD_TAG) {
            return new HeaderHolder(view);
        } else if (viewType == BaseAdapter.BODY_TAG) {
            return new Holder(view);
        } else if (viewType == BaseAdapter.FOOT_TAG) {

        } else if (viewType == BaseAdapter.MORE_TAG) {

        }
        return null;
    }

    @Override
    public void onItemBindHolder(RecyclerView.ViewHolder holder, int position, int viewType) {

    }

    /**
     * 重写headView
     */
    private class HeaderHolder extends ItemHolder {

        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }
    /**
     * 重写bodyView
     */
    private class Holder extends ItemHolder {

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
