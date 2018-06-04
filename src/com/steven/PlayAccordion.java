package com.steven;

import com.steven.resp.MusicInfo;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

import java.util.List;

public class PlayAccordion implements EventHandler<WindowEvent>{
    public static ObjectProperty<MusicInfo> playIndex = new SimpleObjectProperty<>(
            new MusicInfo(null, null, null)
    );

    List<>
    @Override
    public void handle(WindowEvent event) {

    }
}
