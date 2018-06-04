package com.steven;

import com.steven.utils.ResourceManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class UIBuilder {
    public static Menu buildMenu(EventHandler<ActionEvent> menuHandler) {
        MenuItem back = null, about = null, setting = null;
        Menu result = MenuBuilder.create().text("      ").items(new MenuItem("  登录",
                        ResourceManager.getViewOfClassPath("menu_login.png")),
                MenuBuilder.create().text("  添加本地歌曲").items(new MenuItem("  添加歌曲"), new MenuItem("  添加歌曲文件夹"))
                        .build(), new SeparatorMenuItem(), new MenuItem("  显示桌面歌词"),
                new MenuItem("  显示音乐库", ResourceManager.getViewOfClassPath("menu_check.png")),
                new MenuItem("  显示均衡器"), new SeparatorMenuItem(),
                new MenuItem("  皮肤颜色", ResourceManager.getViewOfClassPath("menu_skin.png")),
                MenuBuilder.create().text("  迷你模式").items(new MenuItem("  音乐魔方模式"),
                        new MenuItem("  经典微型模式")).build(), new MenuItem("更多音乐工具"),
                MenuBuilder.create().text("  帮助与反馈").graphic(ResourceManager.getViewOfClassPath("menu_help.png")).items(
                        back = new MenuItem(" 意见反馈\t", ResourceManager.getViewOfClassPath("menu_back.png")),
                        about = new MenuItem(" 关于player")).build(),
                setting = new MenuItem(" 选项设置", ResourceManager.getViewOfClassPath("menu_set.png")),
                new MenuItem(" 退出", ResourceManager.getViewOfClassPath("menu_exit.png"))
        ).build();

        back.setOnAction(menuHandler);
        about.setOnAction(menuHandler);
        setting.setOnAction(menuHandler);

        return result;

    }

    public static Node buildGroupBtn() {
        return LabelBuilder.create().id("group_btn").prefHeight(104 / 4).prefHeight(23).build();
    }

    public static ImageView loadpView(String fileName, double width, double height) {
        Image result = ResourceManager.loadClasspathImage(fileName);
        ImageView renVal = new ImageView(result);
        renVal.setViewport(new Rectangle2D(0, 0, width, height));
        return renVal;
    }

    static void mailToOrBrowse(boolean mail) {
        if (!Desktop.isDesktopSupported()) {
            return;
        }

        Desktop desktop = Desktop.getDesktop();
        try {
            if (desktop.isSupported(Desktop.Action.MAIL)&& mail) {
                desktop.mail(new URI("mailto:191232134@qq.com"));
            } else if (desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(new URI("www.shidingfeng.com"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void openDefaultBrowser(String http) {
        try {
            String cmd = "rundll32 url.dll,FileProtocolHandler " + http;
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
