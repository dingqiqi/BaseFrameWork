package com.dingqiqi.baseframework.camera;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.dingqiqi.baseframework.R;

public class SelectImageDialog extends DialogFragment {

    private Button mBtnPhoto;
    private Button mBtnFile;

    private View.OnClickListener mListener;

    @SuppressWarnings("deprecation")
    @Override
    public void onStart() {
        super.onStart();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();

//		params.width = getDialog().getWindow().getWindowManager()
//				.getDefaultDisplay().getWidth() * 4 / 5;

        params.width = getDialog().getWindow().getWindowManager()
                .getDefaultDisplay().getWidth();

        getDialog().getWindow().setAttributes(params);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.dialog_select_image_layout, null,
                false);

        mBtnFile = (Button) view.findViewById(R.id.btn_file);
        mBtnPhoto = (Button) view.findViewById(R.id.btn_photos);

        mBtnPhoto.setOnClickListener(mListener);
        mBtnFile.setOnClickListener(mListener);

        return view;
    }

    public void setListener(View.OnClickListener listener) {
        mListener = listener;
    }

}
