package com.example.grafika;

import android.graphics.Path;
import android.util.Pair;

public class Stroke {

    // color of the stroke
    public int color;

    // width of the stroke
    public int strokeWidth;
    public Pair<Float,Float> beginCoor;

    public Pair<Float, Float> getEndCoor() {
        return endCoor;
    }

    public void setEndCoor(Pair<Float, Float> endCoor) {
        this.endCoor = endCoor;
    }

    public Pair<Float,Float> endCoor;
    // a Path object to
    // represent the path drawn
    public Path path;

    // constructor to initialise the attributes
    public Stroke(int color, int strokeWidth, Path path,Pair<Float,Float> beginCoor) {
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.path = path;
        this.beginCoor = beginCoor;
    }
    public Stroke(int color, int strokeWidth, Path path,Pair<Float,Float> beginCoor,Pair<Float,Float> endCoor) {
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.path = path;
        this.beginCoor = beginCoor;
        this.endCoor = endCoor;
    }
}