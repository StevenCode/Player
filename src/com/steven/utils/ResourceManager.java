package com.steven.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.channels.FileLock;
import java.util.Random;

public class ResourceManager {
    static Image singer_cache = null;
    static Random random = null;

    public static boolean isWindows() {
        return System.getProperty("os.name").indexOf("Windows") != -1;
    }

    public static boolean isPreInstance() {
        FileLock lock = null;
        try {
            String dir = System.getenv("APPDATA");
            File flagFile = new File(dir, "Zplayer/zplayer.lock");
            if (!flagFile.exists()) {
                flagFile.createNewFile();
            }
            FileOutputStream fis = new FileOutputStream(flagFile);
            lock = fis.getChannel().tryLock();
        } catch (Exception e) {
            System.err.println("程序正在运行......");
        }

        return lock == null;
    }

    public static String getUrl(String fileName) {
        File file = new File(fileName);
        return file.toURI().toString();
    }

    public static String getResourceUrl(String classPath) {
        return ResourceManager.class.getResource(classPath).toString();
    }

    public static Image loadImage(String fileName) {
        Image result = null;
        FileInputStream image = null;
        try {
            image = new FileInputStream(fileName);
            result = new Image(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(image);
        }

        return result;
    }

    public static Image loadClasspathImage(String fileName) {
        if ("singer.png".equals(fileName) && singer_cache != null) {
            return singer_cache;
        }

        Image result = null;
        InputStream in = null;
        try {
            in = ResourceManager.class.getResourceAsStream("/com/steven/images/" + fileName);
            result = new Image(in);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(in);
        }
        if ("singer.png".equals(fileName) && singer_cache == null) {
            singer_cache = result;
        }

        return result;
    }

    public static ImageView getViewOfClassPath(String fileName) {
        Image result = loadClasspathImage(fileName);
        ImageView renVal = new ImageView(result);
        return renVal;
    }

    public static int getRandom(int limit){
        if(random == null) random = new Random();

        return random.nextInt(limit);
    }

    public static BufferedImage getTrayIcon(){
        try{
            return ImageIO.read(ResourceManager.class
                    .getResourceAsStream("/com/steven/images/icon.gif"));
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
