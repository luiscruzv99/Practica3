package com.luis.pojos;

import android.location.Location;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.util.ArrayList;

public class Bici extends ETracked{

    private double avgSpeed; //En Km/H


    public Bici(){}

    public Bici(long millisActivity, double avgSpeed, ArrayList<Localizacion> path) {
        super(millisActivity, path);
        this.avgSpeed = avgSpeed;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }
}
