package com.luis.pojos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

public class Flexiones extends EUnTracked{

    private int numFlex;

    public Flexiones(){}

    public Flexiones(long millisActivity, int numFlex) {
        super(millisActivity);
        this.numFlex = numFlex;
    }

    public int getNumFlex() {
        return numFlex;
    }

    public void setNumFlex(int numFlex) {
        this.numFlex = numFlex;
    }
}
