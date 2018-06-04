package com.steven;

import com.steven.resp.MusicInfo;
import javafx.scene.Node;

public abstract class AbstractView {
    MusicInfo music;
    Node view;

    public AbstractView(){
        this(null);
    }


    public AbstractView(MusicInfo info) {
        this.music = info;

        initView();
    }


    public void setMusic(MusicInfo music) {
        this.music = music;

        updateView();
    }

    public Node getView() {
        return view;
    }

    protected abstract void initView();

    protected abstract void updateView();
}
