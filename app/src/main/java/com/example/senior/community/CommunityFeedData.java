package com.example.senior.community;

public class CommunityFeedData {
    private String name, time, mainText;

    public CommunityFeedData(String name, String time, String mainText) {
        this.name = name;
        this.time = time;
        this.mainText = mainText;
    }

    public String getName() {
        return name;
    }
    public String getTime() {
        return time;
    }
    public String getMainText() {
        return mainText;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setMainText(String mainText) {
        this.mainText = mainText;
    }
}
