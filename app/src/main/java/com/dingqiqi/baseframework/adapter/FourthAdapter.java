package com.dingqiqi.baseframework.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dingqiqi.baseframework.BuildConfig;
import com.dingqiqi.baseframework.R;
import com.dingqiqi.baseframework.manager.GlideManager;
import com.dingqiqi.baseframework.mode.ImageMode;
import com.dingqiqi.baseframework.view.CircleImageView;

import java.util.List;

/**
 * Created by dingqiqi on 2016/12/30.
 */
public class FourthAdapter extends BaseAdapter {
    private Context mContext;
    private List<ImageMode> mList;

    public FourthAdapter(Context mContext, List<ImageMode> mList) {
        super(mContext, mList);
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public View getCustomItemView(ViewGroup parent, int viewType) {
        if (viewType == BaseAdapter.HEAD_TAG) {

        } else if (viewType == BaseAdapter.BODY_TAG) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_image_layout, parent, false);
            return view;
        } else if (viewType == BaseAdapter.FOOT_TAG) {

        } else if (viewType == BaseAdapter.MORE_TAG) {

        }

        return null;
    }

    @Override
    public ItemHolder getCustomItemHolder(View view, int viewType) {
        if (viewType == BaseAdapter.HEAD_TAG) {

        } else if (viewType == BaseAdapter.BODY_TAG) {
            return new Holder(view);
        } else if (viewType == BaseAdapter.FOOT_TAG) {

        } else if (viewType == BaseAdapter.MORE_TAG) {

        }
        return null;
    }

    @Override
    public void onItemBindHolder(RecyclerView.ViewHolder holder, int position, int viewType) {
        if (viewType == BaseAdapter.HEAD_TAG) {

        } else if (viewType == BaseAdapter.BODY_TAG) {
            Holder customHolder = (Holder) holder;

            customHolder.mTextView.setText(mList.get(position).getTitle() + position);
            GlideManager.getInstance().loadImage(mContext, BuildConfig.URL_IMAGE_SHOW + mList.get(position).getImg(), customHolder.mImageView);
            GlideManager.getInstance().loadImage(mContext, BuildConfig.URL_IMAGE_SHOW + mList.get(position).getImg(), customHolder.mCircleImageView);
        } else if (viewType == BaseAdapter.FOOT_TAG) {

        } else if (viewType == BaseAdapter.MORE_TAG) {

        }
    }

    private class Holder extends ItemHolder {

        private TextView mTextView;
        private ImageView mImageView;
        private CircleImageView mCircleImageView;

        public Holder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.item_tv);
            mImageView = (ImageView) itemView.findViewById(R.id.item_iv);
            mCircleImageView = (CircleImageView) itemView.findViewById(R.id.item_circle_iv);
        }
    }

}
