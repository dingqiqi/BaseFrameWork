package com.dingqiqi.baseframework.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dingqiqi.baseframework.BuildConfig;
import com.dingqiqi.baseframework.R;
import com.dingqiqi.baseframework.manager.GlideManager;
import com.dingqiqi.baseframework.view.CircleImageView;

/**
 * Created by Administrator on 2016/12/26.
 */
public class ThirdFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third_layout, null, false);

        TextView tv = (TextView) view.findViewById(R.id.textview);
        tv.setText("第3页");

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.circle_imageview);

       // GlideManager.getInstance().loadImage(getActivity(), "http://www.people.com.cn/mediafile/pic/20161226/77/10269249654204058753.jpg", imageView);
       // GlideManager.getInstance().loadImage(getActivity(), "http://www.people.com.cn/mediafile/pic/20161226/77/10269249654204058753.jpg", circleImageView);

        GlideManager.getInstance().loadImage(getActivity(), BuildConfig.URL_IMAGE_SHOW + "/ext/161016/3f2ab53c286b8a5c3949616bafc805ca.jpg", imageView);
        GlideManager.getInstance().loadImage(getActivity(), BuildConfig.URL_IMAGE_SHOW + "/ext/161016/3f2ab53c286b8a5c3949616bafc805ca.jpg", circleImageView);

        return view;
    }

}
