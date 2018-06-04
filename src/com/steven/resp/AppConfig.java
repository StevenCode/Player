package com.steven.resp;

import com.steven.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class AppConfig {
    public static final String LYRIC_DIR = "lyricDir";

    //应用歌曲数据是否改变
    public static boolean dataChange = false;
    private static boolean settingsChange = false;
    private static Properties configs;

    public static boolean isSettingsChange() {
        return settingsChange;
    }

    public static void saveSetting() {
        if (!settingsChange) {
            return;
        }

        String dir = System.getenv("APPDATA");
        File file = new File(dir, "Zplayer/zplayer.properties");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            configs.store(fos, "GBK");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(fos);
        }
    }

    public static String getConfig(String key) {
        return configs.getProperty(key);
    }

    public static void updateConfig(String key, String value) {
        settingsChange = true;

        configs.put(key, value);
    }

    public static File getAppIndex(){
        String dir = System.getenv("APPDATA");
        File file = new File(dir, "Zplayer/Index");
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public static void loadConfigs() {
        if (configs != null) {
            return;
        }
        String dir = System.getenv("APPDATA");
        File file = new File(dir, "Zplayer/zplayer.properties");
        configs = new Properties();

        if(!file.exists()) return;
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(file);
            configs.load(fis);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            IOUtils.closeQuietly(fis);
        }
    }

    public static void debug(Object obj){
        System.out.println(obj);
    }

}
