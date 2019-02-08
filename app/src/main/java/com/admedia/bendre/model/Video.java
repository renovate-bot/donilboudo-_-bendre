package com.admedia.bendre.model;

import java.util.ArrayList;
import java.util.List;

public class Video {
    private String title;
    private String url;

    public Video(String title, String url) {
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
        videos.add(new Video("Le piège des Kim", "https://www.youtube.com/watch?v=mPZRk0UA24k"));
        videos.add(new Video("Le monde selon Xi Jinping", "https://www.youtube.com/watch?v=ow_tQQzukfQ"));
        videos.add(new Video("REPORTAGE sur le MIT de Boston, La fabrique de génies (Sept à Huit) 2018", "https://www.youtube.com/watch?v=ag45W_FTTiM&t=75s"));
        videos.add(new Video("Le Safran : l'épice la plus convoitée du monde", "https://www.youtube.com/watch?v=hDi6sVVO6cI"));
//        videos.add(new Video("", ""));
//        videos.add(new Video("", ""));
//        videos.add(new Video("", ""));

        return videos;
    }
}
