package com.steven.center;

import com.steven.resp.MusicInfo;
import javafx.application.Platform;
import javafx.scene.Node;

import java.util.concurrent.*;

public class DelayMusicTips extends AbstractMusicTips {
    private static DelayMusicTips tips = new DelayMusicTips();

    ScheduledFuture<?> sf;
    ScheduledExecutorService service;


    public DelayMusicTips() {
        super();
    }

    protected void initIfNeeded() {
        if (super.instantiate) {
            return;
        }

        super.initTips();
        service = Executors.newSingleThreadScheduledExecutor();
        instantiate = true;
    }

    public static DelayMusicTips getMusicTips() {
        return tips;
    }

    public void dispose() {
        if (!super.instantiate) {
            return;
        }
        if (sf != null) {
            sf.cancel(true);
        }
        service.shutdown();
    }

    void delayAttach(MusicInfo info, Node tartget) {
        initIfNeeded();
        if (postCheck(info)) {
            return;
        }
        if (sf != null) {
            sf.cancel(false);
        }
        sf = service.schedule(new ShowTask(info, tartget), 200, TimeUnit.MILLISECONDS);
    }

    void delayDetach() {
        if (sf != null) {
            sf.cancel(false);
        }
        popup.hide();
    }

    class ShowTask implements Runnable{
        MusicInfo musicInfo;
        Node node;

        public ShowTask(MusicInfo mi, Node node){
            this.musicInfo = mi;
            this.node = node;
        }

        public void run() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    attach(musicInfo, node);
                }
            });
        }
    }

}
