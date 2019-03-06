package com.admedia.bendre.model;

import java.util.ArrayList;
import java.util.List;

public class Video {
    private String title;
    private String url;

    private Video(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public static List<Video> DUMMIES() {
        List<Video> videos = new ArrayList<>();
        videos.add(new Video("A 13 year old healer in Burkina Faso vpro Metropolis\n", "kQ2lIc1zVlw"));
        videos.add(new Video("A 13 year old healer in Burkina Faso vpro Metropolis\n", "kQ2lIc1zVlw"));
//        videos.add(new Video("Le monde selon Xi Jinping", "ow_tQQzukfQ"));
//        videos.add(new Video("REPORTAGE sur le MIT de Boston, La fabrique de génies (Sept à Huit) 2018", "ag45W_FTTiM&t=75s"));
//        videos.add(new Video("Le Safran : l'épice la plus convoitée du monde", "hDi6sVVO6cI"));
//        videos.add(new Video("", ""));
//        videos.add(new Video("", ""));
//        videos.add(new Video("", ""));

        return videos;
    }
}
