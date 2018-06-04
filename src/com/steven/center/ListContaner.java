package com.steven.center;

import com.steven.resp.MusicInfo;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.util.List;

public class ListContaner {
    private static final int SONG_MAX_LEGTH = 200;

    private ListView<MusicInfo> listView;

    private StringProperty groupName;
    List<MusicInfo> cache;

    public ListContaner(String listName,  List<MusicInfo> data) {
        this.groupName = new SimpleStringProperty(listName);
        this.listView = new ListView<MusicInfo>();
        // this.listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.listView.setItems(FXCollections.observableArrayList(data));
        this.listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


    }
}
