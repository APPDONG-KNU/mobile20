package com.example.senior;

public class Item {
    private int imageResId;
    private String text;
    private String text2;

    public Item(int imageResId, String text, String text2) {
        this.imageResId = imageResId;
        this.text = text;
        this.text2 = text2;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getText() {
        return text;
    }

    public String getText2(){return text2;}
}
