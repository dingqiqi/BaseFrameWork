package com.dingqiqi.baseframework.mode;

import java.io.Serializable;

/**
 * Created by dingqiqi on 2016/12/30.
 */
public class BaseMode implements Serializable {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
