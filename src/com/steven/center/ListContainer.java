package com.steven.center;

import com.steven.PlayAccordion;
import com.steven.resp.MusicInfo;
import com.steven.utils.FontUtils;
import com.steven.utils.ResourceManager;
import com.steven.utils.StringUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.util.*;

public class ListContainer {
    private static final int SONG_MAX_LEGTH = 200;

    private ListView<MusicInfo> listView;

    private StringProperty groupName;
    List<MusicInfo> cache;

    public ListContainer(String listName, List<MusicInfo> data) {
        this.groupName = new SimpleStringProperty(listName);
        this.listView = new ListView<MusicInfo>();
        // this.listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.listView.setItems(FXCollections.observableArrayList(data));
        this.listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        initCellFactory();
    }

    private void initCellFactory() {
        listView.setCellFactory(new Callback<ListView<MusicInfo>, ListCell<MusicInfo>>() {
            @Override
            public ListCell<MusicInfo> call(ListView<MusicInfo> param) {
                return new MusicCell();
            }
        });
    }

    public String getGroup() {
        return groupName.get();
    }

    public StringProperty groupProperty() {
        return groupName;
    }

    public void setDataGroup(String group) {
        for (MusicInfo tempInfo : listView.getItems()) {
            tempInfo.group = group;
        }
    }

    public void filterData(List<MusicInfo> srs) {
        if (cache == null) {
            cache = new ArrayList<MusicInfo>();
            cache.addAll(listView.getItems());
        }

        if (srs == null) {
            srs = Collections.emptyList();
        }

        listView.getItems().setAll(srs);
    }

    public void backData() {
        if (cache != null) {
            listView.getItems().setAll(cache);
            cache.clear();
            cache = null;
        }
    }

    public void sortData() {
        Comparator<MusicInfo> comparator = new Comparator<MusicInfo>() {
            @Override
            public int compare(MusicInfo one, MusicInfo two) {
                return StringUtils.compare(one.getFullname(), two.getFullname());
            }
        };

        Collections.sort(listView.getItems(), comparator);
    }

    public ListView<MusicInfo> getView() {
        return listView;
    }

    public void updateBigCell() {
        MusicInfo info = listView.getItems().remove(0);
        listView.getItems().add(0, info);

        listView.scrollTo(MusicInfo.myindex);
        System.gc();

    }

    final class MusicCell extends ListCell<MusicInfo> implements EventHandler<MouseEvent> {
        Group cache = new Group();
        Label name, time, mv;

        public MusicCell() {
            setOnMouseClicked(this);
            setOnMouseEntered(this);
            setOnMouseExited(this);

            initCache();
        }

        private void initCache() {
            name = LabelBuilder.create().alignment(Pos.CENTER_LEFT).layoutY(10).prefHeight(23 + 6).build();
            time = LabelBuilder.create().alignment(Pos.CENTER_LEFT).layoutX(230).layoutY(10).prefHeight(23 + 6).build();
            mv = LabelBuilder.create().id("mv_btn").layoutX(200).layoutY(15).prefHeight(24).prefWidth(192 / 8).build();

            cache.getChildren().addAll(name, mv, time);
        }

        public void updateItem(MusicInfo info, boolean empty) {
            super.updateItem(info, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            }else {
                MusicInfo index = PlayAccordion.playIndex.get();
                if (getGroup().equals(index.group) && getItem() == index) {
                    setId("big-cell");
                    setGraphic(getInfo2(info));
                }else {
                    simpleView(info);
                }
            }
        }

        private void simpleView(MusicInfo info) {
            int my_index = getIndex() + 1;
            String songName = info.getFullname();
            boolean mvShow = ResourceManager.getRandom(30) < 8;

            name.setText(" " + (my_index >= 10 ? my_index + "" : "0" + my_index) + "  " + adjustSongName(songName, mvShow));
            time.setText(info.formatDuration());
            mv.setVisible(mvShow);

            setGraphic(cache);
        }

        private String adjustSongName(String songName, boolean mvShow) {
            int length = FontUtils.getPixLength(songName, time.getFont());
            if (length < SONG_MAX_LEGTH - 20) {
                return songName;
            }

            int subLength = 1, srcLegnth = songName.length();
            int tempLength = mvShow ? SONG_MAX_LEGTH - 20 : SONG_MAX_LEGTH;
            while (length > tempLength) {
                length = FontUtils.getPixLength(songName.substring(0, srcLegnth - subLength), time.getFont());
                subLength++;
            }

            return songName.substring(0, srcLegnth - subLength) + "...";
        }


        @Override
        public void handle(MouseEvent event) {
            if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                DelayMusicTips.getMusicTips().delayDetach();
                if (event.getClickCount() == 2) {
                    PlayAccordion.playIndex.set(getItem());
                    MusicInfo.myindex = getIndex();

                    updateBigCell();
                }
            } else if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
                DelayMusicTips.getMusicTips().delayAttach(getItem(), this);
            } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
                DelayMusicTips.getMusicTips().delayDetach();

            }
        }

        Node getInfo2(MusicInfo info) {
            int my_index = getIndex() + 1;
            return new BigCell(info, my_index);
        }
    }
}
