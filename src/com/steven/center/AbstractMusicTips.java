package com.steven.center;

import com.steven.ViewsContext;
import com.steven.resp.MusicInfo;
import com.steven.utils.Locals;
import com.steven.utils.ResourceManager;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.stage.Popup;
import javafx.stage.Screen;

public class AbstractMusicTips {
    private boolean contextmenushow = false;

    boolean instantiate = false;

    Popup popup;
    private Group content;
    private ImageView pbg;

    public AbstractMusicTips() {

    }

    protected void initTips() {
        popup = new Popup();
        popup.setAutoHide(true);
        popup.setAutoFix(true);
        popup.setHideOnEscape(false);

        initLayout();
    }


    public void menuShow(){
        this.contextmenushow = true;
    }

    public void menuHide(){
        this.contextmenushow = false;
    }

    private void initLayout() {
        content = new Group();

        pbg = ImageViewBuilder.create().fitHeight(102).fitWidth(257).build();

        ImageView head = ImageViewBuilder.create().id("tips_image").image(ResourceManager.loadClasspathImage("singer.png"))
                .layoutX(10).layoutY(20).build();

        Label text = LabelBuilder.create().alignment(Pos.CENTER_LEFT).layoutX(75).layoutY(20).build();
        Label infomation = LabelBuilder.create().layoutX(75).layoutY(55).build();

        content.getChildren().addAll(pbg, head, text, infomation);
        popup.getContent().add(content);
    }

    protected boolean postCheck(MusicInfo info) {
        return info == null || ViewsContext.stage().getY() < 6 || contextmenushow || popup.isShowing();
    }

    protected void attach(MusicInfo info, Node target) {
        Label text = (Label) content.getChildren().get(2);
        text.setText(info.getFullname());

        Label infomation = (Label) content.getChildren().get(3);
        infomation.setText("大小:" + info.getFileSize() + "\t文件格式" + info.getFormat() + "\n码率:000 Kbps 播放次数:10");

        Screen screen = Screen.getPrimary();
        double shiftTop = 32.0 - 15;
        if (Locals.getLeftScreen(target) + 260 > screen.getBounds().getWidth()) {
            pbg.setImage(ResourceManager.loadClasspathImage("tips_left.png"));
            popup.show(ViewsContext.stage(), Locals.getLeftScreen(target) - 255, Locals.getTopScreen(target));
        }else {
            pbg.setImage(ResourceManager.loadClasspathImage("tips_right.png"));
            double right = Locals.getRightScreen(target);
            if (target.getLayoutBounds().getWidth() == 284.0) {
                right += 13.0;
            }
            popup.show(ViewsContext.stage(), right, Locals.getTopScreen(target) - shiftTop);
        }
    }

}
