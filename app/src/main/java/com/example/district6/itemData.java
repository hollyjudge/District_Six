package com.example.district6;

/*
    Provides structure for data to be used
    to populate a spinner in an array in MainActivityJava
 */
public class itemData {

    private final String text;
    private final Integer imageId;

    public itemData(String text, Integer imageId) {
        this.text = text;
        this.imageId = imageId;
    }

    public String getText() {
        return text;
    }

    public Integer getImageId() {
        return imageId;
    }
}