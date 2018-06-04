package com.steven;

import com.steven.resp.AppConfig;
import com.steven.utils.ResourceManager;
import javafx.application.Platform;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TrayManager implements ActionListener{
    private static TrayManager _INSTANCE = new TrayManager();
    TrayIcon trayIcon;

    public TrayManager() {
        Platform.setImplicitExit(!ResourceManager.isWindows());

    }

    public static TrayManager getTrayManager(){
        return _INSTANCE;
    }

    public void initTray() {
        if (!ResourceManager.isWindows()) {
            return;
        }

        //任务栏图标菜单
        PopupMenu popup = new PopupMenu();
        MenuItem open = new MenuItem("打开/关闭");
        MenuItem quit = new MenuItem("退出");

        open.addActionListener(this);
        quit.addActionListener(this);

        popup.add(open);
        popup.add(quit);

        try {
            SystemTray tray = SystemTray.getSystemTray();
            trayIcon = new TrayIcon(ResourceManager.getTrayIcon(), "player", popup);
            trayIcon.setToolTip("player");
            tray.add(trayIcon);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clear(){
        if(trayIcon == null){
            clearAndQuit();
        }else{
            ViewsContext.stage().hide();
        }
    }

    public void clearAndQuit() {
        //TODO
        if (AppConfig.dataChange) {

        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.getClass() == TrayIcon.class) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (!ViewsContext.stage().isShowing()) {
                        ViewsContext.stage().show();
                    }
                }
            });
            return;
        }

        MenuItem item = (MenuItem) source;
        if ("退出".equals(item.getLabel())) {
            SystemTray.getSystemTray().remove(trayIcon);
            clearAndQuit();
        } else if ("打开/关闭".equals(item.getLabel())) {
            Platform.runLater(new Runnable(){
                public void run(){
                    if(!ViewsContext.stage().isShowing()){
                        ViewsContext.stage().show();
                    }else{
                        ViewsContext.stage().hide();
                    }
                }
            });
        }
    }
}
