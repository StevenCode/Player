package com.steven.resp;

import com.steven.utils.IOUtils;

import java.text.DecimalFormat;
import java.time.Duration;

public class MusicInfo {
    public static int myindex = -1;

    public String group = "默认列表";
    public Duration time;
    public String url;

    public MusicInfo() {

    }

    public MusicInfo(String group, Duration time, String url) {
        this.group = group;
        this.time = time;
        this.url = url;
    }

    public MusicInfo(String url, String group) {
        this.url = url;
        this.group = group;
        this.time = Duration.ZERO;
    }

    public MusicInfo(String url) {
        this(url, "默认列表");
    }

    public String getFullname() {
        String filePath = IOUtils.decodeUrl(url, "UTF-8");
        int slash = filePath.lastIndexOf("/");
        filePath = filePath.substring(slash + 1);
        int dot = filePath.lastIndexOf(".");
        return filePath.substring(0, dot);
    }

    public String formatDuration() {
        double minutes = time.toMinutes();
        int minutesWhole = (int) Math.floor(minutes);
        int secondsWhole = (int) Math.round((minutes - minutesWhole) * 60);
        return String.format("%1$02d:%2$02d", minutesWhole, secondsWhole);
    }

    public String getFormat() {
        int dot = url.lastIndexOf(".");
        return url.substring(dot + 1);
    }

    public String getFileSize() {
        long length = IOUtils.getFileByUrl(url).length();
        double size = (double) length / (1024 * 1024);

        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(size) + "M";
    }

    public String getName() {
        String fullname = getFullname();
        String[] vp = fullname.split(" *?- *?");
        return vp[1];
    }

    public String getSinger() {
        String fullname = getFullname();

        String[] vp = fullname.split(" *?- *?");
        return vp[0];
    }

    public String toString() {
        return url;
    }

    public int hasCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;
        MusicInfo other = (MusicInfo) obj;
        if(group == null){
            if(other.group != null) return false;
        }else if(!group.equals(other.group)) return false;
        if(url == null){
            if(other.url != null) return false;
        }else if(!url.equals(other.url)) return false;
        return true;
    }

}
