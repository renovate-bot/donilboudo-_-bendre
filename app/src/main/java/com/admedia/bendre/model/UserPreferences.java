package com.admedia.bendre.model;

public class UserPreferences {
    private int id;
    private boolean enableNotifications;

    public UserPreferences(int id, int enableNotif) {
        this.id = id;
        this.enableNotifications = enableNotif != 0;
    }

    public UserPreferences () {

    }

    public boolean isEnableNotifications() {
        return enableNotifications;
    }

    public void setEnableNotifications(boolean enableNotifications) {
        this.enableNotifications = enableNotifications;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
